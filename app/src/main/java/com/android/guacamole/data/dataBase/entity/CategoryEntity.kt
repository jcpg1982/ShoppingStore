package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    var idCategory: Int,
    var nameCategory: String? = ""
)
