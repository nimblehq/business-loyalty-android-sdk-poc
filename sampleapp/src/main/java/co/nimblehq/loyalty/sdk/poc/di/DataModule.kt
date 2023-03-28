package co.nimblehq.loyalty.sdk.poc.di

import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepository
import co.nimblehq.loyalty.sdk.poc.data.repository.SampleAppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsRepository(
        repository: SampleAppRepositoryImpl
    ): SampleAppRepository
}
