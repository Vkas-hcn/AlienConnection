package com.beetle.chili.triggers.connection.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.AlienBean
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.uasndje.HisActivity
import com.beetle.chili.triggers.connection.uasndje.ListActivity
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HistoryAdaoter(
    private val vpnList: MutableList<VInForBean>,
    private val activity: HisActivity
) :
    RecyclerView.Adapter<HistoryAdaoter.AppViewHolder>() {

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFlag: AppCompatImageView = view.findViewById(R.id.item_flg)
        val tvCou: TextView = view.findViewById(R.id.tv_coutory)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvCreateTime: TextView = view.findViewById(R.id.tv_create_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_his, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val item = vpnList[position]
        val vpnCharBean = DataUtils.nowVpn.let {
            Gson().fromJson(it, VInForBean::class.java)
        }
        holder.tvCreateTime.text = item.getVpnDateTime()
        holder.tvCou.text = item.getName()
        holder.imgFlag.setImageResource(
            if (item.isSmart) R.drawable.icon_s_logo else item.getIcon()
        )
        if (App.vvState && vpnCharBean.vpnDate == item.vpnDate) {
            activity.getEndTIme(item, holder.tvTime)
        } else {
            holder.tvTime.text = item.connectTime
        }
    }



    override fun getItemCount(): Int = vpnList.size

}