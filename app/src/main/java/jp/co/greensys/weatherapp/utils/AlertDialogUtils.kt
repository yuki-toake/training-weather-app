package jp.co.greensys.weatherapp.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import jp.co.greensys.weatherapp.R

/**
 * Created by Yuki Toake on 2021/12/09.
 */
class AlertDialogUtils {
    companion object {
        /**
         * アラートダイアログ
         *
         * アプリテーマを設定し忘れるため統一化
         * ここまでするならDialogFragmentを使ったほうがいい気がする
         */
        fun alertDialog(
            context: Context,
            title: String,
            message: String,
            positiveMessage: String? = null,
            positive: DialogInterface.OnClickListener? = null,
            negativeMessage: String? = null,
            negative: DialogInterface.OnClickListener? = null
        ): MaterialAlertDialogBuilder =
            MaterialAlertDialogBuilder(context, R.style.Theme_WeatherApp_Dialog).apply {
                setTitle(title)
                setMessage(message)

                // ポジティブボタン
                if (positiveMessage != null || positive != null)
                    setPositiveButton(positiveMessage, positive)

                // ネガティブボタン
                if (negativeMessage != null || negative != null)
                    setNegativeButton(negativeMessage, negative)
            }
    }
}