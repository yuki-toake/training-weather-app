package jp.co.greensys.weatherapp.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import jp.co.greensys.weatherapp.R

/**
 * app:iconIdでiconのidを受け取ってGlideで取得＆表示
 *
 * Created by Toake on 2022/01/21.
 */
object ImageLoader {
    @JvmStatic
    @BindingAdapter("iconId")
    fun ImageView.loadIcon(id: String?) {
        Glide.with(context)
            .load(context.getString(R.string.request_weather_icon, id))
            .into(this)
    }
}