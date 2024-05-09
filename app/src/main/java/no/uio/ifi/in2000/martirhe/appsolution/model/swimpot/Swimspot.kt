package no.uio.ifi.in2000.martirhe.appsolution.model.swimpot

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.SimpleMetAlert
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.WarningIconColor
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.MetAlertUiState

@Entity(tableName = "Swimspot")
data class Swimspot(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val googleId: String?,
    val spotName: String,
    val lat: Double,
    val lon: Double,
    val accessibility: String?,
    val locationstring: String?,
    val original: Boolean?,
    var favourited: Boolean?,
    val url: String?,
) {
    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }

    fun getMarkerIcon(metAlertUiState: MetAlertUiState): BitmapDescriptor {
        val markerIcon: BitmapDescriptor

        when (metAlertUiState) {
            is MetAlertUiState.Success -> {
                val alertColor = SimpleMetAlert.mostSevereColor(
                    metAlertUiState.simpleMetAlertList.filter {
                        it.isRelevantForCoordinate(getLatLng())
                    }
                )
                markerIcon = when (alertColor to favourited) {
                    WarningIconColor.YELLOW to true -> BitmapDescriptorFactory.fromResource(R.drawable.pin_yellow_star_38)
                    WarningIconColor.YELLOW to false -> BitmapDescriptorFactory.fromResource(R.drawable.pin_yellow_38)
                    WarningIconColor.ORANGE to true -> BitmapDescriptorFactory.fromResource(R.drawable.pin_orange_star_38)
                    WarningIconColor.ORANGE to false -> BitmapDescriptorFactory.fromResource(R.drawable.pin_orange_38)
                    WarningIconColor.RED to true -> BitmapDescriptorFactory.fromResource(R.drawable.pin_red_star_38)
                    WarningIconColor.RED to false -> BitmapDescriptorFactory.fromResource(R.drawable.pin_red_38)
                    WarningIconColor.GREEN to true -> BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_star_38)
                    WarningIconColor.GREEN to false -> BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_38)
                    else -> {
                            BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_38)
                        }
                    }

                }

            else -> {markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.pin_blue_38)}
        }
        return markerIcon
    }

    fun getAccecibilityStrings(): List<String> {
        val stringMap = mapOf(
            "wheelchairAccessibleParking" to "HC-parkering",
            "wheelchairAccessibleEntrance" to "Rullestolvennlig inngang",
            "wheelchairAccessibleRestroom" to "HC-toalett"
        )

        // Check if accessibility is not null, split the string by ";", and translate each feature
        return accessibility?.split(";")?.mapNotNull { stringMap[it] } ?: emptyList()
    }

    fun updateFavourite(favourite: Boolean) {
        favourited = favourite
    }

    fun getQuerySearchString(fullString: Boolean = true): String {
        return if (fullString) {
            "$spotName $locationstring"
        } else spotName
    }

}