package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DocumentItem
import com.example.data.NotificationItem
import com.example.data.ServiceRequest
import com.example.ui.PricePlan
import com.example.ui.Screen
import com.example.ui.SmartLaunchViewModel
import com.example.ui.UserSession
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// ============================================================================
// COMMONS & CORE WRAPPERS
// ============================================================================

@Composable
fun SmartLaunchHeader(
    title: String,
    subtitle: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .testTag("back_button")
                            .padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Representing Peru Port Authority badge logo
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Red)
                                .width(12.dp)
                                .height(8.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White)
                                .width(12.dp)
                                .height(8.dp)
                        )
                    }
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                if (actions != null) {
                    Row(content = actions)
                }
            }
        }
    }
}

// Sidebar Drawer Header style for corporate identity info
@Composable
fun SidebarHeader(session: UserSession) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OceanNavy, CoolCelesta)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsBoat,
                    contentDescription = "Boat",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "SMART LAUNCH",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "SERVICES PERÚ",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = session.company,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${session.username} (${session.role})",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.80f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing badges indicators
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        when (session.activePlan) {
                            PricePlan.Basic -> Color.Gray
                            PricePlan.Control -> SeaTeal
                            PricePlan.Premium -> MarineAmber
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = session.activePlan.planName.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// Sidebar menu item standard styling
@Composable
fun SidebarNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeValue: Int = 0
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) CoolCelesta.copy(alpha = 0.15f) else Color.Transparent,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) OceanNavy else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) OceanNavy else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            if (badgeValue > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Red)
                        .size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeValue.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// Universal bottom status info (shows current UTC context)
@Composable
fun CompactFooter(session: UserSession) {
    Surface(
        color = OceanNavy,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Terminal: ${session.company.take(15)}...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Text(
                text = "UTM - Zona 18 Sur (Perú)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}


// ============================================================================
// SCREEN 1: WELCOME SCREEN (SPLASH & PRESENTATION)
// ============================================================================

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OceanNavy, DarkNavyBase)
                )
            )
    ) {
        // Decorative Waves drawing in background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Draw grid guidelines
            for (i in 1..8) {
                val y = height * i / 9
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, y),
                    end = Offset(width, y)
                )
            }
            // Stylized port radars scope rings
            drawCircle(
                color = CoolCelesta.copy(alpha = 0.04f),
                center = Offset(width / 2, height / 2),
                radius = width * 0.4f,
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = CoolCelesta.copy(alpha = 0.02f),
                center = Offset(width / 2, height / 2),
                radius = width * 0.7f,
                style = Stroke(width = 3.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section (Peru Flag & Brand Details)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Peru Shield Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Red)
                            .width(14.dp)
                            .height(10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White)
                            .width(14.dp)
                            .height(10.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Red)
                            .width(14.dp)
                            .height(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AUTORIDAD PORTUARIA",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "v2.6.4-PRO",
                    style = MaterialTheme.typography.labelSmall,
                    color = CoolCelesta,
                    fontWeight = FontWeight.Bold
                )
            }

            // Center Logo & Corporate Description
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                // Boat Graphic Illustration using layered shapes
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBoat,
                        contentDescription = "Smart Launch Services",
                        tint = CoolCelesta,
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "SMART LAUNCH SERVICES",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Gestión & Coordinación Marítima Digital",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = CoolCelesta,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Plataforma integrada para la administración, rastreo de lanchas auxiliares y despachos operativos en los terminales del Callao, Chancay, Paita, Matarani y Pisco.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Bottom CTA Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CoolCelesta),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("welcome_start_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "INGRESAR AL PANEL CORPORATIVO",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Anchor,
                            contentDescription = "Sign In"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Conexión Encriptada APN SSL v3 • Smart Launch",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}


// ============================================================================
// SCREEN 2: CORPORATE LOGIN SCREEN (AUTHENTICATION & TIERS DIRECTIVES)
// ============================================================================

