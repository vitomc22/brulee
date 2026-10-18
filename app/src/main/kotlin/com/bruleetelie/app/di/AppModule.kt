package com.bruleetelie.app.di

import android.content.Context
import com.bruleetelie.data.BruleeRepository
import com.bruleetelie.data.datastore.OperatingSettingsDataStore
import com.bruleetelie.data.db.BruleeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BruleeDatabase =
        BruleeDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context: Context): OperatingSettingsDataStore =
        OperatingSettingsDataStore(context)

    @Provides
    @Singleton
    fun provideRepository(
        database: BruleeDatabase,
        settings: OperatingSettingsDataStore
    ): BruleeRepository = BruleeRepository(database, settings)
}
