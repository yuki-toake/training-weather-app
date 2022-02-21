package jp.co.greensys.weatherapp.domain.remote_data_source

import java.io.Serializable

// 座標
data class Coord(
    val lat: Double,
    val lon: Double
) : Serializable