package dev.yovany.todoapp.presentation.detail.domain

import dev.yovany.todoapp.domain.Category

data class CategoryView(
    val category: Category,
    var isSelected: Boolean = false
)