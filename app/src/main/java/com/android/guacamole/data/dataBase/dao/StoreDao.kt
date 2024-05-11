package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.guacamole.data.dataBase.entity.StoreEntity

@Dao
interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCategory: List<StoreEntity>)

    @Query("SELECT * FROM StoreEntity")
    suspend fun getAll(): List<StoreEntity>
}