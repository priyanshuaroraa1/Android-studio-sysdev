package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.ktor.utils.io.errors.IOException


@Composable
fun loadMapStyleFromAssets(): String {
    val context = LocalContext.current
    return try {
        context.assets.open("map_style_light.json").bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}