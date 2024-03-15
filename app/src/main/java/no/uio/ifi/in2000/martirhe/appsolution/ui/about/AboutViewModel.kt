package no.uio.ifi.in2000.martirhe.appsolution.ui.about

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AboutViewModel: ViewModel() {
    var latitude by mutableDoubleStateOf(50.911491)
    var longitude by mutableDoubleStateOf(12.757933)
    var showForecast by mutableStateOf(false)
    var chosenCity by mutableStateOf("Oslo")

    val aboutTitle = "Om Oss"
}