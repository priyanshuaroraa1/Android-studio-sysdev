# PLASK
Velkommen til GitHub-repositoriet for Plask, en Android-applikasjon designet av meg og 4 andre studenter for å hjelpe brukere med å oppdage og utforske ulike badeplasser. 

# Dokumentasjon
All dokumentasjon kan finnes i dette GitHub-repositoriet og i rapporten som er levert som en del av dette prosjektet.

Prosjektets arkitektur er beskrevet i [ARCHITECTURE.md](ARCHITECTURE.md), samt at store deler av prosjektets funksjonalitet er modellert i [MODELING.md](MODELING.md).

Alle branch-navn kan slåes opp i [product_backlog.csv](product_backlog.csv) for en fullstendig oversikt.

# Hvordan kjøre appen
git clone: https://github.com/priyanshuaroraa1/IN2000_V24.git
1. Åpne prosjektet i Android Studio.
2. Bygg og kjør appen på en emulator eller fysisk enhet.

# Funksjoner
Interaktivt kart: Kartfunksjonalitet som viser lokasjoner av badeplasser med relevant vær- og vanninformasjon.
Detaljerte værvarsler: Tilgang til oppdaterte værvarsler for badeplasser i Norge.
Favorittsystem: Mulighet for brukere til å lagre sine favorittbadeplasser for rask tilgang.
Informasjon om badeplasser: Viser informasjon om badeplasser, inkludert temperatur og bølgeforhold. 

# Teknologier
Kotlin og Jetpack Compose for moderne og effektiv Android-utvikling.
MVVM (Model-Viewmodel-View) arkitektur for skille utviklingen av et grafisk brukergrensesnitt
Dagger-Hilt for avhengighetsinjeksjon.
Room for lokal databasestyring.
Ktor for nettverkskall til eksterne APIer.
Flow og Coroutines for asynkron datahåndtering og UI-oppdateringer.

# Biblioteker

## AndroidX Libraries
- `androidx.navigation:navigation-compose`
- `androidx.core:core-ktx`
- `androidx.lifecycle:lifecycle-runtime-ktx`
- `androidx.lifecycle:lifecycle-runtime-compose`
- `androidx.compose.runtime:runtime-livedata`
- `androidx.activity:activity-compose`
- `androidx.compose.ui:ui`, `ui-graphics`, `ui-tooling-preview`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.compose.material3:material3-android`
- `androidx.test:monitor`, `ext:junit`, `espresso:espresso-core`
- `androidx.compose.material:material-icons-extended-android`
- `androidx.appcompat:appcompat`
- `androidx.hilt:hilt-navigation-compose`
- `androidx.datastore:datastore-preferences`
- `androidx.room:room-runtime`, `room-ktx`, `room-testing`
- `androidx.core:core-splashscreen`

## Google Libraries and Utilities
- `com.google.android.gms:play-services-location`
- `com.google.accompanist:accompanist-permissions`
- `com.google.android.gms:play-services-maps`
- `com.google.maps.android:maps-compose`
- `com.google.maps.android:android-maps-utils`
- `com.google.maps.android:maps-ktx`
- `com.google.maps.android:maps-utils-ktx`
- `com.google.dagger:hilt-android`, `hilt-android-compiler`, `hilt-compiler`

## Kotlin Libraries
- `org.jetbrains.kotlinx:kotlinx-coroutines-core`
- `org.jetbrains.kotlinx:kotlinx-coroutines-android`
- `org.jetbrains.kotlinx:kotlinx-coroutines-play-services`
- `org.jetbrains.kotlinx:kotlinx-coroutines-test`

## Ktor Libraries
- `io.ktor:ktor-client-core`
- `io.ktor:ktor-client-cio`
- `io.ktor:ktor-client-android`
- `io.ktor:ktor-client-content-negotiation`
- `io.ktor:ktor-serialization`, `ktor-serialization-gson`, `ktor-serialization-kotlinx-json`
- `io.ktor:ktor-client-mock`

## Image Loading
- `io.coil-kt:coil-compose`

## Testing Frameworks
- `junit:junit`
- `org.mockito:mockito-core`

# Kontakt
For spørsmål eller innspill, vennligst ikke nøl med å kontakte vårt utviklingsteam på priyanshu768@gmail.com - Din tilbakemelding er verdifull for oss, og vi står klare til å bistå deg.
