package no.uio.ifi.in2000.martirhe.appsolution.data.local.database

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
    val favourited: Boolean?,
) {
    fun getLatLng(): LatLng {
        return LatLng(lat, lon)
    }

    fun getMarkerIcon(metAlertUiState: MetAlertUiState): BitmapDescriptor {
        var markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.pin_normal_38)

        when (metAlertUiState) {
            is MetAlertUiState.Success -> {
                val alertColor = SimpleMetAlert.mostSevereColor(
                    metAlertUiState.simpleMetAlertList.filter {
                        it.isRelevantForCoordinate(getLatLng())
                    }
                )
                markerIcon = when (alertColor) {
                    WarningIconColor.YELLOW -> BitmapDescriptorFactory.fromResource(R.drawable.pin_yellow_38)
                    WarningIconColor.ORANGE -> BitmapDescriptorFactory.fromResource(R.drawable.pin_orange_38)
                    WarningIconColor.RED -> BitmapDescriptorFactory.fromResource(R.drawable.pin_red_38)
                    else -> {
                        BitmapDescriptorFactory.fromResource(R.drawable.pin_normal_38)
                    }                    }

                }

            else -> {markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.pin_normal_38)}
        }
        return markerIcon
    }

}