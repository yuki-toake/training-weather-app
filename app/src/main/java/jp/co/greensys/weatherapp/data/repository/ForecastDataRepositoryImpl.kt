package jp.co.greensys.weatherapp.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.co.greensys.weatherapp.data.network.ApiService
import jp.co.greensys.weatherapp.domain.entity.ForecastDataEntity
import jp.co.greensys.weatherapp.domain.remote_data_source.Coord
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by Toake on 2022/01/31.
 */
class ForecastDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ForecastDataRepository {
    override fun getForecastDataByName(name: String): Single<ForecastDataEntity> =
        apiService.fetchForecastByName(name)
            // io用途のスレッド
            .subscribeOn(Schedulers.io())
            // メインスレッド
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.body()?.toForecastDataEntity() ?: throw HttpException(it)
            }

    override fun getForecastDataByCoord(coord: Coord): Single<ForecastDataEntity> =
        apiService.fetchForecastByCoord(
            lat = coord.lat,
            lon = coord.lon
        )
            // io用途のスレッド
            .subscribeOn(Schedulers.io())
            // メインスレッド
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.body()?.toForecastDataEntity() ?: throw HttpException(it)
            }
}