@Composable
fun LoginScreen(onLoginSuccess: (Ruc: String, User: String, Pass: String) -> Boolean) {
    var ruc by remember { mutableStateOf("20100456789") } // Default Cosmos Callao
    var user by remember { mutableStateOf("coordinador.callao") }
    var password by remember { mutableStateOf("cosmos2026") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showRucTips by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Elegant maritime header banner
            Surface(
                color = OceanNavy,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBoat,
                        contentDescription = "Logo",
                        tint = CoolCelesta,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Smart Launch Services",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Sistemas Intranet Portuario del Perú",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Screen Contents
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "Autenticación de Sucursal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OceanNavy
                )
                Text(
                    text = "Ingrese los datos de su agencia acreditada para habilitar el despacho de lanchas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Input RUC
                OutlinedTextField(
                    value = ruc,
                    onValueChange = { if (it.length <= 11) ruc = it },
                    label = { Text("RUC de la Empresa (11 dígitos)") },
                    placeholder = { Text("RUC comenzando con 20...") },
                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = "RUC") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("ruc_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                // Input Usuario
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("ID de Usuario") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User ID") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("user_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                // Input Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña de Acceso") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Pass") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("password_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, Color.Red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Error, contentDescription = "Error", tint = Color.Red)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = errorMessage ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }
                }

                // Call to action button
                Button(
                    onClick = {
                        if (ruc.length < 11) {
                            errorMessage = "El RUC de la empresa debe tener exactamente 11 dígitos"
                        } else {
                            val success = onLoginSuccess(ruc, user, password)
                            if (!success) {
                                errorMessage = "Autenticación fallida. Ingrese datos válidos."
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OceanNavy),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit")
                ) {
                    Text(
                        text = "CONECTAR SUCURSAL",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Interactive directives card explaining how RUC configures active Price levels
                if (showRucTips) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CoolCelesta.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, CoolCelesta.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = OceanNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Directivas de Demostración",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanNavy
                                    )
                                }
                                IconButton(
                                    onClick = { showRucTips = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Cerrar",
                                        tint = OceanNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "El sistema asignará el perfil de servicio según la firma del RUC:",
                                style = MaterialTheme.typography.labelSmall,
                                color = OceanNavy,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            BulletText("RUC que inicia con 20 y termina en 9 (ej: 20100456789) -> Smart Premium (Cosmos S.A.)")
                            BulletText("RUC que termina en 5 (ej: 20330441235) -> Smart Control (Terr. Chancay)")
                            BulletText("Cualquier otro RUC -> Smart Basic")

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Nota: Puede cambiar de plan libremente en cualquier momento desde la pantalla de comparación de planes.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            CompactFooter(
                UserSession(
                    username = "Sin Conexión",
                    company = "Terminal No Identificado",
                    activePlan = PricePlan.Basic,
                    role = "-"
                )
            )
        }
    }
}

@Composable
fun BulletText(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("•", color = CoolCelesta, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground)
    }
}


// ============================================================================
// SCREEN 3: MAIN MENU (HUB CONTROL CENTER)
// ============================================================================

@Composable
fun MainMenuScreen(
    viewModel: SmartLaunchViewModel,
    onMenuSelected: (Screen) -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val requests by viewModel.serviceRequests.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val pendingCount = requests.count { it.status == "Pendiente" }
    val activeTransitCount = requests.count { it.status == "En Ruta" }
    val unreadNotifCount = notifications.count { !it.isRead }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header containing pricing banner details
            SmartLaunchHeader(
                title = "Muro de Control Marítimo",
                subtitle = "Bienvenido, ${session.username}",
                actions = {
                    Box(modifier = Modifier.padding(end = 4.dp)) {
                        IconButton(onClick = { onMenuSelected(Screen.NotificationCenter) }) {
                            Icon(
                                imageVector = if (unreadNotifCount > 0) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = if (unreadNotifCount > 0) MarineAmber else Color.White
                            )
                        }
                        if (unreadNotifCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .size(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    badgeValueString(unreadNotifCount),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            )

            // Content Scroll
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Tier Plan Banner Alert
                PlanStatusBarBanner(session = session) {
                    onMenuSelected(Screen.PlanComparison)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats/Summary Cards Carousel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatsSummaryCard(
                        title = "En Tránsito",
                        count = activeTransitCount.toString(),
                        tint = CoolCelesta,
                        bgColor = CoolCelesta.copy(alpha = 0.08f),
                        icon = Icons.Default.DirectionsBoat,
                        modifier = Modifier.weight(1f)
                    )
                    StatsSummaryCard(
                        title = "Asignaciones",
                        count = requests.count { it.status == "Aprobado" }.toString(),
                        tint = SeaTeal,
                        bgColor = SeaTeal.copy(alpha = 0.08f),
                        icon = Icons.Default.Task,
                        modifier = Modifier.weight(1f)
                    )
                    StatsSummaryCard(
                        title = "Pendientes APN",
                        count = pendingCount.toString(),
                        tint = MarineAmber,
                        bgColor = MarineAmber.copy(alpha = 0.08f),
                        icon = Icons.Default.HourglassEmpty,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Módulos de Coordinación",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OceanNavy
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2x2 or grid system representing the operations
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GridMenuCard(
                            title = "Solicitar Lancha",
                            sub = "Formulario Oficial",
                            icon = Icons.Default.AddModerator,
                            color = OceanNavy,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.ServiceRequestForm)
                        }
                        GridMenuCard(
                            title = "Rastreo GPS",
                            sub = "Simulación Radar Activo",
                            icon = Icons.Default.Radar,
                            color = CoolCelesta,
                            modifier = Modifier.weight(1f)
                        ) {
                            // If we have an active request in route, select it first, else default
                            val enRuta = requests.firstOrNull { it.status == "En Ruta" } ?: requests.firstOrNull()
                            if (enRuta != null) {
                                viewModel.selectRequestForTracking(enRuta)
                            } else {
                                onMenuSelected(Screen.ServiceTracking)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GridMenuCard(
                            title = "Historial Servicios",
                            sub = "Bitácora de Despacho",
                            icon = Icons.Default.History,
                            color = OceanNavy,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.ServiceHistory)
                        }
                        GridMenuCard(
                            title = "Gestión Documental",
                            sub = "Zarpes y Certificados",
                            icon = Icons.Default.FolderOpen,
                            color = OceanNavy,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.DocumentManagement)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GridMenuCard(
                            title = "Reportes KPI",
                            sub = "Registros de Eficiencia",
                            icon = Icons.Default.Assessment,
                            color = SeaTeal,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.OperationalReports)
                        }
                        GridMenuCard(
                            title = "Presencia Puertos",
                            sub = "Callao, Chancay S.A.",
                            icon = Icons.Default.Place,
                            color = OceanNavy,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.CompanyProfile)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GridMenuCard(
                            title = "Comparación de Planes",
                            sub = "Smart Tiers",
                            icon = Icons.Default.Compare,
                            color = CoolCelesta,
                            modifier = Modifier.weight(1f)
                        ) {
                            onMenuSelected(Screen.PlanComparison)
                        }
                        // Executive Dashboard for Premium clients
                        GridMenuCard(
                            title = "Dashboard Premium",
                            sub = "Acceso Clientes VIP",
                            icon = Icons.Default.Analytics,
                            color = if (session.activePlan == PricePlan.Premium) MarineAmber else Color.Gray,
                            modifier = Modifier.weight(1f),
                            badgeText = if (session.activePlan != PricePlan.Premium) "Bloqueado" else "VIP"
                        ) {
                            onMenuSelected(Screen.PremiumDashboard)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Log out action link in bottom
                OutlinedButton(
                    onClick = { viewModel.logout() },
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Salir")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DESCONECTAR SESIÓN CORPORATIVA RUC")
                }
            }

            CompactFooter(session)
        }
    }
}

// Stats summary card view helper
@Composable
fun StatsSummaryCard(
    title: String,
    count: String,
    tint: Color,
    bgColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = OceanNavy
            )
        }
    }
}

// Status Banner for plans
@Composable
fun PlanStatusBarBanner(
    session: UserSession,
    onNavigatePlan: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = OceanNavy),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when(session.activePlan) {
                            PricePlan.Basic -> Icons.Default.DirectionsBoat
                            PricePlan.Control -> Icons.Default.Speed
                            PricePlan.Premium -> Icons.Default.Shield
                        },
                        contentDescription = "Plan",
                        tint = when(session.activePlan) {
                            PricePlan.Basic -> Color.White
                            PricePlan.Control -> CoolCelesta
                            PricePlan.Premium -> MarineAmber
                        }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Firma Activa: ${session.username}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.70f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = session.activePlan.planName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Official",
                            tint = when(session.activePlan) {
                                PricePlan.Premium -> MarineAmber
                                else -> CoolCelesta
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onNavigatePlan,
                colors = ButtonDefaults.buttonColors(containerColor = CoolCelesta),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("CAMBIAR", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// Menu grid card
@Composable
fun GridMenuCard(
    title: String,
    sub: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        modifier = modifier
            .height(96.dp)
            .testTag("menu_card_${title.replace(" ", "_").lowercase()}")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = sub,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(RoundedCornerShape(bottomStart = 8.dp, topEnd = 12.dp))
                        .background(
                            if (badgeText == "VIP") MarineAmber else Color.Gray.copy(alpha = 0.5f)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


// ============================================================================
// SCREEN 4: SERVICE REQUEST FORM (SOLICITUD DE SERVICIOS)
// ============================================================================

@Composable
fun ServiceRequestFormScreen(
    viewModel: SmartLaunchViewModel,
    onFormSubmitted: () -> Unit,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()

    val ports = listOf("Callao", "Chancay", "Paita", "Matarani", "Pisco")
    val services = listOf(
        "Relevo de Tripulación",
        "Víveres y Comida",
        "Lubricantes y Repuestos",
        "Inspector de Puerto",
        "Asistencia de Práctico"
    )

    var selectedPort by remember { mutableStateOf(ports.first()) }
    var targetVessel by remember { mutableStateOf("MV Callao Express") }
    var selectedService by remember { mutableStateOf(services.first()) }
    var notes by remember { mutableStateOf("") }

    var portDropdownExpanded by remember { mutableStateOf(false) }
    var serviceDropdownExpanded by remember { mutableStateOf(false) }

    // Formula pricing
    val priceEstimate = when (selectedPort) {
        "Callao" -> 450.0
        "Chancay" -> 620.0
        "Paita" -> 390.0
        "Matarani" -> 550.0
        else -> 310.0 // Pisco
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Solicitar Lancha",
                subtitle = "Portuaria Autorizada - APN",
                onBackClick = onBack
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBoat,
                                contentDescription = "Lancha",
                                tint = CoolCelesta
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Formulario de Despacho",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = OceanNavy.copy(alpha = 0.1f)
                        )

                        // Port Selection dropdown
                        Text(
                            text = "Puerto de Operación (Perú)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                            OutlinedButton(
                                onClick = { portDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth().testTag("port_dropdown"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedPort, color = OceanNavy)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir", tint = OceanNavy)
                                }
                            }
                            DropdownMenu(
                                expanded = portDropdownExpanded,
                                onDismissRequest = { portDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                ports.forEach { portItem ->
                                    DropdownMenuItem(
                                        text = { Text(portItem) },
                                        onClick = {
                                            selectedPort = portItem
                                            portDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Target Vessel Name
                        Text(
                            text = "Nave Mercante Destino",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = targetVessel,
                            onValueChange = { targetVessel = it },
                            placeholder = { Text("Ej: MSC Daniela II, COSCO Callao") },
                            leadingIcon = { Icon(Icons.Default.DirectionsBoat, contentDescription = "Buque") },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .testTag("target_vessel_input")
                        )

                        // Service Category selection dropdown
                        Text(
                            text = "Categoría del Servicio Auxiliar",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                            OutlinedButton(
                                onClick = { serviceDropdownExpanded = true },
                                modifier = Modifier.fillMaxWidth().testTag("service_type_dropdown"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedService, color = OceanNavy)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir", tint = OceanNavy)
                                }
                            }
                            DropdownMenu(
                                expanded = serviceDropdownExpanded,
                                onDismissRequest = { serviceDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                services.forEach { serviceItem ->
                                    DropdownMenuItem(
                                        text = { Text(serviceItem) },
                                        onClick = {
                                            selectedService = serviceItem
                                            serviceDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Notes/Logistics details
                        Text(
                            text = "Instrucciones de Carga / Relevo (Detalle)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            placeholder = { Text("Especifique cantidad de tripulantes, bultos, peso, especificaciones de seguridad, etc.") },
                            minLines = 3,
                            maxLines = 5,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .testTag("notes_input")
                        )

                        // Pricing calculation box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CoolCelesta.copy(alpha = 0.08f)),
                            border = BorderStroke(1.dp, CoolCelesta.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Estimación Tarifaria de Despacho",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Puerto $selectedPort • Tarifa Base Lancha Auxiliar",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "${priceEstimate}0 USD",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = OceanNavy
                                    )
                                }
                            }
                        }

                        // Submit action Button
                        Button(
                            onClick = {
                                if (targetVessel.isNotBlank()) {
                                    viewModel.createServiceRequest(
                                        port = selectedPort,
                                        targetVessel = targetVessel,
                                        serviceType = selectedService,
                                        notes = notes,
                                        price = priceEstimate
                                    )
                                    onFormSubmitted()
                                }
                            },
                            enabled = targetVessel.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_request_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Submit")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ENVIAR REGISTRO DIGITAL APN")
                        }
                    }
                }
            }

            CompactFooter(session)
        }
    }
}


// ============================================================================
// SCREEN 5: REAL-TIME SERVICE TRACKING (GPS RADAR DRAWINGS)
// ============================================================================

@Composable
fun ServiceTrackingScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val trackedRequest by viewModel.selectedTrackRequest.collectAsState()

    // Blinking animation trigger for radar pulse
    var blinkState by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(800)
            blinkState = !blinkState
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "GPS Radar de Lanchas",
                subtitle = "Monitoreo en Tiempo Real - Zona 18S",
                onBackClick = onBack
            )

            if (trackedRequest == null) {
                // If no active boat click yet, request selection
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBoat,
                        contentDescription = "Radar",
                        tint = OceanNavy.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ningún servicio activo seleccionado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Vaya a la bitácora o historial de servicios y elija un servicio en ruta para seguir su ubicación GPS en vivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = OceanNavy),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("IR A BITÁCORA / HISTORIAL")
                    }
                }
            } else {
                val req = trackedRequest!!

                Column(modifier = Modifier.weight(1f)) {
                    // Tactical Radar viewbox using Canvas draws!
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1.2f)
                            .background(DarkNavyBase)
                    ) {
                        // Drawing marine chart lines
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Radar grids
                            drawCircle(color = CoolCelesta.copy(alpha = 0.15f), center = Offset(w/2, h/2), radius = h * 0.2f, style = Stroke(width = 1.dp.toPx()))
                            drawCircle(color = CoolCelesta.copy(alpha = 0.10f), center = Offset(w/2, h/2), radius = h * 0.4f, style = Stroke(width = 1.dp.toPx()))
                            drawCircle(color = CoolCelesta.copy(alpha = 0.05f), center = Offset(w/2, h/2), radius = h * 0.6f, style = Stroke(width = 1.dp.toPx()))

                            // Sweep lines mimicking port control scope
                            drawLine(color = CoolCelesta.copy(alpha = 0.1f), start = Offset(0f, h/2), end = Offset(w, h/2))
                            drawLine(color = CoolCelesta.copy(alpha = 0.1f), start = Offset(w/2, 0f), end = Offset(w/2, h))

                            // Draw channel routes indicators
                            drawLine(
                                color = SeaTeal.copy(alpha = 0.3f),
                                start = Offset(w * 0.1f, h * 0.8f),
                                end = Offset(w * 0.5f, h * 0.5f),
                                strokeWidth = 2.dp.toPx()
                            )
                            drawLine(
                                color = SeaTeal.copy(alpha = 0.3f),
                                start = Offset(w * 0.5f, h * 0.5f),
                                end = Offset(w * 0.9f, h * 0.2f),
                                strokeWidth = 2.dp.toPx()
                            )

                            // Vessel Anchor coordinate (fixed node in center-ish)
                            drawCircle(
                                color = Color.White,
                                center = Offset(w * 0.65f, h * 0.35f),
                                radius = 8.dp.toPx()
                            )
                            // Draw tracking path trail from lancha to boat
                            drawLine(
                                color = CoolCelesta.copy(alpha = 0.6f),
                                start = Offset(w * 0.3f, h * 0.7f),
                                end = Offset(w * 0.65f, h * 0.35f),
                                strokeWidth = 3.dp.toPx()
                            )
                        }

                        // Anchored Vessel Label
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(x = 65.dp, y = (-65).dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(SeaTeal)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "RECEPTOR: " + req.targetVessel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 8.sp
                            )
                        }

                        // Blinking GPS boat tracker over the path (simulated dynamic offset)
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                // Slight offset calculation mimicking its path
                                .offset(x = (-10).dp, y = 15.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (blinkState) CoolCelesta else CoolCelesta.copy(
                                                alpha = 0.3f
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.Black.copy(alpha = 0.7f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (req.launchName == "-") "Buscando..." else req.launchName,
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Radar Compass Indicator overlay
                        Text(
                            text = "RADIO RASTREO APN GPS • SIMULA ACTIVA",
                            style = MaterialTheme.typography.labelSmall,
                            color = CoolCelesta,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        )

                        Text(
                            text = "${req.port.uppercase()} CONTROL SECTOR ${
                                when (req.port) {
                                    "Callao" -> "C-4"
                                    "Chancay" -> "CH-1"
                                    "Paita" -> "P-3"
                                    else -> "S-2"
                                }
                            }",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.4f),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        )
                    }

                    // Bottom Telemetry Info Box (Pricing Tier adaptive!)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp)
                        ) {
                            // Section Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = req.serviceType,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanNavy
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Buque: ${req.targetVessel}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(
                                                    when (req.status) {
                                                        "En Ruta" -> CoolCelesta.copy(alpha = 0.15f)
                                                        "Aprobado" -> SeaTeal.copy(alpha = 0.15f)
                                                        "Completado" -> Color.Gray.copy(alpha = 0.15f)
                                                        else -> MarineAmber.copy(alpha = 0.15f)
                                                    }
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = req.status.uppercase(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when (req.status) {
                                                    "En Ruta" -> OceanNavy
                                                    "Aprobado" -> SeaTeal
                                                    "Completado" -> Color.Gray
                                                    else -> MarineAmber
                                                }
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "RUC SUCURSAL",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoolCelesta,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Divider(modifier = Modifier.padding(vertical = 12.dp), color = OceanNavy.copy(alpha = 0.1f))

                            // Telemetry numerical logs
                            Text(
                                text = "Métricas Telemetría de Lancha",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                TelemetryBox(label = "Latitud", value = String.format("%.5f", req.latitude), modifier = Modifier.weight(1f))
                                TelemetryBox(label = "Longitud", value = String.format("%.5f", req.longitude), modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Show detailed premium speed telemetry ONLY under Smart Control and Smart Premium.
                            // Basic users get restricted notice!
                            if (session.activePlan.level >= PricePlan.Control.level) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    TelemetryBox(label = "Velocidad GPS", value = "11.2 Nudos", modifier = Modifier.weight(1f))
                                    TelemetryBox(label = "Rumbe Récord", value = "284° WNW", modifier = Modifier.weight(1f))
                                    TelemetryBox(label = "ETA Buque", value = "9 minutos", modifier = Modifier.weight(1f))
                                }
                            } else {
                                // Locked warning for Smart Basic
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f)),
                                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "ETA, Rumbo y Velocidad en tiempo real bloqueados en Smart Basic.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Notas Operativas:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy
                            )
                            Text(
                                text = req.notes.ifBlank { "Sin especificaciones especiales ingresadas." },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }

            CompactFooter(session)
        }
    }
}

@Composable
fun TelemetryBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = LightMarineBackground),
        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.05f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = OceanNavy)
        }
    }
}


