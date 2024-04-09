package no.uio.ifi.in2000.martirhe.appsolution.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Swimspot::class],
    version = 1,
    exportSchema = false,
)
abstract class SwimspotsDatabase : RoomDatabase() {
    abstract fun swimspotDao(): SwimspotDao
}