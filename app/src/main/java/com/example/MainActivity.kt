package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.repository.ShahiRepository
import com.example.ui.components.ShahiBottomNav
import com.example.ui.components.ShahiTopBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderDetailScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.ShahiAgroTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShahiViewModel
import com.example.ui.viewmodel.ShahiViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ShahiViewModel by viewModels {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = ShahiRepository(database)
        ShahiViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
            val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
            val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
            val products by viewModel.products.collectAsStateWithLifecycle()
            val orders by viewModel.orders.collectAsStateWithLifecycle()
            val expenses by viewModel.expenses.collectAsStateWithLifecycle()
            val financialMetrics by viewModel.financialMetrics.collectAsStateWithLifecycle()

            // Handle back button for sub-screens
            BackHandler(enabled = currentScreen !is Screen.Home) {
                if (!viewModel.navigateBack()) {
                    viewModel.navigateTo(Screen.Home)
                }
            }

            ShahiAgroTheme(darkTheme = isDarkMode) {
                Scaffold(
                    topBar = {
                        ShahiTopBar(
                            language = language,
                            isDarkMode = isDarkMode,
                            cartItemCount = cartItems.sumOf { it.quantity },
                            canNavigateBack = currentScreen !is Screen.Home,
                            onNavigateBack = {
                                if (!viewModel.navigateBack()) {
                                    viewModel.navigateTo(Screen.Home)
                                }
                            },
                            onCartClick = { viewModel.navigateTo(Screen.Cart) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleTheme = { viewModel.toggleTheme() }
                        )
                    },
                    bottomBar = {
                        // Don't show bottom nav on checkout or order success to focus customer journey
                        if (currentScreen !is Screen.Checkout && currentScreen !is Screen.OrderSuccess) {
                            ShahiBottomNav(
                                currentScreen = currentScreen,
                                language = language,
                                onTabSelected = { screen -> viewModel.navigateTo(screen) }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val screen = currentScreen) {
                            is Screen.Home -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    products = products
                                )
                            }
                            is Screen.Products -> {
                                ProductsScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    products = products,
                                    cartItems = cartItems
                                )
                            }
                            is Screen.ProductDetail -> {
                                ProductDetailScreen(
                                    productId = screen.productId,
                                    viewModel = viewModel,
                                    language = language,
                                    products = products
                                )
                            }
                            is Screen.Cart -> {
                                CartScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    cartItems = cartItems
                                )
                            }
                            is Screen.Checkout -> {
                                CheckoutScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    cartItems = cartItems
                                )
                            }
                            is Screen.OrderSuccess -> {
                                OrderSuccessScreen(
                                    order = screen.order,
                                    viewModel = viewModel,
                                    language = language
                                )
                            }
                            is Screen.Orders -> {
                                OrdersScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    orders = orders
                                )
                            }
                            is Screen.OrderDetail -> {
                                OrderDetailScreen(
                                    orderId = screen.orderId,
                                    viewModel = viewModel,
                                    language = language,
                                    orders = orders
                                )
                            }
                            is Screen.About -> {
                                AboutScreen(
                                    viewModel = viewModel,
                                    language = language
                                )
                            }
                            is Screen.Contact -> {
                                ContactScreen(
                                    language = language
                                )
                            }
                            is Screen.AdminDashboard -> {
                                AdminDashboardScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    orders = orders,
                                    products = products,
                                    expenses = expenses,
                                    metrics = financialMetrics
                                )
                            }
                            is Screen.Settings -> {
                                SettingsScreen(
                                    viewModel = viewModel,
                                    language = language,
                                    isDarkMode = isDarkMode
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
