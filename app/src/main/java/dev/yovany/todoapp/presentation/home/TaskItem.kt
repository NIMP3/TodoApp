package dev.yovany.todoapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    onClickItem: (String) -> Unit = {},
    onDeleteItem: (String) -> Unit = {},
    onToggleCompletion: (Task) -> Unit = {},
    task: Task
){
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClickItem(task.id) }
        ){
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {
                    onToggleCompletion(task)
                }
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp).weight(1f)
            ) {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                if(!task.isCompleted) {
                    task.description?.let {
                        Text(
                            text = it,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Box{
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onDeleteItem(task.id) }
                )
            }

        }

        if(!task.isCompleted) {
            task.category?.let {
                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(Color(task.category.color))
                        .padding(4.dp)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    TodoAppTheme {
        TaskItem(task = Task(
            id = "1",
            title = "I have to do my homework",
            description = "I have to do my homework for the next week",
            category = Category.STUDY,
            isCompleted = false,
        ))
    }
}