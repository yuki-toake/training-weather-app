package jp.co.greensys.weatherapp.ui.fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import jp.co.greensys.weatherapp.R
import jp.co.greensys.weatherapp.databinding.FragmentHomeBinding
import jp.co.greensys.weatherapp.domain.remote_data_source.Coord
import jp.co.greensys.weatherapp.utils.AlertDialogUtils
import jp.co.greensys.weatherapp.utils.FragmentUtils.Companion.findNavControllerSafety

/**
 * ホーム画面のFragment
 *
 * Created by Yuki Toake on 2021/12/16.
 */
class HomeFragment : Fragment(), LocationListener {
    companion object {
        const val PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val PROVIDER = LocationManager.NETWORK_PROVIDER
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 権限ランチャー
    private lateinit var launcher: ActivityResultLauncher<String>

    // 位置情報のマネージャー
    private val locationManager: LocationManager by lazy {
        requireContext().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
    }

    // 補足中のダイアログ
    private val progressDialog: ProgressDialog by lazy { createProgressDialog() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // info: ここで生成する
        launcher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 「都道府県」ボタン
        binding.homeSelectButton.setOnClickListener {
            // 都道府県選択画面へ
            val toSelectScreen = HomeFragmentDirections.actionHomeFragmentToSelectFragment()
            findNavControllerSafety(R.id.homeFragment)?.navigate(toSelectScreen)
        }

        // 「現在地」ボタン
        binding.homeLocationButton.setOnClickListener {
            getLocation()
        }

        // 権限リクエスト
        launcher.launch(PERMISSION_LOCATION)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** 現在地取得 */
    private fun getLocation() {
        // 権限許可されてるか確認
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionDeny()
            return
        }

        // プロバイダーがオフの場合
        if (!locationManager.isProviderEnabled(PROVIDER)) {
            AlertDialogUtils.alertDialog(
                requireContext(),
                getString(R.string.alertdialog_gps_off),
                getString(R.string.alertdialog_please_gps_on),
                getString(R.string.action_settings),
                { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                },
                getString(R.string.alertdialog_close)
            ).show()

            return
        }

        // ダイアログを表示
        progressDialog.show()

        // 位置情報のリクエスト
        locationManager.requestLocationUpdates(
            PROVIDER,
            1000,
            50f,
            this
        )
    }

    /** 権限拒否されている場合 */
    private fun permissionDeny() {
        AlertDialogUtils.alertDialog(
            requireContext(),
            getString(R.string.alertdialog_location_title),
            getString(R.string.alertdialog_location_message),
            getString(R.string.action_settings),
            { _, _ ->
                val uri =
                    Uri.fromParts("package", requireContext().packageName, null)
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            getString(R.string.alertdialog_close)
        ).show()
    }

    /** プログレスダイアログの非表示 */
    private fun dismissProgressDialog() {
        // プログレスダイアログの非表示
        progressDialog.dismiss()
        // 位置情報の補足を停止
        locationManager.removeUpdates(this)
    }

    /** 位置情報が変更された */
    override fun onLocationChanged(location: Location) {
        val coord = Coord(location.latitude, location.longitude)
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(coord = coord)
        findNavControllerSafety(R.id.homeFragment)?.navigate(action)

        dismissProgressDialog()
    }

    /** 捕捉中に位置情報がオン → オフになったとき */
    override fun onProviderDisabled(provider: String) {
        dismissProgressDialog()

        AlertDialogUtils.alertDialog(
            requireContext(),
            getString(R.string.alertdialog_gps_off),
            getString(R.string.alertdialog_gps_keep_on),
            getString(R.string.ok)
        ).show()
    }

    /**
     * 補足中に位置情報がオフ → オンになった時
     *
     * super.onProviderEnabled(provider)をそのまま使用するとクラッシュするため
     * 削除か独自実装するのが望ましい
     */
    override fun onProviderEnabled(provider: String) {}

    /** 位置情報取得中のプログレスダイアログ */
    private fun createProgressDialog(): ProgressDialog =
        ProgressDialog(requireContext(), R.style.Theme_WeatherApp_Dialog).apply {
            setMessage(getString(R.string.getting_location))
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setCancelable(false)
        }
}