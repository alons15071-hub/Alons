package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.SmartLaunchViewModel
import com.example.ui.theme.OceanNavy
import kotlinx.coroutines.launch

@Composable
fun SmartLaunchAppContainer(viewModel: SmartLaunchViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val session by viewModel.userSession.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val unreadNotifCount = notifications.count { !it.isRead }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Screen selection routing
    when (currentScreen) {
        Screen.Splash -> {
            WelcomeScreen(onStartClick = {
                viewModel.navigateTo(Screen.Login)
            })
        }
        Screen.Login -> {
            LoginScreen(onLoginSuccess = { ruc, user, pass ->
                viewModel.loginCorporate(ruc, user, pass)
            })
        }
        else -> {
            // Dashboard drawers wrappers
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        SidebarHeader(session = session)

                        Divider()

                        // Scrollable drawer items
                        SidebarNavigationItem(
                            icon = Icons.Default.Home,
                            label = "Muro de Control",
                            isSelected = currentScreen == Screen.MainMenu,
                            onClick = {
                                viewModel.navigateTo(Screen.MainMenu)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.AddModerator,
                            label = "Solicitar Lancha",
                            isSelected = currentScreen == Screen.ServiceRequestForm,
                            onClick = {
                                viewModel.navigateTo(Screen.ServiceRequestForm)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Radar,
                            label = "Rastreo GPS Lanchas",
                            isSelected = currentScreen == Screen.ServiceTracking,
                            onClick = {
                                viewModel.navigateTo(Screen.ServiceTracking)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.History,
                            label = "Bitácora / Historial",
                            isSelected = currentScreen == Screen.ServiceHistory,
                            onClick = {
                                viewModel.navigateTo(Screen.ServiceHistory)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.FolderOpen,
                            label = "Archivo Digital APN",
                            isSelected = currentScreen == Screen.DocumentManagement,
                            onClick = {
                                viewModel.navigateTo(Screen.DocumentManagement)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Notifications,
                            label = "Notificaciones y Avisos",
                            isSelected = currentScreen == Screen.NotificationCenter,
                            onClick = {
                                viewModel.navigateTo(Screen.NotificationCenter)
                                coroutineScope.launch { drawerState.close() }
                            },
                            badgeValue = unreadNotifCount
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Assessment,
                            label = "Estadísticas / KPIs",
                            isSelected = currentScreen == Screen.OperationalReports,
                            onClick = {
                                viewModel.navigateTo(Screen.OperationalReports)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Place,
                            label = "Puertos del Perú",
                            isSelected = currentScreen == Screen.CompanyProfile,
                            onClick = {
                                viewModel.navigateTo(Screen.CompanyProfile)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Compare,
                            label = "Planes de Servicio",
                            isSelected = currentScreen == Screen.PlanComparison,
                            onClick = {
                                viewModel.navigateTo(Screen.PlanComparison)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        SidebarNavigationItem(
                            icon = Icons.Default.Analytics,
                            label = "Dashboard VIP Premium",
                            isSelected = currentScreen == Screen.PremiumDashboard,
                            onClick = {
                                viewModel.navigateTo(Screen.PremiumDashboard)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        SidebarNavigationItem(
                            icon = Icons.Default.ExitToApp,
                            label = "Cerrar Sucursal",
                            isSelected = false,
                            onClick = {
                                viewModel.logout()
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                // Rendering actual sub-view page based on routing state
                // This incorporates floating button or swipe layout options to easily slide out the menu drawer!
                Box {
                    when (currentScreen) {
                        Screen.MainMenu -> {
                            MainMenuScreen(
                                viewModel = viewModel,
                                onMenuSelected = { target -> viewModel.navigateTo(target) }
                            )
                        }
                        Screen.ServiceRequestForm -> {
                            ServiceRequestFormScreen(
                                viewModel = viewModel,
                                onFormSubmitted = { viewModel.navigateTo(Screen.ServiceHistory) },
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.ServiceTracking -> {
                            ServiceTrackingScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.ServiceHistory -> {
                            ServiceHistoryScreen(
                                viewModel = viewModel,
                                onTrackRequest = { req -> viewModel.selectRequestForTracking(req) },
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.DocumentManagement -> {
                            DocumentManagementScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.NotificationCenter -> {
                            NotificationCenterScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.OperationalReports -> {
                            OperationalReportsScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.CompanyProfile -> {
                            CompanyProfileScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.PlanComparison -> {
                            PlanComparisonScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() }
                            )
                        }
                        Screen.PremiumDashboard -> {
                            PremiumDashboardScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateBack() },
                                onNavigateToPlans = { viewModel.navigateTo(Screen.PlanComparison) }
                            )
                        }
                        else -> {
                            MainMenuScreen(
                                viewModel = viewModel,
                                onMenuSelected = { target -> viewModel.navigateTo(target) }
                            )
                        }
                    }

                    // Floating action menu drawer toggler so users don't get lost, visible only for logged-in modules!
                    ExtendedFloatingActionButton(
                        onClick = {
                            coroutineScope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        },
                        icon = { Icon(Icons.Default.Menu, contentDescription = "Menú Principal") },
                        text = { Text("MENÚ", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        containerColor = OceanNavy,
                        contentColor = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 64.dp)
                    )
                }
            }
        }
    }
}
