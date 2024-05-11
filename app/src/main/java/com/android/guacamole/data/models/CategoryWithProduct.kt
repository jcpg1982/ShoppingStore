package com.android.guacamole.data.models

import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory

data class CategoryWithProduct(
    var category: CategoryEntity? = null,
    var listProducts: List<ProductWithStoreAndCategory>? = null
)