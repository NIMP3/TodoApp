package dev.yovany.todoapp.presentation.detail

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.R
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.presentation.detail.providers.TaskScreenStatePreviewProvider
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun TaskScreenRoot(
    viewModel: TaskScreenViewModel,
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
) {
    val state = viewModel.state.collectAsState()
    val event = viewModel.event

    val context = LocalContext.current

    LaunchedEffect(true) {
        event.collect { event ->
            when (event) {
                TaskScreenEvent.TaskCreated, TaskScreenEvent.TaskUpdated -> {
                    val messageId = if(event == TaskScreenEvent.TaskCreated) R.string.task_created else R.string.tasks_updated
                    Toast.makeText(
                        context,
                        context.getString(messageId),
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateBack()
                }
            }
        }
    }

    TaskScreen(
        modifier = modifier,
        state = state.value,
        onTaskScreenAction = { action ->
            when (action) {
                is TaskScreenAction.Back -> navigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    state: TaskScreenState,
    onTaskScreenAction: (TaskScreenAction) -> Unit,
) {
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isDescriptionFocused by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.semantics{ contentDescription = "Task Screen" },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.task),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { onTaskScreenAction(TaskScreenAction.Back) }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            Row {
                Text(
                    text = stringResource(R.string.done),
                    modifier = Modifier.padding(8.dp)
                )

                Checkbox(
                    modifier = Modifier.semantics{ contentDescription = "Task Done" },
                    checked = state.isTaskDone,
                    onCheckedChange = { onTaskScreenAction(TaskScreenAction.ChangeTaskDone(it)) },
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = state.category?.toString() ?: stringResource(R.string.category),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(8.dp)
                            .semantics { contentDescription = "Selected Category" }
                    )

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { isCategoryExpanded = true }
                            .semantics { contentDescription = "Select Category" },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select category",
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        DropdownMenu(
                            expanded = isCategoryExpanded,
                            onDismissRequest = { isCategoryExpanded = false },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest
                            )
                                .semantics { contentDescription = "Category Menu" }
                        ) {
                            Column {
                                Category.entries.forEach { category ->
                                    Text(
                                        text = category.toString(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .clickable {
                                                isCategoryExpanded = false
                                                onTaskScreenAction(
                                                    TaskScreenAction.ChangeTaskCategory(category)
                                                )
                                            }
                                            .semantics { contentDescription = category.toString() }
                                    )
                                }
                            }

                        }
                    }
                }
            }

            BasicTextField(
                modifier = Modifier.semantics{ contentDescription = "Task Name" },
                value = state.taskName,
                onValueChange = { onTaskScreenAction(TaskScreenAction.ChangeTaskName(it)) },
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2,
                decorationBox = { innerBox ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (state.taskName.isEmpty()) {
                            Text(
                                modifier = Modifier.semantics{ contentDescription = "Task Name Placeholder" },
                                text = stringResource(R.string.task_name),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            innerBox()
                        }
                    }
                }
            )

            BasicTextField(
                value = state.taskDescription ?: "",
                onValueChange = { onTaskScreenAction(TaskScreenAction.ChangeTaskDescription(it)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSecondary),
                maxLines = if(isDescriptionFocused) 5 else 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {  isDescriptionFocused = it.isFocused }
                    .semantics{ contentDescription = "Task Description" },
                decorationBox = { innerBox ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (state.taskDescription.isNullOrEmpty() && !isDescriptionFocused) {
                            Text(
                                modifier = Modifier.semantics{ contentDescription = "Task Description Placeholder" },
                                text = stringResource(R.string.task_description),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            innerBox()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                enabled = state.canSaveTask,
                onClick = {
                    onTaskScreenAction(TaskScreenAction.SaveTask)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .semantics { contentDescription = "Save Task" },
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskScreenPreviewLight(
    @PreviewParameter(TaskScreenStatePreviewProvider::class) state: TaskScreenState
) {
    TodoAppTheme {
        TaskScreen(state = state, onTaskScreenAction = { })
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TaskScreenPreviewDark(
    @PreviewParameter(TaskScreenStatePreviewProvider::class) state: TaskScreenState
) {
    TodoAppTheme {
        TaskScreen(state = state, onTaskScreenAction = { })
    }
}