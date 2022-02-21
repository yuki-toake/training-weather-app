package jp.co.greensys.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.greensys.weatherapp.data.network.ApiClient
import jp.co.greensys.weatherapp.data.network.ApiService
import javax.inject.Singleton

/**
 * [ApiService]を注入するモジュール
 *
 * Created by Toake on 2022/02/03.
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiClientModule {
    @Provides
    @Singleton
    fun provideApiService(apiClient: ApiClient): ApiService = apiClient.provide()
}