package com.android.guacamole.data.models

import com.android.guacamole.data.dataBase.entity.ProductEntity

data class DetailProduct(
    var idProduct: Int? = -1,
    var nameProduct: String? = "",
    var price: Double? = 0.00,
    var imageProduct: String? = "",
    var idCategory: Int? = null,
    var idStore: Int? = null,
    var quantity: Double? = 0.00
) {
    constructor(productEntity: ProductEntity) : this() {
        idProduct = productEntity.idProduct
        nameProduct = productEntity.nameProduct
        price = productEntity.price
        imageProduct = productEntity.imageProduct
        idCategory = productEntity.idCategory
        idStore = productEntity.idStore
        quantity = 1.00
    }
}
