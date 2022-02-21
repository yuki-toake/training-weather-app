package jp.co.greensys.weatherapp.domain.remote_data_source

import jp.co.greensys.weatherapp.domain.entity.DateEntity
import jp.co.greensys.weatherapp.domain.entity.ForecastItemEntity

/**
 * 3時間ごとの天気予報の内容
 *
 * Created by Yuki Toake on 2021/12/10.
 */
data class Forecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    // 降水確率
    val pop: Double,
    // 雨量
    val rain: Rain,
    // 日照比
    val sys: ForecastSys,
    val dtTxt: String
) {
    /** [Forecast]から[ForecastItemEntity]に変換 */
    fun toForecastItem(): ForecastItemEntity {
        return ForecastItemEntity(
            date = DateEntity(dt),
            maxTemp = main.tempMax,
            minTemp = main.tempMin,
            humidity = main.humidity,
            iconId = weather.first().icon,
            pop = pop
        )
    }
}
