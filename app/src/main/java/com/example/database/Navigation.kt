package com.example.database

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "messageslist"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("messageslist") {
            MessagesListScreen(
                onNavigateToUserDetails = { navController.navigate("userdetails") },
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
                },
            )
        }
    }
}

@Composable
fun MessagesListScreen(
    onNavigateToUserDetails: () -> Unit
) {
    MessagesList(onNavigateToUserDetails)
}

@Composable
fun UserDetailsScreen(
    onNavigateToMessagesList: () -> Unit
) {
    UserDetails(onNavigateToMessagesList)
}