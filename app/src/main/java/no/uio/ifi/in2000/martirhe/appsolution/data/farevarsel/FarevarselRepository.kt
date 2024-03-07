package no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel

import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection


interface FarevarselRepositoryInterface {
    suspend fun getFarevarsler(): FarevarselCollection

}

class FarevarselRepository(
    private val dataSource: FarevarselDataSource = FarevarselDataSource()
) : FarevarselRepositoryInterface {


    override suspend fun getFarevarsler(): FarevarselCollection {
        return dataSource.fetchFarevarsler()
    }
}
