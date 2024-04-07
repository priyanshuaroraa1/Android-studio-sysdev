package no.uio.ifi.in2000.martirhe.appsolution.data.local

import kotlinx.coroutines.flow.Flow

interface SwimspotRepository {
    suspend fun getAllSwimspots(): Flow<List<Swimspot>>
}
