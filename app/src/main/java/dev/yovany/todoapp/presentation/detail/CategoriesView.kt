package dev.yovany.todoapp.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.yovany.todoapp.domain.Category
import dev.yovany.todoapp.presentation.detail.CategoryCard
import dev.yovany.todoapp.presentation.detail.domain.CategoryView
import dev.yovany.todoapp.ui.theme.TodoAppTheme

@Composable
fun CategoriesView(
    modifier: Modifier = Modifier,
    categories: List<Category> = Category.entries,
    onCategoriesSelected: (List<Category>) -> Unit = { _ -> }
) {
    val categoriesView = remember {
        mutableStateListOf<CategoryView>().apply {
            addAll(Category.entries.map { category ->
                CategoryView(category = category, isSelected = categories.contains(category))
            })
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        )
        
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Fixed(4),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalItemSpacing = 8.dp,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .semantics { contentDescription = "Category List Menu" }
                .height(200.dp)
                .padding(start = 12.dp, bottom = 12.dp)) {

            items(categoriesView.size) { index ->
                CategoryCard(categoryView = categoriesView[index]) {
                    categoriesView[index] = categoriesView[index].copy(
                        isSelected = !categoriesView[index].isSelected
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .semantics { contentDescription = "Done Categories" }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(48.dp),
            onClick = { onCategoriesSelected(categoriesView.filter { it.isSelected }.map { it.category }) },
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(text = "Done")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CategoriesViewsPreviewLight() {
    TodoAppTheme {
        CategoriesView(categories = listOf(Category.PERSONAL, Category.TRAVEL))
    }
}