package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot

interface SwimspotRepository {
    suspend fun getAllSwimspots(): Flow<List<Swimspot>>
    suspend fun upsertSwimspot(swimspot: Swimspot)
    suspend fun getSwimspotById(swimspotId: Int): Swimspot
    suspend fun getLastAddedSwimspot(): Swimspot
    suspend fun deleteSwimspot(swimspot: Swimspot)
    suspend fun getAllFavorites(): List<Swimspot>
}
