package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey
    var idProduct: Int,
    var nameProduct: String? = "",
    var price: Double? = 0.00,
    var imageProduct: String? = "",
    var idCategory: Int? = null,
    var idStore: Int? = null
)
