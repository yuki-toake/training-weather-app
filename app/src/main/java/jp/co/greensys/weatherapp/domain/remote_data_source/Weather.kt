package jp.co.greensys.weatherapp.domain.remote_data_source

// 気候
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)