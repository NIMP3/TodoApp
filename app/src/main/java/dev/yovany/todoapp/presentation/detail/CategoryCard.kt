package dev.yovany.todoapp.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.presentation.detail.domain.CategoryView

@Composable
fun CategoryCard(
    categoryView: CategoryView,
    modifier: Modifier = Modifier,
    onCategorySelected: () -> Unit = {}
) {
    val backgroundColor = if(categoryView.isSelected) MaterialTheme.colorScheme.surface
                          else Color(categoryView.category.color)
    val foregroundColor = if(categoryView.isSelected) Color(categoryView.category.color)
                          else MaterialTheme.colorScheme.surfaceContainer

    Card(
        modifier = modifier
            .semantics { contentDescription = categoryView.category.name }
            .wrapContentHeight()
            .clickable { onCategorySelected() },
        shape = RoundedCornerShape(size = 12.dp),
        border = BorderStroke(
            width = if (categoryView.isSelected) 1.dp else 0.dp,
            color = Color(categoryView.category.color)
        ),
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(backgroundColor)
                .padding(8.dp)) {
            Text(text = categoryView.category.name,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = foregroundColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    CategoryCard(categoryView = CategoryView(
        category = Category.WORK,
        isSelected = true
    ), modifier = Modifier.padding(8.dp))
}