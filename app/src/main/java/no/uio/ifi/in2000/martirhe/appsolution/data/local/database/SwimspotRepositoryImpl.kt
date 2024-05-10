package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot
import javax.inject.Inject


class SwimspotRepositoryImpl @Inject constructor(
    private val swimspotDao: SwimspotDao,
) : SwimspotRepository {

    override suspend fun getAllSwimspots(): Flow<List<Swimspot>> {
        return swimspotDao.getAllSwimspots()
    }

    override suspend fun upsertSwimspot(swimspot: Swimspot) {
        swimspotDao.upsertSwimspot(swimspot)
    }

    override suspend fun getSwimspotById(swimspotId: Int): Swimspot {
        return swimspotDao.getSwimspotById(swimspotId)
    }
    override suspend fun getLastAddedSwimspot(): Swimspot {
        return swimspotDao.getLastAddedSwimspot()
    }

    override suspend fun deleteSwimspot(swimspot: Swimspot) {
        return swimspotDao.deleteSwimspot(swimspot)
    }

    override suspend fun getAllFavorites(): List<Swimspot> {
        return swimspotDao.getAllFavorites()
    }
}