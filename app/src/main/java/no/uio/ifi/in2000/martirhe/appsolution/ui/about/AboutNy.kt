package no.uio.ifi.in2000.martirhe.appsolution.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Attribution
import androidx.compose.material.icons.filled.Copyright
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Filter5
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.martirhe.appsolution.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(navController: NavController) {

    val isCardExpanded = remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7DCCE9),
            secondary = Color(0xFF0E2D4E),
            tertiary = Color(0xFFF2EDEC),
            onPrimary = Color.White,
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.about_screen_label),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.tertiary)
                )
            },
            containerColor = MaterialTheme.colorScheme.tertiary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(0.dp)
                    .padding(horizontal = 16.dp)
                    //.background(Color.White)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plasklogo1),
                        contentDescription = "App Logo",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit,
                    )
                }

                Text(
                    stringResource(id = R.string.about_screen_title),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 25.sp,
                        lineHeight = 28.sp,
                        fontFamily = FontFamily(Font(R.font.font1)),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(25.dp))

                Text(
                    stringResource(id = R.string.about_screen_info),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Start,
                    lineHeight = 23.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Filter5, contentDescription = "Info Icon", tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Our Team",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Row(modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .align(Alignment.CenterHorizontally)
                        ) {
                            Ansatte("Priyanshu", R.drawable.priyanshu)
                            Spacer(modifier = Modifier.width(8.dp))
                            Ansatte("Vetle", R.drawable.vetle)
                            Spacer(modifier = Modifier.width(8.dp))
                            Ansatte("Bernd", R.drawable.bernd)
                            Spacer(modifier = Modifier.width(8.dp))
                            Ansatte("Martine", R.drawable.martine)
                            Spacer(modifier = Modifier.width(8.dp))
                            Ansatte("Sindre", R.drawable.sindre)
                        }

                        Spacer(Modifier.height(16.dp))

                        Text(
                            stringResource(id = R.string.about_screen_team),
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                lineHeight = 25.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {

                                Text(
                                    stringResource(id = R.string.about_screen_teaminfo),
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        lineHeight = 23.sp,
                                    ),
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Icon(Icons.Filled.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("info@plask.no", color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row {
                                Icon(Icons.Filled.Phone, contentDescription = "Phone", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("123 456 7890", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Icon(Icons.Filled.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("info@plask.no", color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Icon(Icons.Filled.Phone, contentDescription = "Phone", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("123 456 7890", color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { /* API-knapp handling */ },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("API-knapp", fontSize = 10.sp)
                }

                Spacer(Modifier.height(16.dp))

            }
        }
    }
}

@Composable
fun Ansatte(name: String, drawableId: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Ansatte Image",
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        )
        Text(name, color = MaterialTheme.colorScheme.primary)
    }
}

