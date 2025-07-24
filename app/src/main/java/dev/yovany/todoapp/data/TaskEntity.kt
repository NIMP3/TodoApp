package dev.yovany.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.domain.Task
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val categories: List<Int> = emptyList(),
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean,
    val date: Long
) {
    fun toTask(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            categories = categories.mapNotNull { Category.fromOrdinal(it) },
            isCompleted = isCompleted,
            date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date),
                ZoneId.systemDefault()
            )
        )
    }

    companion object {
        fun fromTask(task: Task): TaskEntity {
            return TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                categories = task.categories.map { it.ordinal },
                isCompleted = task.isCompleted,
                date = task.date
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

            )
        }
    }
}