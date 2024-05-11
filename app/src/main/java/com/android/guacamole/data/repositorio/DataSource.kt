package com.android.guacamole.data.repositorio

import com.android.guacamole.data.dataBase.entity.CardInformationEntity
import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity
import com.android.guacamole.data.dataBase.entity.StoreEntity
import com.android.guacamole.data.dataBase.entity.User
import com.android.guacamole.data.models.SalesReportWithProducts
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory
import kotlinx.coroutines.flow.Flow

interface DataSource {
    //card information
    suspend fun insertCardInformationEntity(cardInformationEntity: CardInformationEntity): Flow<Unit>

    //category
    suspend fun insertCategory(listCategory: List<CategoryEntity>): Flow<Unit>
    suspend fun getAllCategory(): Flow<List<CategoryEntity>>

    //product
    suspend fun insertProduct(listCategory: List<ProductEntity>): Flow<Unit>
    suspend fun getAllProduct(): Flow<List<ProductWithStoreAndCategory>>

    //shopping cart
    suspend fun insertShoppingCartEntity(shoppingCartEntity: ShoppingCartEntity): Flow<Unit>

    //shopping cart detail
    suspend fun insertListShoppingCartDetailEntity(listShoppingCartDetailEntity: List<ShoppingCartDetailEntity>): Flow<Unit>
    suspend fun resumeShoppingCartDetailEntity(userId: String): Flow<SalesReportWithProducts>

    //store
    suspend fun insertStore(listCategory: List<StoreEntity>): Flow<Unit>
    suspend fun getAllStore(): Flow<List<StoreEntity>>

    //User
    suspend fun insertUser(entity: User): Flow<Unit>
    suspend fun updateUser(entity: User): Flow<Unit>
    suspend fun deleteByIdUser(id: String): Flow<Unit>
    suspend fun getUserByIdUser(user: String, password: String): Flow<User?>
}