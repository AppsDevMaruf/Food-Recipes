package com.maruf.foody.di

import android.content.Context
import androidx.room.Room
import com.maruf.foody.data.database.RecipesDao
import com.maruf.foody.data.database.RecipesDatabase
import com.maruf.foody.utils.Constants.Companion.DATABASE_NAME

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): RecipesDatabase {
        return Room.databaseBuilder(context, RecipesDatabase::class.java, DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase): RecipesDao {
        return database.recipesDao()
    }

}
