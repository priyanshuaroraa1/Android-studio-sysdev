package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot

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

    @Query("SELECT * FROM Swimspot WHERE Swimspot.favourited == 1")
    suspend fun getAllFavorites(): List<Swimspot>
}