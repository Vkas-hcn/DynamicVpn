package sdfhj

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.widget.ImageView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import dy.na.mic.R
import dy.na.mic.bean.DynamicVpnBean
import dy.na.mic.utils.DynamicUtils

class CKOG(private val dataList: MutableList<DynamicVpnBean>) :
    RecyclerView.Adapter<CKOG.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCountry: TextView = itemView.findViewById(R.id.tv_item_name)
        var imgFlag: ImageView = itemView.findViewById(R.id.img_item_flg)
        var conItem: LinearLayout = itemView.findViewById(R.id.ll_item)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 处理 item 点击事件
                    onItemClick(position)
                }
            }
        }
    }

    // 定义点击事件的回调接口
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    // 在 item 点击事件中触发回调
    private fun onItemClick(position: Int) {
        onItemClickListener?.onItemClick(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        // 加载自定义的布局文件
        val itemView: View = inflater.inflate(R.layout.item_service_dynamic, parent, false)

        // 创建ViewHolder对象
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 获取数据
        val item = dataList[position]
        // 将数据绑定到视图上
        if (item.dynamic_best == true) {
            holder.txtCountry.text = "Faster server"
            holder.imgFlag.setImageResource(DynamicUtils.getFlagCountryDynamic("Faster server"))
        } else {
            holder.txtCountry.text = String.format(item.dynamic_country + "-" + item.dynamic_city)
            holder.imgFlag.setImageResource(DynamicUtils.getFlagCountryDynamic(item.dynamic_country.toString()))
        }
        if (item.dynamic_check == true) {
            holder.conItem.setBackgroundResource(R.drawable.bg_item_type)
        } else {
            holder.conItem.setBackgroundResource(R.drawable.bg_item)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addData(newData: MutableList<DynamicVpnBean>) {
        dataList.addAll(newData)
        notifyDataSetChanged()
    }
}