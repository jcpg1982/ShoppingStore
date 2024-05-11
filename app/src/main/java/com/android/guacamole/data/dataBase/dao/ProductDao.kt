package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.guacamole.data.dataBase.entity.ProductEntity
import com.android.guacamole.data.models.pojo.ProductWithStoreAndCategory

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCategory: List<ProductEntity>)

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAll(): List<ProductWithStoreAndCategory>
}