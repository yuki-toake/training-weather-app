package jp.co.greensys.weatherapp.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ConvertUtils {
    companion object {
        /**
         * タイムスタンプから[pattern]に変換
         *
         * [pattern]がない場合は yyyy年MM月dd日で変換
         */
        fun convertDate(timestamp: Long, pattern: String = "yyyy年MM月dd日"): String {
            val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

            // タイムゾーンを日本に合わせて
            return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.of("Asia/Tokyo"))
                .format(dateTimeFormatter)
        }
    }
}