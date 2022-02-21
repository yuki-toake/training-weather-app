package jp.co.greensys.weatherapp.utils

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

/**
 * Created by Yuki Toake on 2021/12/15.
 */
class FragmentUtils {
    companion object {
        /** 移動元のFragmentと現在のFragmentのIDが一致しているか */
        fun Fragment.findNavControllerSafety(@IdRes currentId: Int): NavController? {
            return try {
                val controller = NavHostFragment.findNavController(this)
                return if (controller.currentDestination?.id == currentId)
                    controller
                else null
            } catch (e: Exception) {
                Log.e("　Utils", "findNavControllerSafety", e)
                null
            }
        }
    }
}