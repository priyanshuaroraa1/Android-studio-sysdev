package no.uio.ifi.in2000.martirhe.appsolution.data.farevarsel



import com.google.gson.annotations.SerializedName

// TODO: Do we need @SerializedName-annotation?
// Go through this file and evaluate if we need the @SerializedAnnotation for all variables, or just
// for some.

data class FeatureCollection(
    val features: List<Feature>,
    val lang: String,
    @SerializedName("lastChange") val lastChange: String,
    val type: String
)

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    @SerializedName("`when`") val `when`: TimeInterval
)

data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)

data class Properties(
    val area: String,
    @SerializedName("awarenessResponse") val awarenessResponse: String,
    @SerializedName("awarenessSeriousness") val awarenessSeriousness: String,
    @SerializedName("awareness_level") val awarenessLevel: String,
    @SerializedName("awareness_type") val awarenessType: String,
    val certainty: String,
    val consequences: String,
    val county: List<Any>, // Empty array in provided JSON, the actual type is not defined
    val description: String,
    val event: String,
    @SerializedName("eventAwarenessName") val eventAwarenessName: String,
    @SerializedName("eventEndingTime") val eventEndingTime: String,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val resources: List<Resource>,
    @SerializedName("riskMatrixColor") val riskMatrixColor: String,
    val severity: String,
    val title: String,
    @SerializedName("triggerLevel") val triggerLevel: String?,
    val type: String
)

data class Resource(
    val description: String,
    @SerializedName("mimeType") val mimeType: String,
    val uri: String
)

data class TimeInterval(
    val interval: List<String>
)



