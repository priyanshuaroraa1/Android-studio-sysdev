package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.martirhe.appsolution.R

@Composable
fun getFromDirectionPainterResource(windDirection: String): Painter {
    val resourceId = when (windDirection) {
        "north" -> R.drawable.north
        "north_northeast" -> R.drawable.north_northeast
        "northeast" -> R.drawable.northeast
        "east_northeast" -> R.drawable.east_northeast
        "east" -> R.drawable.east
        "east_southeast" -> R.drawable.east_southeast
        "southeast" -> R.drawable.southeast
        "south_southeast" -> R.drawable.south_southeast
        "south" -> R.drawable.south
        "south_southwest" -> R.drawable.south_southwest
        "southwest" -> R.drawable.southwest
        "west_southwest" -> R.drawable.west_southwest
        "west" -> R.drawable.west
        "west_northwest" -> R.drawable.west_northwest
        "northwest" -> R.drawable.northwest
        "north_northwest" -> R.drawable.north_northwest
        else -> R.drawable.north
    }
    return painterResource(id = resourceId)
}