package dev.yovany.todoapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.yovany.todoapp.presentation.detail.TaskScreenRoot
import dev.yovany.todoapp.presentation.detail.TaskScreenViewModel
import dev.yovany.todoapp.presentation.home.HomeScreenRoot
import dev.yovany.todoapp.presentation.home.HomeScreenViewModel
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeScreenDestination
        ){
            composable<HomeScreenDestination> {
                val homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
                HomeScreenRoot(
                    viewModel = homeScreenViewModel,
                    navigateToTaskScreen = {
                        navController.navigate(TaskScreenDestination(it))
                    },
                )
            }

            composable<TaskScreenDestination> {
                val taskScreenViewModel: TaskScreenViewModel = hiltViewModel()

                TaskScreenRoot(
                    viewModel = taskScreenViewModel,
                    navigateBack = {
                        navController.navigateUp()
                    },
                )
            }
        }
    }
}

@Serializable
object HomeScreenDestination

@Serializable
data class TaskScreenDestination(val taskId: String? = null)