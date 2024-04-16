package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SwimspotDao {

    @Query("SELECT * FROM Swimspot")
    fun getAllSwimspots(): Flow<List<Swimspot>>

    @Upsert
    suspend fun upsertSwimspot(swimspot: Swimspot)

    @Delete
    suspend fun deleteSwimspot(swimspot: Swimspot)

}