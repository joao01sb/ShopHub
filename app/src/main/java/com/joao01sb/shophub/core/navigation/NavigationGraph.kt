package com.joao01sb.shophub.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.joao01sb.shophub.core.navigation.graphs.authGraph
import com.joao01sb.shophub.features.auth.presentation.event.AuthUiEvent
import com.joao01sb.shophub.features.auth.presentation.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
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
                AuthUiEvent.NavigateToHome ->{
                    navController.navigate(Routes.HomeGraph) {
                        popUpTo(Routes.AuthGraph) { inclusive = true }
                    }
                }
                AuthUiEvent.NavigateToLogin ->  navController.navigate(Routes.Login)
                AuthUiEvent.NavigateToRegister -> navController.navigate(Routes.Register)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(authViewModel)
    }
}