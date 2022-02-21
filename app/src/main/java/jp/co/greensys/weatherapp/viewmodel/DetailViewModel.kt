package jp.co.greensys.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import jp.co.greensys.weatherapp.data.repository.ForecastDataRepository
import jp.co.greensys.weatherapp.domain.entity.ForecastDataEntity
import jp.co.greensys.weatherapp.domain.remote_data_source.Coord
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by Toake on 2022/01/20.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val forecastDataRepository: ForecastDataRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    lateinit var uiCallback: UICallback

    // 表示するデータ
    private val _forecastDataEntity: MutableLiveData<ForecastDataEntity> = MutableLiveData()
    val forecastDataEntity: LiveData<ForecastDataEntity>
        get() = _forecastDataEntity

    // インディケーターを表示するか
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * 天気予報の取得
     *
     * 主にコールバックの定義
     * 取得は[getForecastByNameOrCoord]に任せてSingleを返してもらう
     */
    fun getForecastData(
        name: String?,
        coord: Coord?
    ) {
        getForecastByNameOrCoord(name, coord)
            .subscribe({
                // 成功時
                setData(it)
            }, {
                // 失敗時
                // 200番台以外のレスポンス
                if (it is HttpException)
                    uiCallback.showNoResponseDialog()
                else
                    uiCallback.showFailureDialog()
            })
            .addTo(compositeDisposable)
    }

    /** 天気予報の取得 */
    private fun getForecastByNameOrCoord(name: String?, coord: Coord?): Single<ForecastDataEntity> {
        return when {
            name != null ->
                forecastDataRepository.getForecastDataByName(name)
            coord != null ->
                forecastDataRepository.getForecastDataByCoord(coord)
            else ->
                throw IllegalStateException("Both the coord and the name are null.")
        }
    }

    /** データをセット */
    private fun setData(forecastDataEntity: ForecastDataEntity) {
        // インディケーター非表示
        _isLoading.value = false
        // データセット
        _forecastDataEntity.value = forecastDataEntity
    }

    /** 閉じるボタンクリック */
    fun onClickClose() {
        uiCallback.dismiss()
    }

    /** キャンセル処理 */
    fun cancel() {
        compositeDisposable.dispose()
    }

    /** UI更新のコールバック */
    interface UICallback {
        fun showNoResponseDialog()
        fun showFailureDialog()
        fun dismiss()
    }
}