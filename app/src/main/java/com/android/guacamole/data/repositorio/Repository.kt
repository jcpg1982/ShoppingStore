package com.android.guacamole.data.repositorio

import com.android.guacamole.data.dataBase.DataBase
import com.android.guacamole.data.dataBase.entity.CardInformationEntity
import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity
import com.android.guacamole.data.dataBase.entity.StoreEntity
import com.android.guacamole.data.dataBase.entity.User
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(dataBase: DataBase) : DataSource {

    private val cardInformationDao = dataBase.cardInformationDao
    private val categoryDao = dataBase.categoryDao
    private val productDao = dataBase.productDao
    private val shoppingCartDao = dataBase.shoppingCartDao
    private val shoppingCartDetailDao = dataBase.shoppingCartDetailDao
    private val storeDao = dataBase.storeDao
    private val userDao = dataBase.userDao

    override suspend fun insertCardInformationEntity(cardInformationEntity: CardInformationEntity) =
        flow { emit(cardInformationDao.insert(cardInformationEntity)) }

    override suspend fun insertCategory(listCategory: List<CategoryEntity>) =
        flow { emit(categoryDao.insert(listCategory)) }

    override suspend fun getAllCategory() = flow { emit(categoryDao.getAll()) }

    override suspend fun insertStore(listCategory: List<StoreEntity>) =
        flow { emit(storeDao.insert(listCategory)) }

    override suspend fun getAllStore() = flow { emit(storeDao.getAll()) }

    override suspend fun insertProduct(listCategory: List<ProductEntity>) =
        flow { emit(productDao.insert(listCategory)) }

    override suspend fun getAllProduct() = flow { emit(productDao.getAll()) }

    override suspend fun insertShoppingCartEntity(shoppingCartEntity: ShoppingCartEntity) =
        flow { emit(shoppingCartDao.insert(shoppingCartEntity)) }

    override suspend fun insertListShoppingCartDetailEntity(listShoppingCartDetailEntity: List<ShoppingCartDetailEntity>) =
        flow { emit(shoppingCartDetailDao.insert(listShoppingCartDetailEntity)) }

    override suspend fun resumeShoppingCartDetailEntity(userId: String) =
        flow { emit(shoppingCartDetailDao.resume(userId)) }

    override suspend fun insertUser(entity: User) = flow { emit(userDao.insert(entity)) }

    override suspend fun updateUser(entity: User) = flow { emit(userDao.update(entity)) }

    override suspend fun deleteByIdUser(id: String) = flow { emit(userDao.deleteById(id)) }

    override suspend fun getUserByIdUser(user: String, password: String) =
        flow { emit(userDao.getUserById(user, password)) }
}