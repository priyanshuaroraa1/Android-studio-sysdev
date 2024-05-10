package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot

@Database(
    entities = [Swimspot::class],
    version = 1,
    exportSchema = false,
)
abstract class SwimspotsDatabase : RoomDatabase() {
    abstract fun swimspotDao(): SwimspotDao
}