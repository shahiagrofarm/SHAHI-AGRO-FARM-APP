package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.ui.util.AppStrings
import com.example.ui.viewmodel.Screen

@Composable
fun ShahiBottomNav(
    currentScreen: Screen,
    language: AppLanguage,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("bottom_navigation"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        val homeSelected = currentScreen is Screen.Home
        val productsSelected = currentScreen is Screen.Products || currentScreen is Screen.ProductDetail
        val ordersSelected = currentScreen is Screen.Orders || currentScreen is Screen.OrderDetail
        val aboutSelected = currentScreen is Screen.About || currentScreen is Screen.Contact
        val adminSelected = currentScreen is Screen.AdminDashboard

        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

        NavigationBarItem(
            selected = homeSelected,
            onClick = { onTabSelected(Screen.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
            label = { Text(AppStrings.t("nav_home", language), fontSize = 11.sp, fontWeight = if (homeSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = itemColors,
            modifier = Modifier.testTag("nav_tab_home")
        )

        NavigationBarItem(
            selected = productsSelected,
            onClick = { onTabSelected(Screen.Products) },
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Products", modifier = Modifier.size(22.dp)) },
            label = { Text(AppStrings.t("nav_products", language), fontSize = 11.sp, fontWeight = if (productsSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = itemColors,
            modifier = Modifier.testTag("nav_tab_products")
        )

        NavigationBarItem(
            selected = ordersSelected,
            onClick = { onTabSelected(Screen.Orders) },
            icon = { Icon(Icons.Default.LocalShipping, contentDescription = "Orders", modifier = Modifier.size(22.dp)) },
            label = { Text(AppStrings.t("nav_orders", language), fontSize = 11.sp, fontWeight = if (ordersSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = itemColors,
            modifier = Modifier.testTag("nav_tab_orders")
        )

        NavigationBarItem(
            selected = aboutSelected,
            onClick = { onTabSelected(Screen.About) },
            icon = { Icon(Icons.Default.Info, contentDescription = "About", modifier = Modifier.size(22.dp)) },
            label = { Text(AppStrings.t("nav_about", language), fontSize = 11.sp, fontWeight = if (aboutSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = itemColors,
            modifier = Modifier.testTag("nav_tab_about")
        )

        NavigationBarItem(
            selected = adminSelected,
            onClick = { onTabSelected(Screen.AdminDashboard) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Admin", modifier = Modifier.size(22.dp)) },
            label = { Text(AppStrings.t("nav_admin", language), fontSize = 11.sp, fontWeight = if (adminSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = itemColors,
            modifier = Modifier.testTag("nav_tab_admin")
        )
    }
}
