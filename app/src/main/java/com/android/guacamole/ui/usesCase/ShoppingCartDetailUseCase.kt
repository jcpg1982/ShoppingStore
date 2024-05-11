package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.repositorio.Repository
import javax.inject.Inject

class ShoppingCartDetailUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(listShoppingCartDetailEntity: List<ShoppingCartDetailEntity>) =
        repository.insertListShoppingCartDetailEntity(listShoppingCartDetailEntity)

    suspend operator fun invoke(userId: String) = repository.resumeShoppingCartDetailEntity(userId)

}