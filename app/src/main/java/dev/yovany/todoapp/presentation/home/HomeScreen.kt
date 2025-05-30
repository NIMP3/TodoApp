package dev.yovany.todoapp.presentation.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yovany.todoapp.R
import dev.yovany.todoapp.presentation.home.providers.HomeScreenPreviewProvider
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun HomeScreenRoot(
    viewModel: HomeScreenViewModel,
    navigateToTaskScreen: (String?) -> Unit) {

    val state = viewModel.state.collectAsState()
    val event = viewModel.event

    val context = LocalContext.current

    LaunchedEffect(true) {
        event.collect { event ->
            when(event) {
                HomeScreenEvent.DeletedAllTasks -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.all_tasks_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                HomeScreenEvent.DeletedTask -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.task_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                HomeScreenEvent.UpdatedTasks -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.tasks_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    HomeScreen(
        state = state.value,
        onAction = { action ->
            when (action) {
                HomeScreenAction.OnAddTask -> navigateToTaskScreen(null)
                is HomeScreenAction.OnClickTask -> navigateToTaskScreen(action.taskId)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeDataState,
    onAction: (HomeScreenAction) -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { isMenuExpanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    onAction(HomeScreenAction.OnDeleteAllTasks)
                                    isMenuExpanded = false },
                                text = {
                                    Text(
                                        text = stringResource(R.string.delete_all)
                                    )
                                }
                            )
                        }
                    }
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(HomeScreenAction.OnAddTask) },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->  
        if (state.completedTasks.isNotEmpty() || state.pendingTasks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                item {
                    SummaryInfo(
                        date = state.date,
                        pendingTasks = state.pendingTasks.size,
                        completedTasks = state.completedTasks.size,
                        totalTasks = state.completedTasks.size + state.pendingTasks.size,
                    )
                }

                stickyHeader {
                    SectionTitle(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            ),
                        title = stringResource(R.string.completed_tasks)
                    )
                }

                items(
                    state.completedTasks,
                    key = { task -> task.id }
                ){ task ->
                    TaskItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        task = task,
                        onClickItem = { onAction(HomeScreenAction.OnClickTask(task.id)) },
                        onDeleteItem = { onAction(HomeScreenAction.OnDeleteTask(task)) },
                        onToggleCompletion = { onAction(HomeScreenAction.OnToggleTask(task)) }
                    )
                }

                stickyHeader {
                    SectionTitle(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surface
                            ),
                        title = stringResource(R.string.pending_tasks)
                    )
                }

                items(
                    state.pendingTasks,
                    key = { task -> task.id }
                ){ task ->
                    TaskItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp)),
                        task = task,
                        onClickItem = { onAction(HomeScreenAction.OnClickTask(task.id)) },
                        onDeleteItem = { onAction(HomeScreenAction.OnDeleteTask(task)) },
                        onToggleCompletion = { onAction(HomeScreenAction.OnToggleTask(task)) }
                    )
                }
            }
        }
        else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_tasks),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreviewLight(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TodoAppTheme {
        HomeScreen(
            state = state,
            onAction = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun HomeScreenPreviewDark(
    @PreviewParameter(HomeScreenPreviewProvider::class) state: HomeDataState
) {
    TodoAppTheme {
        HomeScreen(
            state = state,
            onAction = {}
         )
    }
}