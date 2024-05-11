package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity
import com.android.guacamole.data.repositorio.Repository
import javax.inject.Inject

class ShoppingCartUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(shoppingCartEntity: ShoppingCartEntity) =
        repository.insertShoppingCartEntity(shoppingCartEntity)

}