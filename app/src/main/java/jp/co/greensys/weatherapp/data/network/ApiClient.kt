package jp.co.greensys.weatherapp.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import jp.co.greensys.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

/**
 * Retrofit
 *
 * Created by Yuki Toake on 2021/12/17.
 */
class ApiClient @Inject constructor() {
    // スネークケースをキャメルケースに変換
    private val gsonFactory: GsonConverterFactory =
        GsonConverterFactory.create(
            GsonBuilder().setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
            ).create()
        )

    // API通信のログ
    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }
        ).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonFactory)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    /** DI用にApiServiceの実態化 */
    fun provide(): ApiService = retrofit.create(ApiService::class.java)
}