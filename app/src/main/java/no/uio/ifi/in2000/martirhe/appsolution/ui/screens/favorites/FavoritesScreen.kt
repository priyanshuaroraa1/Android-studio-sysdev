import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import no.uio.ifi.in2000.martirhe.appsolution.ui.screens.home.HomeViewModel


@Composable
fun FavoritesScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Text(text = "Testing")
//    val favoritesState = homeViewModel.favoritesState.collectAsState().value

//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                favoritesViewModel.loadFavorites()
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    LazyColumn {
//        items(favoritesState.favoritedSwimspots) {
//            Text(text = it.spotName)
//        }
//    }


//
//    val homeState = homeViewModel.homeState.collectAsState().value
//
//    val lifecycleOwner = LocalLifecycleOwner.current
//
//    DisposableEffect(lifecycleOwner) {
//        val observer = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_RESUME) {
//                homeViewModel.loadSwimspotsTest()
//            }
//        }
//        lifecycleOwner.lifecycle.addObserver(observer)
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    LazyColumn {
//        items(
//            homeState.allSwimspots.filter { swimspot ->
//                swimspot.favourited == true
//            }
//        ) { swimspot ->
//            Text(text = swimspot.spotName)
//        }
//    }
}