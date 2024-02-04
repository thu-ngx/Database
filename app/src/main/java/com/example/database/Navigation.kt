package com.example.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("messageslist") {
            MessagesListScreen(
                onNavigateToUserDetails = { navController.navigate("userdetails") }, viewModel, context
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
                }, viewModel, context
            )
        }
    }
}

@Composable
fun MessagesListScreen(
    onNavigateToUserDetails: () -> Unit, viewModel: MainViewModel, context: Context
) {
    MessagesList(onNavigateToUserDetails, viewModel, context)
}

@Composable
fun UserDetailsScreen(
    onNavigateToMessagesList: () -> Unit, viewModel: MainViewModel, context: Context
) {
    UserDetails(onNavigateToMessagesList, viewModel, context)
}