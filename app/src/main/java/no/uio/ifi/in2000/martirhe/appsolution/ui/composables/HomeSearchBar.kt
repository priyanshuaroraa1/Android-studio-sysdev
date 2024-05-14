package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel
import no.uio.ifi.in2000.martirhe.appsolution.R
import no.uio.ifi.in2000.martirhe.appsolution.model.swimpot.Swimspot
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.SwimspotPinIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    homeViewModel: HomeViewModel = viewModel(),
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,


    ) {
    val homeState = homeViewModel.homeState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current


    Box {
        Box(
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {

            SearchBar(
                query = homeState.searchBarText,
                onQueryChange = onQueryChange,
                onSearch = {
                    if (keyboardController != null) {
                        keyboardController.hide()
                    }
                    onSearch(it)
                },
                active = homeState.searchBarActive,
                onActiveChange = onActiveChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp)),
                placeholder = {
                    Text(text = "Søk etter badeplass")
                },
                leadingIcon = {
                    if (homeState.searchBarActive) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Tilbake knapp",
                            Modifier.clickable {
                                homeViewModel.updateSearchbarActive(false)
                                homeViewModel.updateSearchbarText("")
                            }
                        )
                    } else {
                        SwimspotPinIcon()
                    }
//
                },
                trailingIcon = {
                    if (homeState.searchBarActive && homeState.searchBarText != "") {
                        Icon(
                            modifier = Modifier.clickable {
                                if (homeState.searchBarText.isNotEmpty()) {
                                    homeViewModel.updateSearchbarText("")
                                } else {
                                    homeViewModel.updateSearchbarActive(false)
                                }
                            },
                            imageVector = Icons.Outlined.Cancel,
                            contentDescription = "Avbryt ikon"
                        )
                    }
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),


                ) {

                LazyColumn {
                    if (homeState.searchBarText == "") {
                        val searchHistory = homeViewModel.getSearchHistory()

                        items(searchHistory) {searchString ->
                            SearchHistoryItem(homeViewModel = homeViewModel, searchString = searchString)
                        }
                    } else {
                        val searchBarResult = homeViewModel.getSearchBarResults(homeState.searchBarText)
                        itemsIndexed(searchBarResult) { index, swimspot ->
                            SuggestedSwimspotItem(
                                homeViewModel = homeViewModel,
                                coroutineScope = coroutineScope,
                                swimspot = swimspot,
                                index = index,
                                searchBarText = homeState.searchBarText,
                            )
                        }
                        val numberOfHistoryElements = (10 - searchBarResult.size)
                        val searchHistory = homeViewModel.getSearchHistory().take(numberOfHistoryElements)
                        items(searchHistory) {searchString ->
                            SearchHistoryItem(homeViewModel = homeViewModel, searchString = searchString)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchHistoryItem(
    homeViewModel: HomeViewModel,
    searchString: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_medium),
                vertical = dimensionResource(id = R.dimen.padding_small)
            )
            .clickable {
                homeViewModel.onSearchHistoryClick(searchString)
            }
    ) {
        Icon(imageVector = Icons.Default.History, contentDescription = "Logg ikon")
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
        Text(text = searchString)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.NorthWest, contentDescription = "Velg $searchString")
    }
}

@Composable
fun SuggestedSwimspotItem(
    homeViewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    swimspot: Swimspot,
    index: Int,
    searchBarText: String,
) {
    val selectedColor = if (index == 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(selectedColor)
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_medium),
                vertical = dimensionResource(id = R.dimen.padding_small)
            )
            .clickable {
                homeViewModel.onSearchBarSelectSwimspot(
                    swimspot,
                    coroutineScope
                )
            }
    ) {

        Column {

            val annotatedTextSwimspot = buildAnnotatedString {
                val text = swimspot.spotName
                val regex = Regex(Regex.escape(searchBarText), RegexOption.IGNORE_CASE)
                var lastIndex = 0

                regex.findAll(text).forEach { matchResult ->
                    append(text.substring(lastIndex, matchResult.range.first))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchResult.value)
                    }
                    lastIndex = matchResult.range.last + 1
                }

                append(text.substring(lastIndex, text.length))
            }
            val annotatedTextLocation = buildAnnotatedString {
                val text = swimspot.locationstring ?: ""
                val regex = Regex(Regex.escape(searchBarText), RegexOption.IGNORE_CASE)
                var lastIndex = 0

                regex.findAll(text).forEach { matchResult ->
                    append(text.substring(lastIndex, matchResult.range.first))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(matchResult.value)
                    }
                    lastIndex = matchResult.range.last + 1
                }

                append(text.substring(lastIndex, text.length))
            }

            Text(text = annotatedTextSwimspot)


            Text(
                text = annotatedTextLocation,
                color = Color.Gray
            )
        }
    }
}