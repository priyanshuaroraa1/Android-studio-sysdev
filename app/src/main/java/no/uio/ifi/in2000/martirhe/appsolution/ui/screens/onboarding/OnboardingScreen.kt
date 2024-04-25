package no.uio.ifi.in2000.martirhe.appsolution.ui.screens.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.ktor.http.ContentType
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.ui.navigation.Routes

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
    ) {
        var currentPage by remember { mutableStateOf(0) }
        val totalPages = 4

        val (imageId, mainTitle, subTitle, bodyText) = when (currentPage) {
            0 -> PageInfo(
                imageId = R.drawable.pageone,
                stringResource(id = R.string.pageone_maintitle),
                stringResource(id = R.string.pageone_subtitle),
                stringResource(id = R.string.pageone_bodytext)
            )

            1 -> PageInfo(
                imageId = R.drawable.pagethree,
                stringResource(id = R.string.pagetwo_maintitle),
                stringResource(id = R.string.pagetwo_subtitle),
                stringResource(id = R.string.pagetwo_bodytext)
            )

            2 -> PageInfo(
                imageId = R.drawable.pagetwo,
                stringResource(id = R.string.pagethree_maintitle),
                stringResource(id = R.string.pagethree_subtitle),
                stringResource(id = R.string.pagethree_bodytext)
            )

            else -> PageInfo(
                imageId = R.drawable.pagefour,
                stringResource(id = R.string.pagefour_maintitle),
                stringResource(id = R.string.pagefour_subtitle),
                stringResource(id = R.string.pagefour_bodytext)
            )
        }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.tertiary,
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.pin),
                        contentDescription = "Plask logo",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(12.dp)
                    )
                }
            },

            bottomBar = {
                OnboardingBottomBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onSkipClicked = { currentPage = totalPages - 1 },
                    onNextClicked = {
                        if (currentPage < totalPages - 1) {
                            currentPage++
                        } else {
                            navController.navigate(Routes.LOCATION_SCREEN)
                        }
                    }
                )
            }
        ) {
            OnboardingContent(imageId, mainTitle, subTitle, bodyText, currentPage, totalPages)
        }
    }
}

@Composable
fun OnboardingContent(
    imageId: Int,
    mainTitle: String,
    subTitle: String,
    bodyText: String,
    currentPage: Int,
    totalPages: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Main Illustration",
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = mainTitle,
            style = TextStyle(
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.font1))
            ),
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .size(34.dp)
        )

        Text(
            subTitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .size(34.dp)
        )

        Text(
            bodyText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .size(32.dp)
        )
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
                        color = if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.2f
                        ),
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
                Text(
                    stringResource(id = R.string.onboarding_skip),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Button(
            onClick = onNextClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (currentPage < totalPages - 1) stringResource(id = R.string.onboarding_next) else stringResource(
                    id = R.string.onboarding_done
                ),
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

