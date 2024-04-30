package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DragHandle(
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    width: Dp = 50.dp,
    height: Dp = 5.dp,
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = width, height = height)
            .background(
                color = color,
                shape = RoundedCornerShape(50)
            )
    )
}