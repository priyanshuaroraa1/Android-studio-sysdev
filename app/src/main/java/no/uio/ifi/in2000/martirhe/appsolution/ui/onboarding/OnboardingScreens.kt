package no.uio.ifi.in2000.martirhe.appsolution.ui.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import no.uio.ifi.in2000.martirhe.appsolution.R

// Define the colors as per your specifications
val LightBlue = Color(0xFF7DCCE9)
val DarkBlue = Color(0xFF0E2D4E)
val OffWhite = Color(0xFFF2EDEC)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreens(navController: NavController) {
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = 4

    // Determine the text for the main and sub titles based on the current page
    val (mainTitle, subTitle, bodyText) = when (currentPage) {
        0 -> Triple("Main Title 1", "Subtitle 1", "Body Text 1")
        1 -> Triple("Main Title 2", "Subtitle 2", "Body Text 2")
        2 -> Triple("Main Title 3", "Subtitle 3", "Body Text 3")
        else -> Triple("Main Title 4", "Subtitle 4", "Body Text 4")
    }

    Scaffold(
        containerColor = LightBlue, // Set the background color for the container of the Scaffold
        contentColor = LightBlue,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.plasktekst),
                contentDescription = "Tekst",
                modifier = Modifier
                    .size(250.dp)
            )
            Spacer(modifier = Modifier.height((1).dp))
            Image(
                painter = painterResource(id = R.drawable.plasklogo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            PageIndicator(currentPage = currentPage, totalPages = totalPages)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mainTitle,
                color = DarkBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subTitle,
                color = DarkBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = bodyText,
                color = DarkBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {
        for (index in 0 until totalPages) {
            Box(
                modifier = Modifier
                    .width(if (index == currentPage) 30.dp else 20.dp)
                    .height(5.dp)
                    .background(if (index == currentPage) DarkBlue else OffWhite)
            ) {
                // This box acts as a single indicator
            }
            if (index < totalPages - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
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
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(LightBlue)
    ) {
        // Only show the Skip button if the current page is not the last page
        if (currentPage < totalPages - 1) {
            TextButton(
                onClick = onSkipClicked,
                colors = ButtonDefaults.textButtonColors(contentColor = LightBlue)
            ) {
                Text("Skip", color = DarkBlue)
            }
        } else {
            Spacer(modifier = Modifier.width(74.dp)) // Placeholder spacer to balance the row layout
        }

        Button(
            onClick = onNextClicked,
            colors = ButtonDefaults.buttonColors(Color(0xFF0E2D4E))
        ) {
            Text(
                text = if (currentPage < totalPages - 1) "Neste" else "Ferdig",
                color = Color.White
            )
        }
    }
}


