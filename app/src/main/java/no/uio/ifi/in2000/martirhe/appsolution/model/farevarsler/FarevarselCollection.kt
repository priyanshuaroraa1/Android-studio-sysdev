package no.uio.ifi.in2000.martirhe.appsolution.model.farevarsler


import com.google.gson.annotations.SerializedName


data class FarevarselCollection (
    val features: List<Feature>,        // ok
    val lang: String,                   // ok
    val lastChange: String,             // ok
    val type: String                    // ok
)

data class Feature (
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    @SerializedName("when") val featureWhen: TimeInterval,
)

data class TimeInterval (
    val interval: List<String>
)

data class Geometry (
    val coordinates: Any, // Denne kan være String, List<String>, ... , List<List<List<List<String>>>>
    val type: String
)


data class Properties(
    @SerializedName("altitude_above_sea_level") val altitudeAboveSeaLevel: Int?,
    val area: String,
    val awarenessResponse: String?,
    val awarenessSeriousness: String?,
    @SerializedName("awareness_level") val awarenessLevel: String,
    @SerializedName("awareness_type") val awarenessType: String,
    val ceiling_above_sea_level: Int,
    val certainty: String,
    val consequences: String,
    val contact: String,
    val county: List<String>,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val eventEndingTime: String? = null,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val municipality: List<String>,
    val resources: List<Resource>,
    val riskMatrixColor: String,
    val severity: String,
    val status: String,
    val title: String,
    val type: String,
    val web: String,
    val triggerLevel: String? = null,
    val municipalityID: String? = null, // TODO: Fjerne denne?
    val administrativeID: String? = null, //
    val incidentName: String? = null
)

data class Resource(
    val description: String,
    val mimeType: String,
    val uri: String
)




