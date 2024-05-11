package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardInformationEntity(
    @PrimaryKey
    var idCardInformation: String,
    var nameUser: String? = null,
    var numberCC: String? = null,
    var dateCC: String? = null,
    var ccvCC: String? = null
)
