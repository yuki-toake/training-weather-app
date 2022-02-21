package jp.co.greensys.weatherapp.data.repository

import io.reactivex.rxjava3.core.Single
import jp.co.greensys.weatherapp.domain.entity.ForecastDataEntity
import jp.co.greensys.weatherapp.domain.remote_data_source.Coord

/**
 * Created by Toake on 2022/01/26.
 */
interface ForecastDataRepository {
    fun getForecastDataByName(name: String): Single<ForecastDataEntity>
    fun getForecastDataByCoord(coord: Coord): Single<ForecastDataEntity>
}