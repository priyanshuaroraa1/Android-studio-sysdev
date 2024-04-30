package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.metalert.WarningIconColor

@Composable
fun WarningIcon(
    warningIconColor: WarningIconColor,
    warningIconDescription: String,
) {
    val imageResource = when (warningIconColor) {
        WarningIconColor.YELLOW -> painterResource(id = R.drawable.warning_yellow)
        WarningIconColor.ORANGE -> painterResource(id = R.drawable.warning_orange)
        WarningIconColor.RED -> painterResource(id = R.drawable.warning_red)
        WarningIconColor.GREEN -> painterResource(id = R.drawable.warning_green)
    }
    Image(
        painter = imageResource,
        contentDescription = warningIconDescription,
        modifier = Modifier.size(24.dp) // Set size or other modifiers as needed
    )
}