package no.uio.ifi.in2000.martirhe.appsolution.ui.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.martirhe.appsolution.R

private val ThemeColors = lightColorScheme(
    primary = Color(0xFF7DCCE9),
    onPrimary = Color.White,
    secondary = Color(0xFF0E2D4E),
    onSecondary = Color.White,
    surface = Color(0xFFF2EDEC),
    onSurface = Color(0xFF0E2D4E)
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(navController: NavController) {
    MaterialTheme(colorScheme = ThemeColors) {
        var currentPage by remember { mutableStateOf(0) }
        val totalPages = 4

        val (mainTitle, subTitle, bodyText) = when (currentPage) {
            0 -> Triple("Velkommen til Plask", "Ditt vindu til Norges badesteder", "Oppdag og utforsk de beste badestedene rundt om i landet.")
            1 -> Triple("Utforsk Vannkvalitet", "Informasjon ved fingertuppene", "Få oppdatert informasjon om vannkvalitet og værforhold ved dine favorittbadesteder.")
            2 -> Triple("Favorittsteder", "Lagre og Organiser", "Marker dine favorittbadesteder for rask tilgang og personlig oppdateringer.")
            else -> Triple("Finn Ditt Perfekte Badested", "Søk og Oppdag", "Bruk vårt søkeverktøy for å finne badesteder basert på dine preferanser, fra vannkvalitet til værforhold.")
        }


        Scaffold(
            containerColor = ThemeColors.primary,
            contentColor = ThemeColors.onPrimary,
            bottomBar = {
                OnboardingBottomBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onSkipClicked = { currentPage = totalPages - 1 },
                    onNextClicked = {
                        if (currentPage < totalPages - 1) {
                            currentPage++
                        } else {
                            navController.navigate("next_route")
                        }
                    }
                )
            }
        ) {
            OnboardingContent(mainTitle, subTitle, bodyText, currentPage, totalPages)
        }
    }
}

@Composable
fun OnboardingContent(mainTitle: String, subTitle: String, bodyText: String, currentPage: Int, totalPages: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.plasktekst),
            contentDescription = "Main Illustration",
            modifier = Modifier.size(250.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.plasklogo),
            contentDescription = "Main Illustration",
            modifier = Modifier.size(180.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(mainTitle, style = MaterialTheme.typography.headlineMedium, color = ThemeColors.secondary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(subTitle, style = MaterialTheme.typography.bodyLarge, color = ThemeColors.secondary)
        Spacer(modifier = Modifier.height(16.dp))
        Text(bodyText, style = MaterialTheme.typography.bodySmall, color = ThemeColors.secondary)
        Spacer(modifier = Modifier.height(32.dp))
        PageIndicator(currentPage, totalPages)
    }
}

// PageIndicator and OnboardingBottomBar remain largely unchanged, except for the application of the new color scheme.


@Composable
fun PageIndicator(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        for (index in 0 until totalPages) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = if (index == currentPage) 20.dp else 12.dp, height = 8.dp)
                    .background(
                        color = if (index == currentPage) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}


@Composable
fun OnboardingBottomBar(
    currentPage: Int,
    totalPages: Int,
    onSkipClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentPage < totalPages - 1) {
            TextButton(
                onClick = onSkipClicked,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Skip", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Button(
            onClick = onNextClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (currentPage < totalPages - 1) "Next" else "Done",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    val navController = rememberNavController()
    OnboardingScreen(navController = navController)
}