// ============================================================================
// SCREEN 6: SERVICE HISTORY (BITÁCORA INTEGRAL)
// ============================================================================

@Composable
fun ServiceHistoryScreen(
    viewModel: SmartLaunchViewModel,
    onTrackRequest: (ServiceRequest) -> Unit,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val requests by viewModel.serviceRequests.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedPortFilter by remember { mutableStateOf("Todos") }

    val filteredList = requests.filter {
        val matchesPort = (selectedPortFilter == "Todos" || it.port == selectedPortFilter)
        val matchesQuery = (it.targetVessel.contains(searchQuery, ignoreCase = true) ||
                it.serviceType.contains(searchQuery, ignoreCase = true) ||
                it.launchName.contains(searchQuery, ignoreCase = true))
        matchesPort && matchesQuery
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Bitácora Corporativa",
                subtitle = "Historial Completo de Operaciones",
                onBackClick = onBack
            )

            // Filtering Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar por nave, servicio, lancha...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    // Horizontally scrolling of port chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val filterPorts = listOf("Todos", "Callao", "Chancay", "Paita", "Matarani", "Pisco")
                        filterPorts.forEach { portItem ->
                            val isSelected = selectedPortFilter == portItem
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isSelected) OceanNavy else LightMarineBackground)
                                    .clickable { selectedPortFilter = portItem }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = portItem,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else OceanNavy
                                )
                            }
                        }
                    }
                }
            }

            // Results List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.HourglassDisabled, contentDescription = "Vacio", tint = OceanNavy.copy(alpha = 0.3f), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No se encontraron registros de lanchas", style = MaterialTheme.typography.bodyMedium, color = OceanNavy)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { req ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Clicking an active one launches radar tracking
                                    onTrackRequest(req)
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsBoat,
                                            contentDescription = "Boat",
                                            tint = CoolCelesta,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = req.port.uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CoolCelesta
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .background(
                                                when (req.status) {
                                                    "En Ruta" -> CoolCelesta.copy(alpha = 0.15f)
                                                    "Aprobado" -> SeaTeal.copy(alpha = 0.15f)
                                                    "Completado" -> Color.Gray.copy(alpha = 0.15f)
                                                    else -> MarineAmber.copy(alpha = 0.15f)
                                                }
                                            )
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = req.status.uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (req.status) {
                                                "En Ruta" -> OceanNavy
                                                "Aprobado" -> SeaTeal
                                                "Completado" -> Color.Gray
                                                else -> MarineAmber
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = req.serviceType,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanNavy
                                )
                                Text(
                                    text = "Nave Mercante: ${req.targetVessel}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                )

                                if (req.launchName.isNotBlank() && req.launchName != "-") {
                                    Text(
                                        text = "Unidad Asignada: ${req.launchName}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanNavy,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    color = OceanNavy.copy(alpha = 0.05f)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = req.dateTime,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )

                                    // Display actions under each tier level
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Ver Rastreo",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CoolCelesta
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Icon(Icons.Default.ArrowRight, contentDescription = "Rastreo", tint = CoolCelesta, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            CompactFooter(session)
        }
    }
}


// ============================================================================
// SCREEN 7: DOCUMENT MANAGEMENT & CERTIFICATES (GESTIÓN DOCUMENTAL)
// ============================================================================

@Composable
fun DocumentManagementScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val documents by viewModel.documents.collectAsState()

    var showUploadModal by remember { mutableStateOf(false) }

    // Upload Form States
    var docTitle by remember { mutableStateOf("") }
    var docCategory by remember { mutableStateOf("Certificado") }
    var docExpiry by remember { mutableStateOf("2026-12-31") }
    var docFilename by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Archivo Digital APN",
                subtitle = "Certificados de Zarpe y Polizas",
                onBackClick = onBack,
                actions = {
                    IconButton(onClick = { showUploadModal = true }) {
                        Icon(Icons.Default.CloudUpload, contentDescription = "Upload", tint = Color.White)
                    }
                }
            )

            // Category scrolling filters
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Documentación e Inspección Marítima",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy
                    )
                    Text(
                        text = "Vigile los plazos vigentes de licencias y pólizas para evitar paralizaciones por Capitanía.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                items(documents) { doc ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when (doc.category) {
                                            "Certificado" -> SeaTeal.copy(alpha = 0.10f)
                                            "Póliza" -> MarineAmber.copy(alpha = 0.10f)
                                            else -> CoolCelesta.copy(alpha = 0.10f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (doc.category == "Póliza") Icons.Default.Gavel else Icons.Default.Description,
                                    contentDescription = doc.category,
                                    tint = when (doc.category) {
                                        "Certificado" -> SeaTeal
                                        "Póliza" -> MarineAmber
                                        else -> CoolCelesta
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanNavy,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Archivo: ${doc.filename} • ${doc.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "Vence: ${doc.expiryDate}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (doc.status == "Vencido") Color.Red else OceanNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            when (doc.status) {
                                                "Vigente" -> SeaTeal.copy(alpha = 0.15f)
                                                "Por Vencer" -> MarineAmber.copy(alpha = 0.15f)
                                                else -> Color.Red.copy(alpha = 0.15f)
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = doc.status.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (doc.status) {
                                            "Vigente" -> SeaTeal
                                            "Por Vencer" -> MarineAmber
                                            else -> Color.Red
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Borrar",
                                    tint = Color.Red.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { viewModel.deleteDocument(doc.id) }
                                )
                            }
                        }
                    }
                }
            }

            // Simulated document upload overlay sheet
            if (showUploadModal) {
                AlertDialog(
                    onDismissRequest = { showUploadModal = false },
                    title = {
                        Text(
                            "Registrar Documento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy
                        )
                    },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = docTitle,
                                onValueChange = { docTitle = it },
                                label = { Text("Título descriptivo") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = docFilename,
                                onValueChange = { docFilename = it },
                                label = { Text("Nombre del Archivo (PDF)") },
                                placeholder = { Text("ej: zarpe_callao.pdf") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            // Dropdown selections
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val cats = listOf("Certificado", "Permiso", "Póliza")
                                cats.forEach { c ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (docCategory == c) OceanNavy else LightMarineBackground)
                                            .clickable { docCategory = c }
                                            .padding(horizontal = 10.dp, vertical = 8.dp)
                                            .weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            c,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (docCategory == c) Color.White else OceanNavy
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = docExpiry,
                                onValueChange = { docExpiry = it },
                                label = { Text("Fecha de Expiración (AAAA-MM-DD)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (docTitle.isNotBlank()) {
                                    viewModel.addNewDocument(
                                        title = docTitle,
                                        filename = if (docFilename.endsWith(".pdf")) docFilename else "$docFilename.pdf",
                                        category = docCategory,
                                        size = "2.5 MB",
                                        expiry = docExpiry
                                    )
                                    // Reset states
                                    docTitle = ""
                                    docFilename = ""
                                    showUploadModal = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanNavy)
                        ) {
                            Text("Guardar Certificado")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUploadModal = false }) {
                            Text("Cancelar", color = OceanNavy)
                        }
                    }
                )
            }

            CompactFooter(session)
        }
    }
}


