package jp.co.greensys.weatherapp.domain.entity

import android.content.Context
import jp.co.greensys.weatherapp.R

/**
 * 予報リストに表示するデータ
 *
 * Created by Toake on 2022/01/21.
 */
data class ForecastItemEntity(
    val date: DateEntity,
    private val maxTemp: Float,
    private val minTemp: Float,
    private val humidity: Int,
    val iconId: String,
    private val pop: Double
) {
    /** パーセント表記のために100倍 */
    fun getPop(): Double =
        pop * 100

    /** 整形された最高気温 */
    fun getMaxTemp(context: Context): String =
        context.getString(R.string.format_temperature_max, maxTemp)

    /** 整形された最高気温 */
    fun getMinTemp(context: Context): String =
        context.getString(R.string.format_temperature_min, minTemp)

    /** 整形された最高気温 */
    fun getHumidity(context: Context): String =
        context.getString(R.string.format_humidity, humidity)
}