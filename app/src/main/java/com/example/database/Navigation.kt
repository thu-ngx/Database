package com.example.database

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.database.data.MainViewModel

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "messageslist",
    viewModel: MainViewModel
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("messageslist") {
            MessagesListScreen(
                onNavigateToUserDetails = { navController.navigate("userdetails") }, viewModel
            )
        }
        composable("userdetails") {
            UserDetailsScreen(
                onNavigateToMessagesList = {
                    navController.navigate("messageslist") {
                        popUpTo("messageslist") {
                            inclusive = true
                        }
                    }
                }, viewModel
            )
        }
    }
}

@Composable
fun MessagesListScreen(
    onNavigateToUserDetails: () -> Unit, viewModel: MainViewModel
) {
    MessagesList(onNavigateToUserDetails, viewModel)
}

@Composable
fun UserDetailsScreen(
    onNavigateToMessagesList: () -> Unit, viewModel: MainViewModel
) {
    UserDetails(onNavigateToMessagesList, viewModel)
}