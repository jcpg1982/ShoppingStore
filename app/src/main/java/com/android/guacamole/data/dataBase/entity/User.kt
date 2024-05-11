package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var userId: String = "",
    var name: String? = "",
    var lastName: String? = "",
    var user: String? = "",
    var password: String? = ""
)
