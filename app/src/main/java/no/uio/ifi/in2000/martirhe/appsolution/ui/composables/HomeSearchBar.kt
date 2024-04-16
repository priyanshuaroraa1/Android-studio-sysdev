package no.uio.ifi.in2000.martirhe.appsolution.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeSearchBar(
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeState = homeViewModel.homeState.collectAsState().value

    SearchBar(
        query = homeState.searchBarText,
        onQueryChange = { homeViewModel.updateSearchbarText(it) },
        onSearch = { homeViewModel.onSearchBarSearch() },
        active = homeState.searchBarActive,
        onActiveChange = { homeViewModel.updateSearchbarActive(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {
            Text(text = "Finn badeplass")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            ) // TODO: Description?
        },
        trailingIcon = {
            if (homeState.searchBarActive) {
                Icon(
                    modifier = Modifier.clickable {
                        if (homeState.searchBarText.isNotEmpty()) {
                            homeViewModel.updateSearchbarText("")
                        } else {
                            homeViewModel.updateSearchbarActive(false)
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),


    ) {

        if (homeState.searchBarText == "") {
            homeState.searchBarHistory.forEach {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homeViewModel.updateSearchbarText(it)
                            homeViewModel.onSearchBarSearch()
                        }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = "History Icon"
                    )
                    Text(text = it)
                }
            }
        } else {
            homeState.allSwimspots.map { it.spotName.lowercase() }.filter {
                homeState.searchBarText.lowercase() in it
            }.forEach {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homeViewModel.updateSearchbarText(it)
                            homeViewModel.onSearchBarSearch()
                        }
                ) {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Default.Map,
                        contentDescription = "History Icon"
                    )
                    Text(text = it)
                }
            }
        }

    }
}
