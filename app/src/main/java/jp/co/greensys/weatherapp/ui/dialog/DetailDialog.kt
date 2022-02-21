package jp.co.greensys.weatherapp.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import jp.co.greensys.weatherapp.R
import jp.co.greensys.weatherapp.databinding.DialogDetailBinding
import jp.co.greensys.weatherapp.domain.entity.ForecastItemEntity
import jp.co.greensys.weatherapp.ui.adapter.ForecastListAdapter
import jp.co.greensys.weatherapp.utils.AlertDialogUtils
import jp.co.greensys.weatherapp.utils.ConvertUtils
import jp.co.greensys.weatherapp.viewmodel.DetailViewModel

/** 天気の詳細画面 */
@AndroidEntryPoint
class DetailDialog : BottomSheetDialogFragment() {
    private val viewModel: DetailViewModel by viewModels()
    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailDialogArgs by navArgs()

    // UI更新のコールバック
    private val uiCallback: DetailViewModel.UICallback =
        object : DetailViewModel.UICallback {
            override fun showNoResponseDialog() {
                noResponseDialog.show()
            }

            override fun showFailureDialog() {
                failureDialog.show()
            }

            override fun dismiss() {
                dismissDialog()
            }
        }

    // レスポンスがなかった
    private val noResponseDialog by lazy {
        AlertDialogUtils.alertDialog(
            requireContext(),
            getString(R.string.error_connecting_title),
            getString(R.string.error_connecting_message),
            getString(R.string.ok),
            { _, _ -> dismiss() }
        ).setOnCancelListener { dismiss() }
    }

    // 解析に失敗した
    private val failureDialog by lazy {
        AlertDialogUtils.alertDialog(
            requireContext(),
            getString(R.string.error_loading_title),
            getString(R.string.error_loading_message),
            getString(R.string.ok),
            { _, _ -> dismiss() }
        ).setOnCancelListener { dismiss() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_detail, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bottom Sheetをwrap_content → match_parent
        // info: wrap_contentになってるためcoordinatorの0dpが効かない
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
                ?: return@setOnShowListener
            bottomSheet.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
        }

        // viewModelにui更新のコールバックを渡す
        viewModel.uiCallback = uiCallback

        val cityName = args.cityName
        val coord = args.coord
        viewModel.getForecastData(cityName, coord)

        // データの監視
        viewModel.forecastDataEntity.observe(viewLifecycleOwner, {
            val forecastListAdapter = ForecastListAdapter(it.list)
            binding.detailForecast.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = forecastListAdapter
            }

            // 降水確率
            binding.detailPopChart.data = getGraphData(it.list)
        })
    }

    override fun onPause() {
        super.onPause()

        // コールバックのキャンセル処理
        viewModel.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** ダイアログを閉じる */
    fun dismissDialog() {
        dismiss()
    }

    /**
     * グラフの設定
     *
     * [labels]はX軸の時間表示に必要
     * ex.["12:00", "15:00", ...]
     */
    private fun initGraph(labels: List<String>) {
        binding.detailPopChart.apply {
            // 説明文を非表示
            description.isEnabled = false
            // 凡例を非表示
            legend.isEnabled = false
            // スケールを不変にセット
            setScaleEnabled(false)
            // タッチ無効
            setTouchEnabled(false)

            // 左のY軸
            axisLeft.apply {
                // 最大値
                axisMaximum = 100f
                // 最小値
                axisMinimum = 0f
                // ガイドラインの本数
                labelCount = 5
                // 軸線の非表示
                setDrawAxisLine(false)
                // ラベルの非表示
                setDrawLabels(false)
            }

            // 右のY軸
            axisRight.isEnabled = false

            // X軸
            xAxis.apply {
                // 軸線を下に
                position = XAxis.XAxisPosition.BOTTOM
                // 軸線を非表示
                setDrawAxisLine(false)
                // ガイドライン非表示
                setDrawGridLines(false)
                // ラベルの設定
                valueFormatter = IndexAxisValueFormatter(labels)
            }
        }
    }

    /**
     * グラフ初期化とデータを取得
     */
    private fun getGraphData(list: List<ForecastItemEntity>): LineData {
        // Ｘ軸のラベル
        val labels = list.map { ConvertUtils.convertDate(it.date.date, "H:mm") }
        initGraph(labels)

        // 降水確率のリスト
        val popList = list.map { it.getPop() }
        val entries = arrayListOf<Entry>()
        popList.forEachIndexed { index, pop ->
            entries.add(Entry(index.toFloat(), pop.toFloat()))
        }
        // 折れ線グラフに整形
        val dataSet = LineDataSet(entries, "pop")
        // 値を整数表記に上書き
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String = "${value.toInt()}%"
        }

        return LineData(dataSet)
    }
}