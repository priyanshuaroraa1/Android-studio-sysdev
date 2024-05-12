# Modellering av funksjonalitet i PLASK
Dette dokumentet tar for seg noe av hovedfunksjonaliteten i appen PLASK. Noen av modellene avviker litt i notasjon fra Unified Modeling Language (UML), ettersom vi har valgt å følge oppgavetekstens anbefaling og bruke Mermaid for modellering. Alle avvik fra UML er oppgitt under.


## Funksjonelle krav
Utgangspunktet for denne modelleringen er noen av de viktigste funksjonelle kravene til appen. Disse kravene er utviklet fra de viktigste brukerhistoriene som ble identifisert under tidlig datainnsamling, og har dannet kjernen av funksjonalitet for appen. Disse kravene er ikke sortert etter noen prioritering, men har dannet utgangspunktet for to Use caser som dekker hovedfunksjonaliteten til appen.


### Viktigste funksjonelle krav
- Appen skal vise et kart over badeplasser i Norge.
- Brukeren skal kunne se aktive farevarsler for badeplassene.
- En badeplass sitt farenivå skal representeres med farge i Markøren i kartet
- Brukeren skal kunne se temperatur i vannet, bølgehøyde og -retning ved en badeplass.
- Brukeren skal kunne se været ved badeplassen nå.
- Brukeren skal kunne lagre favorittbadeplassene sine.
- Brukeren skal kunne se bilde av badeplassene
- Det skal være mulig å søke etter badeplasser både på navn, sted og område.

### Use case-diagram
De funksjonelle kravene over har gitt opphav til use case-diagrammet under. Her representerer vi ulike user-stories og hvordan de henger sammen, slik som brukeren opplever appen.

### Use case 
Fra kravene identifisert over, har vi identifisert to Use-caser som dekker flere av hovedfunksjonene i appen. Disse er først beskrevet med en tekstlig beskrivelse.
Diagrammet avviker fra UML ved at noden "User" ikke ser ut som en strekfigur, og at includes- og extends-relasjojnene ikke er notert med "<<" og ">>".

### Aktivitetsdiagram
Deretter er use casene videre modellert i et aktivitetsdiagram som viser hvordan brukeren jobber seg gjennom den aktuelle funksjonaliteten. Dette viser i stor grad hvordan brukeren opplever appen.

Diagrammet avviker fra UML ved at noden User ikke ser ut som en strekfigur, og at includes- og extends-relasjojnene ikke er notert med 

### Sekvensdiagram
Hver av use-casene er videre modellert med et sekvensdiagram, som viser i større detalj hvordan dataflyten foregår.

### Klassediagram

## Use case-diagram
Dette diagrammet viser ulike use caser i appen. 

```mermaid
flowchart LR
%% Nodes

    A{User}
    subgraph System
    B(Legge til og fjerne favorittbadeplass)
    C(Søke etter badeplass)
    F(Finne badeplass i kartet)
    D(Se på favorittbadeplasser)
    E(Sjekke badevettreglene)
    G(Se badetemperatur)
    H(Se værmelding)
    I(Se bilde av badeplass)
    J(Se tilgjengelighetsinformasjon)
    K(Se farevarsler for badeplassene)
    end

%% Edge connections between nodes
    A --> E
    A --> C --> F
    A --> D -->F
    A --> F -- include --> G
    F -- include --> B
    F -- include --> H
    F -- include --> I
    F -- include --> J
    F -- include --> K
    
%% Individual node styling. Try the visual editor toolbar for easier styling!
    style A color:#FFFFFF, stroke:#2962FF, fill:#2962FF
```

# Use-case 1 - Finne badeplass i Drøbak
Dette use-caset bekriver en bruker som er på dagstur til Drøbak, og ønsker å finne den badeplassen i Drøbak som frister mest å bade ved.

**Navn**: Finn badeplass i Drøbak
**Aktør**: Bruker
**Prebetingelser**: Internettilkobling
**Postbetingelser**: Brukeren skal ha bestemt seg for hvor å bade på dagstur i Drøbak.

### Hovedflyt
1. Åpne appen
2. Skrive "Drø" inn i søkefeltet
3. Klikke på "Badeparken i Drøbak"
4. Se på værvarsel og bilde
5. Bestemmer seg for å bade eller å ikke bade ved en badeplass

### Alternativ flyt 1 (Kan repeteres)
4.1 Se på værvarsel og bilde

4.2 Er ikke fornøy og navigerer til en ny badeplass

### Alternativ flyt 2
4.1 Det er et farevarsel for badeplassen

4.2 Klikker på farvarselsymbolet

4.3 Ser at værforholdene kan gjøre det ugunstig å bade.

## Aktivitetsdiagram

