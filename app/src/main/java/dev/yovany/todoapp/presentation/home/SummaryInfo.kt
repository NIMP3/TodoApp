package dev.yovany.todoapp.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.R
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun SummaryInfo(
    modifier: Modifier = Modifier,
    date: String,
    pendingTasks: Int,
    completedTasks: Int,
    totalTasks: Int,
) {
    val angleRatio = remember {
        Animatable(0f)
    }

    LaunchedEffect(completedTasks, totalTasks) {
        if (totalTasks == 0) return@LaunchedEffect
         angleRatio.animateTo(
             targetValue = completedTasks/totalTasks.toFloat(),
             animationSpec =  tween(
                    durationMillis = 400
             )
         )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .weight(1.3f)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.task_summary_info, pendingTasks, completedTasks),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
                .aspectRatio(1f)
        ) {
            val baseColor = MaterialTheme.colorScheme.inversePrimary
            val progressColor = MaterialTheme.colorScheme.primary
            val strokeWidth = 12.dp

            Canvas(
                modifier = Modifier.aspectRatio(1f)
            ) {
                drawArc(
                    color = baseColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = size,
                    style = Stroke(
                        width = strokeWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )

                if(completedTasks <= totalTasks) {
                    drawArc(
                        color = progressColor,
                        startAngle = -90f,
                        sweepAngle = (360f * angleRatio.value),
                        useCenter = false,
                        size = size,
                        style = Stroke(
                            width = strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            Text(
                text = "${(completedTasks/totalTasks.toFloat()).times(100).toInt()}%",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryInfoPreviewLight() {
    TodoAppTheme {
        SummaryInfo(
            date = "May 21, 2025",
            pendingTasks = 3,
            completedTasks = 2,
            totalTasks = 5
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SummaryInfoPreviewDark() {
    TodoAppTheme(darkTheme = true) {
        SummaryInfo(
            date = "May 21, 2025",
            pendingTasks = 3,
            completedTasks = 2,
            totalTasks = 5
        )
    }
}