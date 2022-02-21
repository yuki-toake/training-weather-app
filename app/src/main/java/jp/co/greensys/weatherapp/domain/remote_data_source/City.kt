package jp.co.greensys.weatherapp.domain.remote_data_source

/**
 * 都市の情報
 *
 * Created by Yuki Toake on 2021/12/10.
 */
data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)