// ============================================================================
// SCREEN 8: NOTIFICATION CENTER (CENTRO DE NOTIFICACIONES)
// ============================================================================

@Composable
fun NotificationCenterScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Alertas de Capitanía",
                subtitle = "Avisos de Puerto y Despacho",
                onBackClick = onBack,
                actions = {
                    IconButton(onClick = { viewModel.clearNotifications() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear All", tint = Color.White)
                    }
                }
            )

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Empty",
                            tint = OceanNavy.copy(alpha = 0.3f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No hay notificaciones ni avisos vigentes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OceanNavy
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Eventos del día",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy
                            )
                            TextButton(onClick = { viewModel.clearNotifications() }) {
                                Text("Limpiar Todo", color = CoolCelesta, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    items(notifications) { notif ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (notif.isRead) MaterialTheme.colorScheme.surface else CoolCelesta.copy(alpha = 0.04f)
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (notif.isRead) OceanNavy.copy(alpha = 0.05f) else CoolCelesta.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.markNotificationRead(notif.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (notif.isRead) Color.Gray.copy(alpha = 0.5f) else CoolCelesta)
                                        .align(Alignment.CenterVertically)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanNavy
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = notif.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            CompactFooter(session)
        }
    }
}


// ============================================================================
// SCREEN 9: OPERATIONAL REPORTS (KPI VISUALIZERS)
// ============================================================================

