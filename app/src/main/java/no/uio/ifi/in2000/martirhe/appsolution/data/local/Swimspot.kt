package no.uio.ifi.in2000.martirhe.appsolution.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

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
}