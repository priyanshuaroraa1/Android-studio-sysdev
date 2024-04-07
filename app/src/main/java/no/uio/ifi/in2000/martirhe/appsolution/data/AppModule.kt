package no.uio.ifi.in2000.martirhe.appsolution.data

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.martirhe.appsolution.data.local.SwimspotDao
import no.uio.ifi.in2000.martirhe.appsolution.data.local.SwimspotRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.local.SwimspotRepositoryImpl
import no.uio.ifi.in2000.martirhe.appsolution.data.local.SwimspotsDatabase
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
}