@Composable
fun OperationalReportsScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Estadísticas APN",
                subtitle = "Reportes y Rendimiento de Lanchas",
                onBackClick = onBack
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Indicadores de Productividad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OceanNavy
                )
                Text(
                    text = "Desglose estadístico del rendimiento del transporte de lanchas auxiliares en puertos nacionales.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // High-fidelity Bar chart of service volume by port
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Servicios Mensuales por Puerto (Cant.)",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom drawing bar graph
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Draw baseline
                                drawLine(color = OceanNavy.copy(alpha = 0.2f), start = Offset(0f, h - 20f), end = Offset(w, h - 20f))

                                // Port heights estimation
                                val portStats = listOf(
                                    Pair("Callao", 0.85f),
                                    Pair("Chancay", 0.65f),
                                    Pair("Paita", 0.40f),
                                    Pair("Matarani", 0.50f),
                                    Pair("Pisco", 0.30f)
                                )

                                val barSpacing = w / portStats.size
                                portStats.forEachIndexed { idx, pair ->
                                    val barWidth = 24.dp.toPx()
                                    val barHeight = (h - 40f) * pair.second
                                    val xOffset = (idx * barSpacing) + (barSpacing / 2) - (barWidth / 2)

                                    // Draw bar background structure
                                    drawRect(
                                        color = CoolCelesta.copy(alpha = 0.1f),
                                        size = androidx.compose.ui.geometry.Size(barWidth, h - 20f),
                                        topLeft = Offset(xOffset, 0f)
                                    )

                                    // Draw active value bar
                                    drawRect(
                                        color = if (pair.first == "Chancay") SeaTeal else OceanNavy,
                                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                                        topLeft = Offset(xOffset, h - 20f - barHeight)
                                    )
                                }
                            }

                            // Lay labels on top
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val labels = listOf("Callao [58]", "Chancay [42]", "Paita [24]", "Matarani [31]", "Pisco [18]")
                                labels.forEach { l ->
                                    Text(
                                        text = l,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = OceanNavy,
                                        modifier = Modifier.width(52.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Efficiency KPIs Panel
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "KPIs de Eficiencia Global",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = OceanNavy
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        KPIRow(label = "Tiempo Medio de Respuesta", value = "34.5 mins", progress = 0.88f, tint = SeaTeal)
                        KPIRow(label = "Tasa de Satisfacción Naviera", value = "98.4%", progress = 0.98f, tint = SeaTeal)
                        KPIRow(label = "Eficiencia de Consumo de Combustible", value = "8.4 Gl/S", progress = 0.72f, tint = CoolCelesta)
                        KPIRow(label = "Frecuencia de Uso Mensual", value = "173 despachos", progress = 0.60f, tint = CoolCelesta)
                    }
                }
            }

            CompactFooter(session)
        }
    }
}

