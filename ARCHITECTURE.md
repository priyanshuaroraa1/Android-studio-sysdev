
# Arkitektur for PLASK-appen
Dette dokumentet gir en oversikt over arkitekturen til PLASK-appen, utviklet av Team 5 som en del av IN2000-prosjektet ved UiO. Denne filen skal være til hjelp for fremtidige utviklere som har behov for å sette seg inn i systemet, enten for videre utvikling eller vedlikehold.

## Arkitektur
PLASK-appen er designet med fokus på høy kohesjon og lav kobling, og benytter Model-View-ViewModel (MVVM) arkitekturen for å sikre en klar separation-of-concerns.

### MVVM-arkitektur
Mappestrukturen i prosjektet vårt er satt opp for å underbygge MVVM-arkitekturen. 

- **Modell**: Representerer dataene og forretningslogikken til appen. Den er ansvarlig for å hente, lagre og administrere applikasjonsdata. Denne delen av arkitekturen er organisert under mappen **data** og **model**. 
- **View**: Gir brukergrensesnittkomponentene som presenterer data for brukeren. Den observerer ViewModel og reagerer på endringene for å presentere de mest oppdaterte dataene. Denne delen er organisert under mappen **ui**.
- **ViewModel**: Fungerer som et bindeledd mellom Modell og View. Den behandler brukerhandlinger, manipulerer datamodellen og opprettholder visningens tilstand. *ViewModel*-filene og tilhørende *state*-filer ligger samlet med sine respektive *screen*-filer under *ui.screens.somescreen*-mappene. Se lenger ned for fullstedig oversikt over filstrukturen.

### Lav kobling og høy kohesjon
- **Lav kobling** har vi oppnådd ved å dele inn forskjellig funksjonalitet i hver sine moduler, noe som gjør vedlikehold lettere, og øker skalerbarheten til appen.
- **Høy kohesjon** er sikret ved å designe komponenter som er fokuserte og har et veldefinert formål, noe som forbedrer påliteligheten og ytelsen til applikasjonen.

### Mappestruktur
Mapper og filer i prosjektet er organisert etter følgende struktur. 
- data
	- local
		- database
	- remote
		- locationforecast
		- oceanforecast
		- metalert
- model
	- locationforecast
	- oceanforecast
	- metalert
	- swimpot
- ui
	- composables
	- navigation
	- screens
	- theme
- util
	- di

**Room-databasen** er implementert under *data.local.database*.

**DataSource** og **Reporitory** filer for å hente data fra de respektive API-ene fra Meterologisk institutt, er implementert under *data.remote*.

**DataClasses** for å representere objektene fra API-ene og Databasen, er organisert under *model*.

**Composable-komponenter** som inngår i en eller flere Screens er organisert under *ui.composables*.

Implementasjon nødvendig for **navigasjon** i appen, er organisert under ui.navigation.

**Screens**, tilghørende **ViewModels** og **State**-klasser er organisert under *ui.screens*.

Implementasjon av appens **farger**, **fonter**, **shapes** og **tema**, ligger under *ui.theme*.

**DependencyInjection** med Hilt, er implementert under *util.di*.


## Dataflyt

### Unidirectional Data Flow

Unidirectional Data Flow (UDF) er et designprinsipp hvor data følger én enkelt enveis flyt. Flyten sørger for økt forutsigbarhet i hvordan data endres over tid. I vår app er det ViewModel-filene som prosesserer input fra UI-laget, utfører business-logikken og henter data fra datakildene. Hvert lag kommuniserer kun med lagene direkte ved siden av seg.

### Praktisk eksempel på dataflyt i PLASK
En brukerhistorie som forekommer ofte i PLASK er at en bruker klikker på en badeplass i kartet for å se værdata for den aktuelle badeplassen.
1. **Brukerinteraksjon**: Brukeren klikker på en badeplass i kartet i brukergrensesnittet. Dette kaller på en onClick-funksjon i HomeViewModel, som tar imot et swimspot-objekt som parameter.
2. **HomeViewModel** sender en dataforespørsel til datalaget for å hente værdata for badeplassen.
3. **Datalaget** sender forespurt værdata tilbake til HomeViewModel.
4. **State** blir oppdatert av HomeViewModel med opdpatert værdata og hvilken badeplass som er valgt.
5. **Brukergrensesnittet** observerer endring i State og oppdateres deretter.

## Nøkkelteknologier
- **Jetpack Compose**: For å bygge brukergrensesnittet, støtter seg på reaktive programmeringsprinsipper for å forenkle utviklingsprosessen.
- **Room Database**: For datalagring, abstraherer bort boilerplate av SQLite og gir en robust mekanisme for databasetilgang.
- **Ktor og Gson**: Brukes for nettverkskommunikasjon og håndtering av JSON-data henholdsvis.
- **Dagger/Hilt**: For avhengighetsinjeksjon, forenkler håndteringen av avhengigheter på tvers av applikasjonen.
- **GoogleMap SDK for Android**: Composable for å fremvise kart fra Google sin Google Maps Platform. Krever API-nøkkel fra et GCP-prosjekt (Google Cloud Platform).

## Beslutning om API-nivå
Vi ønsket å sette minimum API-nivå i appen så lavt som mulig, for at den skulle være tilgjengelig for så mange brukere som mulig. Vi valgte minimum API-level 28, som gjør appen tilgjengelig for 86,4% av alle android-enheter. Vi ønsket å benytte oss av et lavere API-nivå, men opplevde ustabilitet i forbindelse med å tegne inn over 500 Markører i Google Maps-composablen på lavere nivåer. Basert på feilmeldinger, ser dette ut til å være relatert til GPU-ens maximum-texture-size.

## Sikkerhetsaspekter
- **Sikker lagring av API-nøkler**: Bruker Secrets Gradle Plugin for å håndtere API-nøkler sikkert og holde dem utenfor versjonskontroll.
- **Room Database Security**: Bruker compile-time verifikasjon av SQL-spørringer for å forhindre SQL-injeksjonsangrep.
