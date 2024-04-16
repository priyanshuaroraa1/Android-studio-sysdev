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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.martirhe.appsolution.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(navController: NavController) {

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF7DCCE9),
            secondary = Color(0xFF0E2D4E),
            tertiary = Color(0xFFF2EDEC),
            onPrimary = Color.White,
        )
    ){
        var currentPage by remember { mutableStateOf(0) }
        val totalPages = 4

        val (mainTitle, subTitle, bodyText) = when (currentPage) {
            0 -> Triple(stringResource(id = R.string.pageone_maintitle), stringResource(id = R.string.pageone_subtitle), stringResource(id = R.string.pageone_bodytext))
            1 -> Triple(stringResource(id = R.string.pagetwo_maintitle), stringResource(id = R.string.pagetwo_subtitle), stringResource(id = R.string.pagetwo_bodytext))
            2 -> Triple(stringResource(id = R.string.pagethree_maintitle), stringResource(id = R.string.pagethree_subtitle), stringResource(id = R.string.pagethree_bodytext))
            else -> Triple(stringResource(id = R.string.pagefour_maintitle), stringResource(id = R.string.pagefour_subtitle), stringResource(id = R.string.pagefour_bodytext))
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.tertiary,
            bottomBar = {
                OnboardingBottomBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onSkipClicked = { currentPage = totalPages - 1 },
                    onNextClicked = {
                        if (currentPage < totalPages - 1) {
                            currentPage++
                        } else {
                            navController.navigate("about")
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
            painter = painterResource(id = R.drawable.plasklogo1),
            contentDescription = "Main Illustration",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(mainTitle,
            style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontFamily = FontFamily(Font(R.font.font1)))
        Spacer(modifier = Modifier.height(16.dp).size(36.dp))
        Text(subTitle,
            style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp).size(34.dp))
        Text(bodyText,
            style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp).size(32.dp))
        PageIndicator(currentPage, totalPages)
    }
}

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
                Text(stringResource(id = R.string.onboarding_skip), color = MaterialTheme.colorScheme.secondary)
            }
        }

        Button(
            onClick = onNextClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (currentPage < totalPages - 1) stringResource(id = R.string.onboarding_next) else stringResource(id = R.string.onboarding_done),
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

