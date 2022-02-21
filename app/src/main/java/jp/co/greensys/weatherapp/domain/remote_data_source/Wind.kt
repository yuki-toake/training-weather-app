package jp.co.greensys.weatherapp.domain.remote_data_source

/**
 * 風
 *
 * Created by Yuki Toake on 2021/12/10.
 */
data class Wind(
    val speed: Float,
    val deg: Int,
    val gust: Float
)