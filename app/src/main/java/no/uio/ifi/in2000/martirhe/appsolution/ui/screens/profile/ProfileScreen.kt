package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePainter by remember { mutableStateOf<Painter?>(null) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.profile_header),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)

            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
        ) {
            ProfileHeader(imageUri, imagePainter) { pickMedia.launch("image/*") }

            MenuItem(
                buttonText = "Badevettreglene",
                buttonOnClick = {
                    navController.navigate(Routes.WATERSAFETYRULES_SCREEN)

                }
            )

            MenuItem(
                buttonText = "Om oss",
                buttonOnClick = {
                    navController.navigate(Routes.ABOUT_US_SCREEN)
                }
            )

        }
    }
}

@Composable
fun MenuItem(
    buttonText: String,
    buttonOnClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = Modifier
            .clickable(onClick = buttonOnClick)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = buttonText, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Velg $buttonText"
            )
        }
    }
}

@Composable
fun ProfileHeader(imageUri: Uri?, imagePainter: Painter?, onImagePickerClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (imageUri != null && imagePainter != null) {
            Image(
                painter = imagePainter,
                contentDescription = "Profilbilde",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImagePickerClick)
            )
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Default profilbilde",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImagePickerClick)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
