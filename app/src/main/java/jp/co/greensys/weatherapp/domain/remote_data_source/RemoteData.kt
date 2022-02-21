package jp.co.greensys.weatherapp.domain.remote_data_source

import jp.co.greensys.weatherapp.domain.entity.DateEntity
import jp.co.greensys.weatherapp.domain.entity.ForecastDataEntity

/**
 * 3時間ごとの天気予報
 *
 * Created by Yuki Toake on 2021/12/10.
 */
data class RemoteData(
    val cod: String,
    val message: String,
    val cnt: Int,
    val city: City,
    val list: List<Forecast>
) {
    fun toForecastDataEntity(): ForecastDataEntity =
        ForecastDataEntity(
            name = city.name,
            dateEntity = DateEntity(list.first().dt),
            list = list.map { it.toForecastItem() }
        )
}