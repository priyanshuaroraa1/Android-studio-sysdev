package no.uio.ifi.in2000.martirhe.appsolution.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeSearchBar(
    homeViewModel: HomeViewModel = viewModel()
) {


    SearchBar(
        query = homeViewModel.searchBarText,
        onQueryChange = { homeViewModel.searchBarText = it },
        onSearch = { homeViewModel.onSearch() },
        active = homeViewModel.searchBarActive,
        onActiveChange = { homeViewModel.searchBarActive = it },
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
            if (homeViewModel.searchBarActive) {
                Icon(
                    modifier = Modifier.clickable {
                        if (homeViewModel.searchBarText.isNotEmpty()) {
                            homeViewModel.searchBarText = ""
                        } else {
                            homeViewModel.searchBarActive = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Icon"
                )
            }

        },

        ) {

        if (homeViewModel.searchBarText == "") {
            homeViewModel.searchBarHistory.forEach {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homeViewModel.searchBarText = it
                            homeViewModel.onSearch()
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
            homeViewModel.badeplasserDummy.map { it.navn.lowercase()}.filter {
                homeViewModel.searchBarText.lowercase() in it
            }.forEach {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            homeViewModel.searchBarText = it
                            homeViewModel.onSearch()
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


// Her er den store TODO-lista
// TODO: Lagre tidligere søk i en lokal eller ekstern database