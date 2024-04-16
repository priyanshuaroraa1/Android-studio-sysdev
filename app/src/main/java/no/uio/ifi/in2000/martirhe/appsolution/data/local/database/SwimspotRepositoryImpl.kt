package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SwimspotRepositoryImpl @Inject constructor(
    private val swimspotDao: SwimspotDao,
) : SwimspotRepository {

    override suspend fun getAllSwimspots(): Flow<List<Swimspot>> {
        return swimspotDao.getAllSwimspots()
    }
}