@Composable
fun KPIRow(label: String, value: String, progress: Float, tint: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.ExtraBold, color = OceanNavy)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = tint,
            trackColor = tint.copy(alpha = 0.15f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}


// ============================================================================
// SCREEN 10: COMPANY PROFILE (PRESENCIA PUERTOS DE PERÚ)
// ============================================================================

@Composable
fun CompanyProfileScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()

    val details = listOf(
        PortDetail("Terminal Portuario del Callao", "APN Callao", "12 lanchas rápidas", "relevos.callao@smartlaunch.pe", "Av. Jorge Chávez 140"),
        PortDetail("Megapuerto Multipropósito Chancay", "Cosco Terminal", "6 lanchas operativas", "chancay@smartlaunch.pe", "Av. Panamericana Norte km 80"),
        PortDetail("Terminal Portuario de Paita", "Paita Hub", "4 lanchas", "paita@smartlaunch.pe", "Jr. Jorge Chávez 230"),
        PortDetail("Terminal Portuario de Matarani", "TISUR Dock", "5 lanchas rápidas", "matarani@smartlaunch.pe", "Muelle de Matarani block C"),
        PortDetail("Puerto Paracas - Pisco", "Pisco Terminal", "3 lanchas de apoyo", "pisco@smartlaunch.pe", "Carretera Punta Pejerrey km 12")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Cobertura Puertos Perú",
                subtitle = "Firma Smart Launch Services",
                onBackClick = onBack
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Infraestructura Integrada Nacional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy
                    )
                    Text(
                        text = "Contamos con presencia presencial y soporte de radio marítima permanente en los principales muelles terminales del Perú.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                items(details) { port ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = port.name, tint = CoolCelesta)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = port.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Operador de Muelle: ${port.operator}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Flota Disponible: ${port.fleetCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Contacto Radial: ${port.email}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = CoolCelesta
                            )
                            Text(
                                text = "Dirección Oficinas: ${port.address}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            CompactFooter(session)
        }
    }
}

