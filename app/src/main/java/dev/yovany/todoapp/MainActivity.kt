package dev.yovany.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dev.yovany.todoapp.navigation.NavigationRoot
import dev.yovany.todoapp.presentation.detail.TaskScreenRoot
import dev.yovany.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                val navHostController = rememberNavController()
                NavigationRoot(navHostController)
            }
        }
    }
}