```mermaid
flowchart TD
%% Nodes
    A(Start)
    C(Skrive 'Drø' inn i søkefeltet)
    D(Klikke på en badeplass i søkemenyen)
    E(Se på værvarsel og bilde)
    F(Klikke på en annen badeplass i kartet)
    G(Klikke på farevarsel)
    H(Slutt)
    I{Fornøyd?}
    J{Vises\nfarevarsel?}
    K(Velger å bade)
    L(Velger å ikke bade)


%% Edge connections between nodes
    A --> C --> D --> E 
    E --> I -- Ja --> J -- nei --> K
    J -- Ja --> G
    I -- Nei --> F --> E
    G --> K --> H
    G --> L --> H


    style A color:#FFFFFF, stroke:#2962FF, fill:#2962FF
    style H color:#FFFFFF, stroke:#2962FF, fill:#2962FF
```

## Sekvensdiagram

Dette sekvensdiagrammet beskriver samhandlingen mellom Bruker, UI og ViewModel, samt hvordan ViewModel henter data fra Repository og DataSource. I sekvensdiagrammet er det ikke spesifisert både LocationForecast og OceanForecast som Repository og DataSource, ettersom begge disse er implementert relativt likt. Sekvensdiagrammet inkluderer ikke prosesser som skjer ved oppstart av appen, som f.eks. kall på MetAlertRepository og -DataSource, men har som forutsetning at dette allerede er oppdatert i HomeState.

```mermaid
sequenceDiagram
    actor User
    participant UI
    participant ViewModel
    participant Repository
    participant DataSource 

    loop Søke etter, og velge, badeplass
    loop Skrive i søkefeltet
    User->> UI: Skrive et symbol i søkefeltet
    UI  ->> ViewModel: Oppdatere HomeState
    ViewModel->>UI: UI observerer endringer i HomeState
    UI->>User: Tegner Composables på nytt med endringer
    end
    User->>UI: Klikke på en badeplass i søkeforslagene
    UI -) ViewModel: Kalle på onSearchbarSelectSwimspot()
    ViewModel ->> UI: UI observerer endringer i SelectedSwimspot i HomeState
    ViewModel->>Repository: Etterspørre data fra repository
    Repository->>DataSource: Etterspørre data fra DataSource
    DataSource-->>Repository: Returnere LocationForecast-objekt
    Repository-->>ViewModel: Oppdatere LocationForecastUiState
    ViewModel->>UI: UI observerer endringer i LocationForecastUiState
    UI->>User: Tegner Composables på nytt
    end
    alt Farevarsel
    User->>UI: Klikker på farevarsel
    UI->>ViewModel: Oppdaterer HomeState
    ViewModel->>UI: UI observerer endringene i HomeState
    UI->>User: Tegner Composables på nytt
    else

    end
```


## Klassediagram

Dette klassediagrammet beskriver forholdet mellom klassene som brukes til å hente data fra API-ene fra Meterologisk institutt.
- **Hvit pil** peker på interfacet/klassen en klasse arver fra, og er markert med "inheritance".
- **Sort pil** peker på klassen som en variabel er en instanse av. Pilen er markert med hvilken variabel det er snakk om.



```mermaid
classDiagram

    class OceanForecastDataSource{
        -String apiKey
        -HttpClient client
        +fetchOceanForecast(lat, lon)
    }

    class OceanForecastRepositoryInterface{
        <<interface>>
        getOceanForecast(lat, lon)
        getOceanForecastRightNow(oceanForecast, lat, lon)
    }

    class OceanForecastRepository{
        - OceanForecastDataSource dataSource
        getOceanForecast(lat, lon)
        getOceanForecastRightNow(oceanForecast, lat, lon)
    }

    class OceanForecastState{
        <<sealed interface>>
    }
    class Success {
        + OceanForecastRightNow oceanForecastRightNow
    }
    class Loading {
    }
    class Error {
    }

    class HomeViewModel{
        -LocationForecastRepositoryInterface locationForecastRepository,
        -OceanForecastRepositoryInterface oceanForecastRepository,
        -MetAlertRepositoryInterface metAlertRepository,
        -SwimspotRepository swimspotRepository,
        -PreferencesManager preferencesManager,
        ...
        -OceanForecastState oceanForecastState,
    }

    OceanForecastRepository "1" --> "1" OceanForecastDataSource : dataSource
    HomeViewModel "1" --> "1" OceanForecastRepository : oceanForecastRepository
    HomeViewModel "1" --> "1" Success : oceanForecastState

    OceanForecastRepository --|> OceanForecastRepositoryInterface : Inheritance
    Success --|> OceanForecastState : Inheritance
    Loading --|> OceanForecastState : Inheritance
    Error --|> OceanForecastState : Inheritance
```

