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

    @Query("SELECT * FROM Swimspot WHERE Swimspot.id == :swimspotId")
    suspend fun getSwimspotById(swimspotId: Int): Swimspot

    @Query("SELECT * FROM Swimspot ORDER BY Swimspot.id DESC LIMIT 1")
    suspend fun getLastAddedSwimspot(): Swimspot
}