package com.joao01sb.shophub.shared_ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.core.navigation.route

@Composable
fun BottomNavigationBarComp(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Routes.Home),
        BottomNavItem("Search", Icons.Default.Search, Routes.Search),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Routes.Cart),
        BottomNavItem("Orders", Icons.Default.List, Routes.Orders)
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentRoute?.contains(item.route.toString()) == true
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route.route()) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Routes
)