data class PortDetail(
    val name: String,
    val operator: String,
    val fleetCount: String,
    val email: String,
    val address: String
)


// ============================================================================
// SCREEN 11: PLAN COMPARISON (BASIC vs CONTROL vs PREMIUM DYNAMIC MATRICES)
// ============================================================================

@Composable
fun PlanComparisonScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Comparación de Planes",
                subtitle = "Estructura Tarifaria y Niveles",
                onBackClick = onBack
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "Seleccione y Simule un Plan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OceanNavy
                )
                Text(
                    text = "Actualice su nivel de servicio en vivo para explorar características adicionales de manera interactiva.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Simulated level updates buttons
                Card(
                    colors = CardDefaults.cardColors(containerColor = OceanNavy),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Firma contratada actual:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = session.activePlan.planName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Icon(Icons.Default.Verified, contentDescription = "V", tint = MarineAmber)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PricePlan.values().forEach { plan ->
                                val isSelected = session.activePlan == plan
                                Button(
                                    onClick = { viewModel.updateActivePlan(plan) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) CoolCelesta else Color.White.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = plan.planName.split(" ").last(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Aesthetic comparison Matrix cards
                Text(
                    text = "Matriz de Características",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = OceanNavy,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                PlanFeatureMatrixRow("Registro Digital de lanchas", basic = true, control = true, premium = true)
                PlanFeatureMatrixRow("Ver horarios operativos", basic = true, control = true, premium = true)
                PlanFeatureMatrixRow("Notificaciones automáticas", basic = false, control = true, premium = true)
                PlanFeatureMatrixRow("Historial operativo avanzado", basic = false, control = true, premium = true)
                PlanFeatureMatrixRow("Rastreo GPS en tiempo real - Radar", basic = false, control = true, premium = true) // Control has basic, premium has advanced
                PlanFeatureMatrixRow("Dashboard ejecutivo avanzado", basic = false, control = false, premium = true)
                PlanFeatureMatrixRow("KPIs logísticos avanzados", basic = false, control = false, premium = true)
                PlanFeatureMatrixRow("Integraciones de Sistemas (APN/Dizmar)", basic = false, control = false, premium = true)
                PlanFeatureMatrixRow("Soporte prioritario de Radio VHF", basic = false, control = false, premium = true)
            }

            CompactFooter(session)
        }
    }
}

@Composable
fun PlanFeatureMatrixRow(
    feature: String,
    basic: Boolean,
    control: Boolean,
    premium: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.05f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                feature,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = OceanNavy
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Basic status
                FeatureIndicator("Basic", basic)
                // Control status
                FeatureIndicator("Control", control)
                // Premium status
                FeatureIndicator("Premium", premium)
            }
        }
    }
}

