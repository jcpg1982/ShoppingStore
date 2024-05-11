package com.android.guacamole.data.dataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.guacamole.data.models.DetailProduct

@Entity
data class ShoppingCartDetailEntity(
    @PrimaryKey var idShoppingCartDetail: String,
    var idShoppingCart: String? = null,
    var nameProduct: String? = "",
    var price: Double? = 0.00,
    var imageProduct: String? = "",
    var idCategory: Int? = null,
    var idStore: Int? = null,
    var quantity: Double? = 0.00
) {
    constructor(
        idShoppingCartDetail: String, idShoppingCart: String, detailProduct: DetailProduct
    ) : this(idShoppingCartDetail) {
        this.idShoppingCart = idShoppingCart
        nameProduct = detailProduct.nameProduct
        price = detailProduct.price
        imageProduct = detailProduct.imageProduct
        idCategory = detailProduct.idCategory
        idStore = detailProduct.idStore
        quantity = detailProduct.quantity
    }
}