package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import kotlinx.coroutines.flow.Flow

interface SwimspotRepository {
    suspend fun getAllSwimspots(): Flow<List<Swimspot>>
    suspend fun upsertSwimspot(swimspot: Swimspot)
    suspend fun getSwimspotById(swimspotId: Int): Swimspot
    suspend fun getLastAddedSwimspot(): Swimspot
    suspend fun deleteSwimspot(swimspot: Swimspot)
}
