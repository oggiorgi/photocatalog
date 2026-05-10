package com.example.photocatalog.presentation.userslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photocatalog.domain.models.User
import com.example.photocatalog.utils.NetworkResult

@Composable
fun UsersListScreen(
    navController: NavController,
    viewModel: UsersListViewModel = viewModel()
) {
    val usersState by viewModel.usersState.collectAsState()
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (usersState) {
            is NetworkResult.Idle,
            is NetworkResult.Loading -> {
                // Показываем загрузку и для Idle, и для Loading
                CircularProgressIndicator()
            }
            
            is NetworkResult.Success -> {
                UserListContent(
                    users = (usersState as NetworkResult.Success).data,
                    onUserClick = { user ->
                        navController.navigate("user_detail/${user.id}")
                    }
                )
            }
            
            is NetworkResult.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = (usersState as NetworkResult.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadUsers() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun UserListContent(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(user = user, onClick = { onUserClick(user) })
        }
    }
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = user.image,
                contentDescription = "Avatar",
                modifier = Modifier.size(64.dp)
            )
            
            Column {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}