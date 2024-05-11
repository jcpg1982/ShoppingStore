package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.android.guacamole.data.dataBase.entity.ShoppingCartEntity

@Dao
interface ShoppingCartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingCartEntity: ShoppingCartEntity)
}