package no.uio.ifi.in2000.martirhe.appsolution.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.metalert.MetAlertRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.oceanforecast.OceanForecastRepositoryInterface

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLocationForecastRepository(
        locationForecastRepository: LocationForecastRepository
    ): LocationForecastRepositoryInterface

    @Binds
    abstract fun bindOceanForecastRepository(
        oceanForecastRepository: OceanForecastRepository
    ): OceanForecastRepositoryInterface

    @Binds
    abstract fun bindMetAlertRepository(
        metAlertRepository: MetAlertRepository
    ): MetAlertRepositoryInterface

}