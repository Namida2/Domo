package com.example.waiterCore.domain.menu

import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem

data class CategoriesNameHolder(
    var categories: List<CategoryName>
) : BaseRecyclerViewItem