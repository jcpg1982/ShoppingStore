package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StoreEntity(
    @PrimaryKey
    var idStore: Int,
    var nameStore: String? = "",
    var imageStore: String? = ""
)
