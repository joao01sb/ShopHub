package com.joao01sb.shophub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joao01sb.shophub.core.navigation.NavigationGraph
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.core.navigation.route
import com.joao01sb.shophub.features.auth.presentation.viewmodel.AuthViewModel
import com.joao01sb.shophub.shared_ui.components.BottomNavigationBarComp
import com.joao01sb.shophub.shared_ui.theme.ShopHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Routes.Home.route(),
        Routes.Search.route(),
        Routes.Cart.route(),
        Routes.Orders.route()
    )

    val shouldShowBottomNav = isAuthenticated &&
            currentRoute in bottomNavRoutes

    val isOnHomeRoute = currentRoute == Routes.Home.route()
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val backPressedTimeout = 2000L

    BackHandler(enabled = isOnHomeRoute) {
        val currentTime = System.currentTimeMillis()

        if (currentTime - backPressedTime < backPressedTimeout) {
            (context as? ComponentActivity)?.finishAffinity()
        } else {
            backPressedTime = currentTime
            Toast.makeText(
                context,
                "Press again to exit",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationBarComp(
                    currentRoute = currentRoute.orEmpty(),
                    onNavigate = { route ->
                        navigateToBottomNavDestination(navController, route, currentRoute)
                    }
                )
            }
        }
    ) { paddingValues ->
        NavigationGraph(
            navController = navController,
            authViewModel = authViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

private fun navigateToBottomNavDestination(
    navController: NavHostController,
    targetRoute: String,
    currentRoute: String?
) {
    if (currentRoute == targetRoute) return

    navController.navigate(targetRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}