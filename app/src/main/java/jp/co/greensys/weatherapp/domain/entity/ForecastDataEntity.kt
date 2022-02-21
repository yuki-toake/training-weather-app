package jp.co.greensys.weatherapp.domain.entity

/**
 * 天気詳細画面に表示するデータ
 *
 * Created by Toake on 2022/01/20.
 */
data class ForecastDataEntity(
    val name: String,
    val dateEntity: DateEntity,
    val list: List<ForecastItemEntity>
)
