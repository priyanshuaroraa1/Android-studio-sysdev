package no.uio.ifi.in2000.martirhe.appsolution.util.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.locationforecast.LocationForecastRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.metalert.MetAlertRepositoryInterface
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastRepository
import no.uio.ifi.in2000.martirhe.appsolution.data.remote.oceanforecast.OceanForecastRepositoryInterface

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