package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.enums.Action
import com.android.guacamole.data.repositorio.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(listCategory: List<CategoryEntity>): Flow<Unit> =
        repository.insertCategory(listCategory)

    suspend operator fun invoke(): Flow<List<CategoryEntity>> = repository.getAllCategory()
}