## Tilstandsdiagram

Følgende tilstandsdiagram viser hvordan tilstanden til OceanForecastUiState endrer seg over tid. 

```mermaid
stateDiagram

state OceanForecastUiState {

    Success --> Loading: When new API-request
    Loading --> Success: When API-call successfull
    Loading --> Error: When API-call unsuccessfull
    Error --> Loading: When new API-request
}
```


# Use-case 2 - Lagre Huk som favoritt
Dette use-caset beskriver en bruker som elsker å bade ved Huk-badeplass, og derfor ønsker å lagre Huk som favoritt.

**Navn**: Lagre Huk som favoritt
**Aktør**: Bruker
**Prebetingelser**: Internettilkobling
**Postbetingelser**: Huk er lagt til i listen over favorittbadeplasser

### Hovedflyt

 1. Åpne appen
 2. Navigere til Huk i kartet
 3. Klikke på *Markøren* til Huk badeplass
 4. Klikke på *stjernen* ved siden av navnet til badeplassen
 5. Klikke på *Favoritter* i navigasjonsmenyen
 6. Ser at Huk er sagret som favoritt

### Alternativ flyt 1
2.1 Skriver "Huk" i søkefeltet
2.2 Klikker på Huk i søkemenyen.

### Alternativ flyt 2
4.1 Huk er allerede markert med stjerne


## Aktivitetsdiagram

```mermaid
flowchart  TD

%% Nodes
A(Start)
C(Navigere til Huk i kartet)
I(Skrive inn 'Huk' i søkefeltet)
J(Trykke på 'Huk' i søkemenyen)
D(Klikke på Markøren til Huk badeplass)
K(Se info om Huk badeplass)
E(Klikke på stjernen ved siden av navnet til badeplassen)
F(Klikke på Favoritter i navigasjonsmenyen)
G(Klikke på farevarsel)
H(Slutt)
L{Søke eller lete?}
M{Er Huk favoritt}

%% Edge connections between nodes
A --> L -- Søke--> C  -->  D  -->  K  
L -- Lete -->  I  -->  J  -->  K --> M
M --Nei-->  E  -->  F  -->  G
M -- Ja -->
G -->  H

style A color:#FFFFFF, stroke:#2962FF, fill:#2962FF
style H color:#FFFFFF, stroke:#2962FF, fill:#2962FF
```

## Sekvensdiagram

På samme måte som tidligere, beskriver dette sekvensdiagrammet samhandlingen mellom Bruker, UI og ViewModel. I tillegg vises hvordan ViewModel henter data fra Repository, DataSource og Database. Implementasjonen av databasen er i dette diagrammet abstrahert bort, men i hovedsak kommuniserer ViewModel med et repository, som igjen kommuniserer med et DataAccessObject (DAO), som kommuniserer med databasen.

```mermaid
sequenceDiagram
    actor User
    participant UI
    participant ViewModel
    participant Repository
    participant DataSource 
    participant Database

    %% Navigere til Huk i kartet
    %% Klikke på Markøren til Huk badeplass
    %% Klikke på stjernen ved siden av navnet til badeplassen
    %% Klikke på Favoritter i navigasjonsmenyen
    %% Ser at Huk er sagret som favoritt

    loop Navigere i kartet
    User->>UI: Navigere i kartet
    UI->>UI: Oppdatere cameraPositionState
    UI->>User: Composables tegnes på nytt
    end

    loop Velge badeplass i kartet
    User->>UI: Klikke på en badeplass-markør i kartet
    UI->>ViewModel: Kalle på onSwimspotPinClick()
    ViewModel->>UI: Oppdaterer SelectedSwimspot i HomeState
    ViewModel->>Repository: Etterspørre data fra repository
    Repository->>DataSource: Etterspørre data fra DataSource
    DataSource->>Repository: Returnere LocationForecast-objekt
    Repository->>ViewModel: Oppdatere LocationForecastUiState
    ViewModel->>UI: UI observerer endringer i LocationForecastUiState
    UI->>User: Tegner Composables på nytt
    end

    User->>UI: Klikke på stjernen ved siden av navnet
    UI->>ViewModel: Kalle på onFavouriteClick()
    ViewModel->>Database: Oppdatere database
    Database->>ViewModel: ViewModelen observerer databasen som Flow
    ViewModel->>UI: Tegne Markør i kartet på nytt

    UI->>UI: Navigere til FavoritesScreen
    UI-->>ViewModel: Opprette instans av FavoritesViewModel (om nødvendig)
    ViewModel->>Database: Etterspørre favoriserte badeplasser
    Database->>ViewModel: Returnere liste av Swimspot-objekter
    ViewModel->>UI: UI observerer endringer i FavoritesState
    UI->>User: Tegner Composables.
```



