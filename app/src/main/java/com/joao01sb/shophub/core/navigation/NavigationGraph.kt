package com.joao01sb.shophub.core.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.joao01sb.shophub.core.navigation.graphs.authGraph
import com.joao01sb.shophub.core.navigation.graphs.cartGraph
import com.joao01sb.shophub.core.navigation.graphs.homeGraph
import com.joao01sb.shophub.core.navigation.graphs.ordersGraph
import com.joao01sb.shophub.features.auth.presentation.event.AuthUiEvent
import com.joao01sb.shophub.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val startDestination = if (isAuthenticated) {
        Routes.HomeGraph
    } else {
        Routes.AuthGraph
    }

    LaunchedEffect(Unit) {
        authViewModel.uiEvent.collect { event ->
            when (event) {
                AuthUiEvent.NavigateToHome -> {
                    Toast.makeText(navController.context, "Logged in", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.HomeGraph) {
                        popUpTo(Routes.AuthGraph) { inclusive = true }
                    }
                }
                AuthUiEvent.NavigateToLogin -> {
                    Toast.makeText(navController.context, "Success in creat user", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.AuthGraph) {
                            inclusive = false
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                AuthUiEvent.NavigateToRegister -> {
                    navController.navigate(Routes.Register) {
                        popUpTo(Routes.AuthGraph) {
                            inclusive = false
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authGraph(authViewModel)
        homeGraph(navController)
        cartGraph(navController)
        ordersGraph(navController)
    }
}