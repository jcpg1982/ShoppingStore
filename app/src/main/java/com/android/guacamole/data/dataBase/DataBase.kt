package com.android.guacamole.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.guacamole.data.dataBase.dao.CardInformationDao
import com.android.guacamole.data.dataBase.dao.CategoryDao
import com.android.guacamole.data.dataBase.dao.ProductDao
import com.android.guacamole.data.dataBase.dao.ShoppingCartDao
import com.android.guacamole.data.dataBase.dao.ShoppingCartDetailDao
import com.android.guacamole.data.dataBase.dao.StoreDao
import com.android.guacamole.data.dataBase.dao.UserDao
import com.android.guacamole.data.dataBase.entity.CardInformationEntity
import com.android.guacamole.data.dataBase.entity.CategoryEntity
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity
import com.android.guacamole.data.dataBase.entity.StoreEntity
import com.android.guacamole.data.dataBase.entity.User

@Database(
    entities = [
        CardInformationEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        ShoppingCartDetailEntity::class,
        ShoppingCartEntity::class,
        StoreEntity::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract val cardInformationDao: CardInformationDao
    abstract val categoryDao: CategoryDao
    abstract val productDao: ProductDao
    abstract val shoppingCartDao: ShoppingCartDao
    abstract val shoppingCartDetailDao: ShoppingCartDetailDao
    abstract val storeDao: StoreDao
    abstract val userDao: UserDao
}