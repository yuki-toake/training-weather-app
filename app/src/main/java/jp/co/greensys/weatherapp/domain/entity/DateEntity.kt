package jp.co.greensys.weatherapp.domain.entity

import jp.co.greensys.weatherapp.utils.ConvertUtils

/**
 * Created by Toake on 2022/01/27.
 */
data class DateEntity(val date: Long) {
    /** yyyy年MM月dd日 */
    fun getFullDateString(): String = ConvertUtils.convertDate(date)

    /** 日付の取得 */
    fun getDateString(): String = ConvertUtils.convertDate(date, "d日")

    /** 時間の取得 */
    fun getTimeString(): String = ConvertUtils.convertDate(date, "HH:mm")

    /**
     * 翌日かどうか
     *
     * HH == "00" なら翌日という安易な実装
     * 00:00, 3:00, 6:00...のきっかり3時間でしか取得しないため
     */
    fun isTomorrow(): Boolean = ConvertUtils.convertDate(date, "HH") == "00"
}
