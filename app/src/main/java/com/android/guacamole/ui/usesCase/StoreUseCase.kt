package com.android.guacamole.ui.usesCase

import com.android.guacamole.data.dataBase.entity.StoreEntity
import com.android.guacamole.data.repositorio.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(listStore: List<StoreEntity>): Flow<Unit> =
        repository.insertStore(listStore)

    suspend operator fun invoke(): Flow<List<StoreEntity>> = repository.getAllStore()
}