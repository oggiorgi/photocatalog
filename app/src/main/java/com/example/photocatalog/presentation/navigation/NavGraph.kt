package com.example.photocatalog.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.photocatalog.di.AppModule
import com.example.photocatalog.presentation.auth.LoginScreen
import com.example.photocatalog.presentation.auth.LoginViewModel
import com.example.photocatalog.presentation.userdetail.UserDetailScreen
import com.example.photocatalog.presentation.userslist.UsersListScreen
import com.example.photocatalog.presentation.userslist.UsersListViewModel
import kotlinx.coroutines.launch

@Composable
fun NavGraph() {
    val context = LocalContext.current
    val appModule = AppModule(context)
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val viewModel: LoginViewModel = viewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("users_list") {
            val viewModel: UsersListViewModel = viewModel()
            UsersListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            "user_detail/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1

            UserDetailScreen(
                navController = navController,
                userId = userId,
                onLogout = {
                    // Очищаем токен и возвращаемся на экран логина
                    lifecycleOwner.lifecycleScope.launch {
                        appModule.logoutUseCase()
                        navController.navigate("login") {
                            popUpTo("users_list") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                getUserDetailUseCase = appModule.getUserDetailUseCase
            )
        }
    }
}