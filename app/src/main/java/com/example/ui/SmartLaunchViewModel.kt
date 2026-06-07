package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DocumentItem
import com.example.data.NotificationItem
import com.example.data.ServiceRequest
import com.example.data.SmartLaunchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class Screen {
    Splash,
    Login,
    MainMenu,
    ServiceRequestForm,
    ServiceTracking,
    ServiceHistory,
    DocumentManagement,
    NotificationCenter,
    OperationalReports,
    CompanyProfile,
    PlanComparison,
    PremiumDashboard
}

enum class PricePlan(val planName: String, val level: Int) {
    Basic("Smart Basic", 1),
    Control("Smart Control", 2),
    Premium("Smart Premium", 3)
}

data class UserSession(
    val username: String = "coordinador.callao",
    val company: String = "Naviera Cosmos S.A.",
    val activePlan: PricePlan = PricePlan.Premium,
    val role: String = "Supervisor de Operaciones Portuarias"
)

class SmartLaunchViewModel(private val repository: SmartLaunchRepository) : ViewModel() {

    // Main Navigation State
    private val _currentScreen = MutableStateFlow(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Navigation backstack helper
    private val navigationStack = mutableListOf<Screen>()

    // Active User Session State
    private val _userSession = MutableStateFlow(UserSession())
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    // Flow states directly connected to Room Database
    val serviceRequests: StateFlow<List<ServiceRequest>> = repository.allRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<DocumentItem>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected service for realtime tracking details
    private val _selectedTrackRequest = MutableStateFlow<ServiceRequest?>(null)
    val selectedTrackRequest: StateFlow<ServiceRequest?> = _selectedTrackRequest.asStateFlow()

    init {
        // Run a simulation task to moveboats/coordinates in real time
        viewModelScope.launch {
            while (true) {
                delay(4000) // update every 4 seconds
                simulateLanchaMovement()
            }
        }
    }

    // Navigation Controls
    fun navigateTo(screen: Screen) {
        navigationStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (navigationStack.isNotEmpty()) {
            _currentScreen.value = navigationStack.removeAt(navigationStack.size - 1)
        } else {
            _currentScreen.value = Screen.MainMenu
        }
    }

    fun forceResetToMainMenu() {
        navigationStack.clear()
        _currentScreen.value = Screen.MainMenu
    }

    // Login Action
    fun loginCorporate(ruc: String, user: String, pass: String): Boolean {
        if (ruc.isNotBlank() && user.isNotBlank() && pass.isNotBlank()) {
            val plan = when {
                ruc.startsWith("20") && ruc.endsWith("9") -> PricePlan.Premium
                ruc.endsWith("5") -> PricePlan.Control
                else -> PricePlan.Basic
            }
            _userSession.update {
                it.copy(
                    username = user,
                    company = when (ruc) {
                        "20100456789" -> "Agencia Marítima Cosmos S.A."
                        "20330441235" -> "Terminales Portuarios Chancay SAC"
                        else -> "Operadores Logísticos del Pacífico"
                    },
                    activePlan = plan,
                    role = if (plan == PricePlan.Premium) "Administrador de Operaciones" else "Agente de Enlace"
                )
            }
            navigateTo(Screen.MainMenu)
            return true
        }
        return false
    }

    // Change Active Service Plan (Dynamic experience toggling!)
    fun updateActivePlan(plan: PricePlan) {
        _userSession.update {
            it.copy(activePlan = plan)
        }
        viewModelScope.launch {
            repository.insertNotification(
                NotificationItem(
                    title = "Plan Actualizado: ${plan.planName}",
                    message = "Su perfil ha cambiado exitosamente al nivel ${plan.planName}. Funcionalidades reconfiguradas.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun logout() {
        navigationStack.clear()
        _currentScreen.value = Screen.Login
    }

    // Create a new Service Request
    fun createServiceRequest(
        port: String,
        targetVessel: String,
        serviceType: String,
        notes: String,
        price: Double
    ) {
        val newReq = ServiceRequest(
            port = port,
            targetVessel = targetVessel,
            serviceType = serviceType,
            dateTime = "Hoy, " + java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date()),
            status = "Pendiente",
            requestedBy = _userSession.value.company,
            notes = notes,
            launchName = "-",
            latitude = when(port) {
                "Callao" -> -12.043
                "Chancay" -> -11.583
                "Paita" -> -5.078
                "Matarani" -> -17.001
                else -> -13.716
            },
            longitude = when(port) {
                "Callao" -> -77.165
                "Chancay" -> -77.272
                "Paita" -> -81.111
                "Matarani" -> -72.102
                else -> -76.222
            },
            price = price
        )

        viewModelScope.launch {
            val id = repository.insertRequest(newReq)
            // Send instant alert
            repository.insertNotification(
                NotificationItem(
                    title = "Nueva Solicitud Registrada",
                    message = "La lancha para $targetVessel en el puerto de $port ha sido ingresada correctamente. N° Operación: $id.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Select service for real-time tracking
    fun selectRequestForTracking(request: ServiceRequest) {
        _selectedTrackRequest.value = request
        navigateTo(Screen.ServiceTracking)
    }

    fun markNotificationRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun clearNotifications() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }

    // Add Document File
    fun addNewDocument(title: String, filename: String, category: String, size: String, expiry: String) {
        val doc = DocumentItem(
            title = title,
            filename = filename,
            category = category,
            uploadDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
            expiryDate = expiry,
            status = "Vigente",
            size = size
        )
        viewModelScope.launch {
            repository.insertDocument(doc)
            repository.insertNotification(
                NotificationItem(
                    title = "Documento Cargado",
                    message = "El documento '$title' fue registrado satisfactoriamente en el archivo digital portuario.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteDocument(id: Int) {
        viewModelScope.launch {
            repository.deleteDocument(id)
        }
    }

    // Simulated Realtime Map Engine: moves active boats and triggers notifications!
    private suspend fun simulateLanchaMovement() {
        val list = repository.allRequests.first()
        val enRutaList = list.filter { it.status == "En Ruta" }
        for (req in enRutaList) {
            // Jitter the coordinates slightly to demonstrate simulation
            val targetLat = req.latitude + (Math.random() - 0.5) * 0.0012
            val targetLon = req.longitude + (Math.random() - 0.5) * 0.0012
            val updated = req.copy(
                latitude = targetLat,
                longitude = targetLon
            )
            repository.updateRequest(updated)

            // Update state flow if this is the currently tracked unit
            if (_selectedTrackRequest.value?.id == req.id) {
                _selectedTrackRequest.value = updated
            }
        }

        // Random check: randomly accept a "Pendiente" request to simulate port control approval
        val pendientes = list.filter { it.status == "Pendiente" }
        if (pendientes.isNotEmpty() && Math.random() < 0.25) {
            val match = pendientes.random()
            val approved = match.copy(
                status = "Aprobado",
                launchName = listOf("Lancha 'Orca IV'", "Lancha 'Huascarán I'", "Lancha 'Pizarro II'").random()
            )
            repository.updateRequest(approved)
            repository.insertNotification(
                NotificationItem(
                    title = "Servicio Aprobado",
                    message = "La lancha para la nave ${match.targetVessel} en ${match.port} ha sido Aprobada y asignada.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        // Random check: move an "Aprobado" request with an assigned launch to "En Ruta"
        val aprobados = list.filter { it.status == "Aprobado" }
        if (aprobados.isNotEmpty() && Math.random() < 0.15) {
            val match = aprobados.random()
            val enRuta = match.copy(status = "En Ruta")
            repository.updateRequest(enRuta)
            repository.insertNotification(
                NotificationItem(
                    title = "Lancha en Ruta",
                    message = "La unidad ${match.launchName} ha zarpado hacia la nave ${match.targetVessel} en el puerto de ${match.port}.",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}

// ViewModel factory provider as we are using standard constructor DI
class SmartLaunchViewModelFactory(private val repository: SmartLaunchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmartLaunchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmartLaunchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
