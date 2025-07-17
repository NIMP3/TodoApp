package dev.yovany.todoapp.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun CategoriesViews(
    modifier: Modifier = Modifier,
    categories: List<Category> = Category.entries
) {
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(3),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalItemSpacing = 8.dp,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.height(120.dp)) {

        items(categories.size) { index ->
            CategoryView(category = categories[index])
        }
    }
}

@Composable
fun CategoryView(
    category: Category,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(Color(category.color))
                .padding(8.dp)) {
            Text(text = category.name,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.surfaceContainer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesViewsPreviewLight() {
    TodoAppTheme {
        CategoriesViews()
    }
}