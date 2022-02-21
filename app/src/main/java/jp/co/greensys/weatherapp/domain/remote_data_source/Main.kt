package jp.co.greensys.weatherapp.domain.remote_data_source

/**
 * 温度・湿度・気圧
 *
 * Created by Yuki Toake on 2021/12/10.
 */
data class Main(
    val temp: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
    val pressure: Int,
    val seaLevel: Int,
    val grndLevel: Int,
    val humidity: Int,
    val tempKf: Float
)