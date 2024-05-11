package com.android.guacamole.data.models

import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.StoreEntity

data class DataJson(
    var category: List<CategoryEntity>? = null,
    var store: List<StoreEntity>? = null,
    var products: List<ProductEntity>? = null
)