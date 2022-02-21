package jp.co.greensys.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import jp.co.greensys.weatherapp.R
import jp.co.greensys.weatherapp.databinding.FragmentSelectBinding
import jp.co.greensys.weatherapp.utils.FragmentUtils.Companion.findNavControllerSafety

/** セレクト画面 */
class SelectFragment : Fragment(R.layout.fragment_select) {
    private var _binding: FragmentSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // リストのクリックリスナー
        binding.selectListview.setOnItemClickListener { _, tv, _, _ ->
            val name = (tv as TextView).text.toString()
            val action = SelectFragmentDirections.actionSelectFragmentToDetailFragment(name)
            findNavControllerSafety(R.id.selectFragment)?.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}