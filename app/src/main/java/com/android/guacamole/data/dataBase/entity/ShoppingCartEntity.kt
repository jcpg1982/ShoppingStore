package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.guacamole.data.enums.TypePayment

@Entity
data class ShoppingCartEntity(
    @PrimaryKey
    var idShoppingCart: String,
    var userId: String? = "",
    var dateTime: String? = "",
    var typePayment: TypePayment? = null,
    var amountReceived: Double? = null,
    var amountPaid: Double? = null,
    var idCardInformation: String? = null
)
