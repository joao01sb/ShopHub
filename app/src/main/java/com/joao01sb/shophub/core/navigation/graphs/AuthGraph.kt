package com.joao01sb.shophub.core.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.joao01sb.shophub.core.navigation.Routes
import com.joao01sb.shophub.features.auth.presentation.event.AuthEvent
import com.joao01sb.shophub.features.auth.presentation.screen.LoginScreen
import com.joao01sb.shophub.features.auth.presentation.screen.RegisterScreen
import com.joao01sb.shophub.features.auth.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.authGraph(
    authViewModel: AuthViewModel
) {

    navigation<Routes.AuthGraph>(
        startDestination = Routes.Login
    ) {
        composable<Routes.Login> {
            val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
            LoginScreen(
                uiState = uiState,
                onValueEmail = {
                    authViewModel.onEvent(AuthEvent.EmailChanged(it))
                },
                onValuePassword = {
                    authViewModel.onEvent(AuthEvent.PasswordChanged(it))
                },
                onClickRegisterNavigation = {
                    authViewModel.onEvent(AuthEvent.SingUp)
                },
                onClickLogin = {
                    authViewModel.onEvent(AuthEvent.Login)
                }
            )
        }
        composable<Routes.Register> {
            val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
            RegisterScreen(
                uiState = uiState,
                onValueName = {
                    authViewModel.onEvent(AuthEvent.NameChanged(it))
                },
                onValueEmail = {
                    authViewModel.onEvent(AuthEvent.EmailChanged(it))
                },
                onValuePassword = {
                    authViewModel.onEvent(AuthEvent.PasswordChanged(it))
                },
                onClickRegister = {
                    authViewModel.onEvent(AuthEvent.Register)
                }
            )
        }
    }
}
