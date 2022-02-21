package jp.co.greensys.weatherapp.data.network

import io.reactivex.rxjava3.core.Single
import jp.co.greensys.weatherapp.BuildConfig
import jp.co.greensys.weatherapp.domain.remote_data_source.RemoteData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yuki Toake on 2021/12/17.
 */
interface ApiService {
    // 都道府県名から天気予報
    @GET("data/2.5/forecast")
    fun fetchForecastByName(
        @Query("q") cityName: String,
        @Query("appid") appid: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String = "ja",
        // 8つ分の取得
        @Query("cnt") cnt: Int = 8
    ): Single<Response<RemoteData>>

    // 位置情報から天気予報
    @GET("data/2.5/forecast")
    fun fetchForecastByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String = "ja",
        // 8つ分の取得
        @Query("cnt") cnt: Int = 8
    ): Single<Response<RemoteData>>
}