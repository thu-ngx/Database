package com.example.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.database.data.MainViewModel
import com.example.database.notification.NotificationViewModel
import com.example.database.views.MessagesList
import com.example.database.views.UserDetails

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "messageslist",
    viewModel: MainViewModel,
    context: Context,
    notificationViewModel: NotificationViewModel
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("messageslist") {
            MessagesListScreen(
                onNavigateToUserDetails = { navController.navigate("userdetails") },
                viewModel,
                context
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
                }, viewModel, context, notificationViewModel
            )
        }
    }
}

@Composable
fun MessagesListScreen(
    onNavigateToUserDetails: () -> Unit,
    viewModel: MainViewModel,
    context: Context,
) {
    MessagesList(onNavigateToUserDetails, viewModel, context)
}

@Composable
fun UserDetailsScreen(
    onNavigateToMessagesList: () -> Unit,
    viewModel: MainViewModel,
    context: Context,
    notificationViewModel: NotificationViewModel
) {
    UserDetails(onNavigateToMessagesList, viewModel, context, notificationViewModel)
}