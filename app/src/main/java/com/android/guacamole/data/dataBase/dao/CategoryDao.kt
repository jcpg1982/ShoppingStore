package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.guacamole.data.dataBase.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listCategory: List<CategoryEntity>)

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getAll(): List<CategoryEntity>
}