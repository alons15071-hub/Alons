package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
class SmartLaunchRepository(private val dao: SmartLaunchDao) {

    // Service Requests
    val allRequests: Flow<List<ServiceRequest>> = dao.getAllRequests()

    suspend fun insertRequest(request: ServiceRequest): Long {
        return dao.insertRequest(request)
    }

    suspend fun updateRequest(request: ServiceRequest) {
        dao.updateRequest(request)
    }

    suspend fun deleteRequest(id: Int) {
        dao.deleteRequestById(id)
    }

    // Notifications
    val allNotifications: Flow<List<NotificationItem>> = dao.getAllNotifications()

    suspend fun insertNotification(notification: NotificationItem) {
        dao.insertNotification(notification)
    }

    suspend fun markNotificationAsRead(id: Int) {
        dao.markNotificationAsRead(id)
    }

    suspend fun clearNotifications() {
        dao.clearAllNotifications()
    }

    // Documents
    val allDocuments: Flow<List<DocumentItem>> = dao.getAllDocuments()

    suspend fun insertDocument(document: DocumentItem) {
        dao.insertDocument(document)
    }

    suspend fun deleteDocument(id: Int) {
        dao.deleteDocumentById(id)
    }

    // Prepopulate database with realistic Peru Maritime port data if empty
    suspend fun prepopulateIfEmpty() {
        // Evaluate requests
        val currentRequests = dao.getAllRequests().first()
        if (currentRequests.isEmpty()) {
            val defaultRequests = listOf(
                ServiceRequest(
                    port = "Callao",
                    targetVessel = "MSC Kanoko",
                    serviceType = "Relevo de Tripulación",
                    dateTime = "07 de Jun, 10:30 AM",
                    status = "En Ruta",
                    requestedBy = "Agencia Marítima Cosmos S.A.",
                    notes = "Relevo urgente de 4 tripulantes y entrega de maletas. Coordinado con Capitanía.",
                    launchName = "Lancha 'Huascarán I'",
                    latitude = -12.040,
                    longitude = -77.165,
                    price = 450.0
                ),
                ServiceRequest(
                    port = "Chancay",
                    targetVessel = "Cosco Shipping Peru",
                    serviceType = "Inspectoría Técnica",
                    dateTime = "07 de Jun, 03:15 PM",
                    status = "Aprobado",
                    requestedBy = "Terminales Portuarios Chancay S.A.",
                    notes = "Embarque de 3 inspectores de SANIPES y práctico oficial de puerto.",
                    launchName = "Lancha 'Chancay II'",
                    latitude = -11.583,
                    longitude = -77.272,
                    price = 600.0
                ),
                ServiceRequest(
                    port = "Paita",
                    targetVessel = "Hamburg Süd Valparaíso",
                    serviceType = "Provisiones y Víveres",
                    dateTime = "06 de Jun, 08:30 AM",
                    status = "Completado",
                    requestedBy = "Líneas Logísticas del Norte",
                    notes = "Despacho de 1.5 toneladas de provisiones frescas y agua tratada.",
                    launchName = "Lancha 'Sullana V'",
                    latitude = -5.078,
                    longitude = -81.111,
                    price = 380.0
                ),
                ServiceRequest(
                    port = "Matarani",
                    targetVessel = "CMA CGM Callao",
                    serviceType = "Entrega de Repuestos",
                    dateTime = "05 de Jun, 04:00 PM",
                    status = "Completado",
                    requestedBy = "TISUR S.A.",
                    notes = "Cambio mecánico crítico: Bomba de inyección hidráulica para motor auxiliar.",
                    launchName = "Lancha 'Mollendo I'",
                    latitude = -17.001,
                    longitude = -72.102,
                    price = 550.0
                ),
                ServiceRequest(
                    port = "Pisco",
                    targetVessel = "MV Paracas Wave",
                    serviceType = "Relevo de Tripulación",
                    dateTime = "08 de Jun, 09:00 AM",
                    status = "Pendiente",
                    requestedBy = "Acuapesca Export SAC",
                    notes = "Embarque rutinario de tripulación entrante de relevo semanal.",
                    launchName = "-",
                    latitude = -13.716,
                    longitude = -76.222,
                    price = 320.0
                )
            )
            for (req in defaultRequests) {
                dao.insertRequest(req)
            }
        }

        // Evaluate notifications
        val currentNotifications = dao.getAllNotifications().first()
        if (currentNotifications.isEmpty()) {
            val defaultNotifications = listOf(
                NotificationItem(
                    title = "Servicio Autorizado - Callao",
                    message = "La lancha 'Huascarán I' obtuvo autorización de APN para acercarse a la nave MSC Kanoko.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 15, // 15 mins ago
                    isRead = false
                ),
                NotificationItem(
                    title = "Alerta de Oleaje - Chancay",
                    message = "Capitanía de Puerto reporta oleaje anómalo moderado. Se sugiere navegación a velocidad reducida.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
                    isRead = false
                ),
                NotificationItem(
                    title = "Certificado por Expirar",
                    message = "El Certificado de Navegabilidad de la unidad Sullana V expira en 5 días hábiles.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
                    isRead = false
                ),
                NotificationItem(
                    title = "Cierre de Puerto - Matarani",
                    message = "El terminal reabre operaciones de lanchas portuarias tras despejarse la niebla densa.",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 36, // 1.5 days ago
                    isRead = true
                )
            )
            for (notif in defaultNotifications) {
                dao.insertNotification(notif)
            }
        }

        // Evaluate documents
        val currentDocs = dao.getAllDocuments().first()
        if (currentDocs.isEmpty()) {
            val defaultDocs = listOf(
                DocumentItem(
                    title = "Certificado de Navegabilidad Huascarán I",
                    filename = "Cert_Naveg_Huascaran_2026.pdf",
                    category = "Certificado",
                    uploadDate = "2026-01-10",
                    expiryDate = "2026-12-31",
                    status = "Vigente",
                    size = "3.2 MB"
                ),
                DocumentItem(
                    title = "Póliza Responsabilidad Civil Armada",
                    filename = "Poliza_Seguro_RC_SmartLaunch.pdf",
                    category = "Póliza",
                    uploadDate = "2026-03-01",
                    expiryDate = "2027-02-28",
                    status = "Vigente",
                    size = "5.4 MB"
                ),
                DocumentItem(
                    title = "Permiso de Zarpe Vigente - Chancay II",
                    filename = "Zarpe_Mat_Chancay_II.pdf",
                    category = "Permiso",
                    uploadDate = "2026-05-15",
                    expiryDate = "2026-06-25",
                    status = "Vigente",
                    size = "1.8 MB"
                ),
                DocumentItem(
                    title = "Licencia de Operación Sanitaria Sullana V",
                    filename = "Lic_Sanitaria_SullanaV.pdf",
                    category = "Permiso",
                    uploadDate = "2025-06-12",
                    expiryDate = "2026-06-15",
                    status = "Por Vencer",
                    size = "2.1 MB"
                ),
                DocumentItem(
                    title = "Certificado de Seguridad de Radio Fluvial",
                    filename = "Cert_Radio_MollendoI.pdf",
                    category = "Certificado",
                    uploadDate = "2025-05-01",
                    expiryDate = "2026-05-01",
                    status = "Vencido",
                    size = "1.5 MB"
                )
            )
            for (doc in defaultDocs) {
                dao.insertDocument(doc)
            }
        }
    }
}
