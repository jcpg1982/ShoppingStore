package com.android.guacamole.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase.Callback
import androidx.room.RoomDatabase.JournalMode
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.guacamole.data.dataBase.DataBase
import com.android.guacamole.data.repositorio.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): DataBase =
        Room.databaseBuilder(context, DataBase::class.java, "guacamole.db")
            .addCallback(sRoomDatabaseCallback)
            .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
            .build()

    private val sRoomDatabaseCallback: Callback = object : Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
        }
    }

    @Provides
    @Singleton
    fun providerRepository(dataBase: DataBase) = Repository(dataBase)
}