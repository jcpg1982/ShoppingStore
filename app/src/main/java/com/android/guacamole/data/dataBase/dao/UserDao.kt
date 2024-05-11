package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.guacamole.data.dataBase.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: User)

    @Update
    suspend fun update(entity: User)

    @Query("DELETE FROM User WHERE userId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM User WHERE user = :user AND password = :password")
    suspend fun getUserById(user: String, password: String): User?
}