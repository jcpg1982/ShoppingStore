package com.android.guacamole.data.models.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.StoreEntity

data class ProductWithStoreAndCategory(
    @Embedded val product: ProductEntity, @Relation(
        parentColumn = "idStore", entityColumn = "idStore"
    ) val store: StoreEntity, @Relation(
        parentColumn = "idCategory", entityColumn = "idCategory"
    ) val category: CategoryEntity
)
