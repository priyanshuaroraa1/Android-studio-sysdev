package no.uio.ifi.in2000.martirhe.appsolution.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotDao
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotRepositoryImpl
import no.uio.ifi.in2000.martirhe.appsolution.data.local.database.SwimspotsDatabase
import no.uio.ifi.in2000.martirhe.appsolution.util.PreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSwimspotsDatabase(
        @ApplicationContext appContext: Context
    ): SwimspotsDatabase {
        return Room.databaseBuilder(
            appContext, SwimspotsDatabase::class.java, "swimspots_database"
        ).createFromAsset("database/swimspots_database.db").build()
    }

    @Provides
    @Singleton
    fun provideSwimspotDao(swimspotsDatabase: SwimspotsDatabase): SwimspotDao {
        return swimspotsDatabase.swimspotDao()
    }

    @Provides
    @Singleton
    fun provideSwimspotRepository(swimspotDao: SwimspotDao): SwimspotRepository {
        return SwimspotRepositoryImpl(swimspotDao)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesManager(sharedPreferences: SharedPreferences): PreferencesManager {
        return PreferencesManager(sharedPreferences)
    }
}
