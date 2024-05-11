package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory
import com.android.guacamole.data.repositorio.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(listProduct: List<ProductEntity>): Flow<Unit> =
        repository.insertProduct(listProduct)

    suspend operator fun invoke(): Flow<List<ProductWithStoreAndCategory>> = repository.getAllProduct()
}