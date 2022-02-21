package jp.co.greensys.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import jp.co.greensys.weatherapp.data.repository.ForecastDataRepository
import jp.co.greensys.weatherapp.data.repository.ForecastDataRepositoryImpl

/**
 * [ForecastDataRepository]を注入するモジュール
 *
 * Created by Toake on 2022/02/03.
 */
@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun provideForecastData(forecastDataRepositoryImpl: ForecastDataRepositoryImpl): ForecastDataRepository =
        forecastDataRepositoryImpl
}