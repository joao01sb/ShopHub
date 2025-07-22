package com.joao01sb.shophub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joao01sb.shophub.core.navigation.NavigationGraph
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.core.navigation.route
import com.joao01sb.shophub.shared_ui.components.BottomNavigationBarComp
import com.joao01sb.shophub.shared_ui.theme.ShopHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopHubTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val mainRoutes = setOf(
                    Routes.Home.route(),
                    Routes.Search.route(),
                    Routes.Cart.route(),
                    Routes.Orders.route()
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute in mainRoutes) {
                            BottomNavigationBarComp(
                                currentRoute = currentRoute ?: "",
                                onNavigate = { route: String ->
                                    navController.navigate(route) {
                                        popUpTo(Routes.HomeGraph.route()) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}
