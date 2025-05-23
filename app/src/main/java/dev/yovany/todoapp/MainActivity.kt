package dev.yovany.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.text.input.TextFieldState
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Category.STUDY
import dev.yovany.todoapp.presentation.detail.TaskScreen
import dev.yovany.todoapp.presentation.detail.TaskScreenRoot
import dev.yovany.todoapp.presentation.detail.TaskScreenState
import dev.yovany.todoapp.presentation.home.HomeScreenRoot
import dev.yovany.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                TaskScreenRoot()
            }
        }
    }
}
