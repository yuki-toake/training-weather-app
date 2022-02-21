package jp.co.greensys.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.greensys.weatherapp.BR
import jp.co.greensys.weatherapp.R
import jp.co.greensys.weatherapp.databinding.Item3hListviewBinding
import jp.co.greensys.weatherapp.domain.entity.ForecastItemEntity

/**
 * 天気予報のリストアダプタ
 *
 * Created by Toake on 2021/12/23.
 */
class ForecastListAdapter(private val list: List<ForecastItemEntity>) :
    ListAdapter<ForecastItemEntity, ForecastListAdapter.ItemViewHolder>(
        object : DiffUtil.ItemCallback<ForecastItemEntity>() {
            override fun areItemsTheSame(oldItemEntity: ForecastItemEntity, newItemEntity: ForecastItemEntity): Boolean =
                oldItemEntity.date == newItemEntity.date

            override fun areContentsTheSame(oldItemEntity: ForecastItemEntity, newItemEntity: ForecastItemEntity): Boolean =
                oldItemEntity == newItemEntity
        }
    ) {
    override fun getItem(position: Int): ForecastItemEntity = list[position]

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            DataBindingUtil.inflate(inflater, R.layout.item_3h_listview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = getItem(position)
        /** 動的にbindingする場合は自動生成されている[BR]を利用 */
        holder.binding.setVariable(BR.forecast, data)
    }

    inner class ItemViewHolder(val binding: Item3hListviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}