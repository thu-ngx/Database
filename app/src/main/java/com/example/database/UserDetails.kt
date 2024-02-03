package com.example.database

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.database.data.AppDatabase
import com.example.database.data.MainViewModel
import com.example.database.ui.theme.DatabaseTheme

@Composable
fun UserDetails(onNavigateToMessagesList: () -> Unit, viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        NavigationBar(onNavigateToMessagesList)
        Spacer(modifier = Modifier.height(80.dp))
        UserInfo(viewModel)
        PhotoPicker()
    }
}


@Composable
fun UserInfo(viewModel: MainViewModel) {
    var userName by remember { mutableStateOf("") }

    val currentUserId = 1

    LaunchedEffect(currentUserId) {
        // Fetch the current user initially
        val currentUser = viewModel.getUserById(currentUserId)
        if (currentUser != null) {
            userName = currentUser.userName ?: ""
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.profile_picture_png_14_44_11_855),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(20.dp))

        TextField(
            value = userName,
            onValueChange = {
                userName = it
            },
            modifier = Modifier.padding(vertical = 30.dp)
        )

        Button(
            onClick = {
                // Update the user's name in the database
                viewModel.updateUserName(userName)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }


}


@Composable
fun NavigationBar(onNavigateToMessagesList: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), contentAlignment = Alignment.CenterStart
    ) {
        BackButton(onNavigateToMessagesList)
    }
}

@Composable
fun BackButton(onNavigateToMessagesList: () -> Unit) {
    Button(
        onClick = onNavigateToMessagesList,
        modifier = Modifier.width(100.dp),
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}

//@Preview
//@Composable
//fun Preview() {
//    DatabaseTheme {
//        UserDetails({})
//    }
//}