@Composable
fun FeatureIndicator(label: String, isAvailable: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = label,
            tint = if (isAvailable) SeaTeal else Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isAvailable) OceanNavy else Color.LightGray,
            fontWeight = if (isAvailable) FontWeight.Medium else FontWeight.Normal
        )
    }
}


// ============================================================================
// SCREEN 12: EXECUTIVE DASHBOARD (FOR PREMIUM CLIENTS EXCLUSIVE)
// ============================================================================

@Composable
fun PremiumDashboardScreen(
    viewModel: SmartLaunchViewModel,
    onBack: () -> Unit,
    onNavigateToPlans: () -> Unit
) {
    val session by viewModel.userSession.collectAsState()
    val requests by viewModel.serviceRequests.collectAsState()

    var showPriorityAlert by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightMarineBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SmartLaunchHeader(
                title = "Dashboard Corporativo VIP",
                subtitle = "Panel Ejecutivo Smart Premium",
                onBackClick = onBack
            )

            // If user's tier is lock (Basic or Control), block and show upgrade banner
            if (session.activePlan != PricePlan.Premium) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MarineAmber.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked Tier",
                            tint = MarineAmber,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Módulo Exclusivo Smart Premium",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "El Dashboard Ejecutivo Avanzado contiene telemetría analítica, integraciones logísticas directas APN-DIZMAR y soporte prioritario VHF de canal exclusivo. Su nivel actual contratado es: ${session.activePlan.planName}.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onNavigateToPlans,
                        colors = ButtonDefaults.buttonColors(containerColor = OceanNavy),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("MEJORAR A SMART PREMIUM", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Full Premium VIP Dashboard Screen
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Header Alert
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MarineAmber.copy(alpha = 0.08f)),
                        border = BorderStroke(1.dp, MarineAmber.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = "VIP Alert", tint = MarineAmber, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Línea Roja Marítima Activada",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanNavy
                                )
                                Text(
                                    "Tiene prioridad de despacho clase A en fondeaderos del Callao y Chancay.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Análisis Operativo Avanzado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OceanNavy,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Fuel Efficiency Curves representation
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Curva de Consumo de Combustible vs Nudos promedio",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val w = size.width
                                    val h = size.height

                                    // Draw background lines
                                    for (i in 1..4) {
                                        drawLine(
                                            color = CoolCelesta.copy(alpha = 0.08f),
                                            start = Offset(0f, h * i / 5),
                                            end = Offset(w, h * i / 5)
                                        )
                                    }

                                    // Draw mock curve path
                                    val points = listOf(
                                        Offset(0f, h * 0.9f),
                                        Offset(w * 0.2f, h * 0.75f),
                                        Offset(w * 0.4f, h * 0.5f),
                                        Offset(w * 0.6f, h * 0.42f),
                                        Offset(w * 0.8f, h * 0.25f),
                                        Offset(w, h * 0.15f)
                                    )
                                    for (idx in 0 until points.size - 1) {
                                        drawLine(
                                            color = CoolCelesta,
                                            start = points[idx],
                                            end = points[idx + 1],
                                            strokeWidth = 3.dp.toPx()
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("5 kts (Mínimo)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text("15 kts (Crucero)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Text("22 kts (Máximo)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }

                    // VIP API Connections State Cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        APIStatusCard(label = "APN Perú API", isOnline = true, modifier = Modifier.weight(1f))
                        APIStatusCard(label = "Dizmar Marina", isOnline = true, modifier = Modifier.weight(1f))
                    }

                    // VIP Active dispatch priorities controller
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Priorización Directa VHF",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = OceanNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Ante demoras de Capitanía, active el canal VIP de Smart Launch para priorizar su lancha ante el puerto.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { showPriorityAlert = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("priority_alert_button")
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = "Alert")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SOLICITAR CANAL EXCLUSIVO VHF S.O.S", fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }

                    if (showPriorityAlert) {
                        AlertDialog(
                            onDismissRequest = { showPriorityAlert = false },
                            title = { Text("Priorización VHF Solicitada", fontWeight = FontWeight.Bold, color = Color.Red) },
                            text = {
                                Text(
                                    "Se ha enviado un token de prioridad APN a las lanchas operativas de Smart Launch en el Callao. Se le asignará escolta de canal marítimo 16.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = { showPriorityAlert = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanNavy)
                                ) {
                                    Text("Entendido")
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CompactFooter(session)
        }
    }
}

@Composable
fun APIStatusCard(label: String, isOnline: Boolean, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, OceanNavy.copy(alpha = 0.05f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isOnline) SeaTeal else Color.Red)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = OceanNavy, fontWeight = FontWeight.Bold)
                Text(if (isOnline) "INTEGRADO OK" else "SIN CONEXIÓN", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 7.sp)
            }
        }
    }
}


// Simple helper to constrain string badges lengths
private fun badgeValueString(count: Int): String {
    return if (count > 99) "99+" else count.toString()
}
