package no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel

import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.FarevarselCollection
import no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler.SimpleMetAlert


interface FarevarselRepositoryInterface {
    suspend fun getFarevarsler(): FarevarselCollection
    suspend fun getSimpleMetAlertsFromCoord(): List<SimpleMetAlert>

}

class FarevarselRepository(
    private val dataSource: FarevarselDataSource = FarevarselDataSource()
) : FarevarselRepositoryInterface {


    override suspend fun getFarevarsler(): FarevarselCollection {
        return dataSource.fetchFarevarsler()
    }

    override suspend fun getSimpleMetAlertsFromCoord(): List<SimpleMetAlert> {

        val farevarselCollection = getFarevarsler()
        val simpleMetAlertList = mutableListOf<SimpleMetAlert>()
        var multiPolygon: List<List<List<List<Float>>>>

        farevarselCollection.features.forEach{

            try {
                multiPolygon = if (it.geometry.type == "Polygon") {
                    it.geometry.coordinates as List<List<List<List<Float>>>>
                } else {
                    listOf(it.geometry.coordinates as List<List<List<Float>>>)
                }
                val area: String = it.properties.area
                val awarenessLevel: String = it.properties.awarenessLevel
                val awarenessType: String = it.properties.awarenessType
                val consequences: String = it.properties.consequences
                val description: String = it.properties.description
                simpleMetAlertList.add(SimpleMetAlert(
                    multiPolygon = multiPolygon,
                    area = area,
                    awarenessLevel = awarenessLevel,
                    awarenessType = awarenessType,
                    consequences = consequences,
                    description = description))
            } catch (_: Exception) {  } // TODO: Føler dette er en billig løsning
        }

        return simpleMetAlertList
    }
}
