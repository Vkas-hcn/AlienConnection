package com.beetle.chili.triggers.connection.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.blkfh.AlienBean
import com.beetle.chili.triggers.connection.blkfh.VInForBean
import com.beetle.chili.triggers.connection.uasndje.ListActivity
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.google.gson.Gson

class ServiceAdaoter (private val vpnList: MutableList<VInForBean>, private val activity: ListActivity) :
    RecyclerView.Adapter<ServiceAdaoter.AppViewHolder>() {

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgFlag: AppCompatImageView = view.findViewById(R.id.item_flg)
        val tvCou: TextView = view.findViewById(R.id.tv_coutory)
        val imgCheck: AppCompatImageView = view.findViewById(R.id.img_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val item = vpnList[position]
        val vpnCharBean = DataUtils.nowVpn.let {
            Gson().fromJson(it, VInForBean::class.java)
        }

        holder.tvCou.text = item.getName()
        holder.imgFlag.setImageResource(
            if (item.isSmart) R.drawable.icon_s_logo else item.getIcon()
        )

        val isSele =
            vpnCharBean?.let { vpnCharBean.host == item.host && vpnCharBean.isSmart == item.isSmart } == true

        holder.imgCheck.setImageResource(
            if (isSele && App.vvState) R.drawable.item_c else R.drawable.item_dis
        )

        holder.itemView.setOnClickListener {
            if (!(isSele && App.vvState)) {
                clickFun(item)
            }
        }
    }

    override fun getItemCount(): Int = vpnList.size

    private fun clickFun(jsonBean: VInForBean) {
        if (App.vvState) {
            showDisConnectFun {
                DataUtils.clickVpn = Gson().toJson(jsonBean)
                endThisPage()
            }
        } else {
            DataUtils.nowVpn = Gson().toJson(jsonBean)
            endThisPage()
        }
    }

    private fun endThisPage() {
        val data = Intent().apply {
            putExtra("end", "list")
        }
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    private fun showDisConnectFun(nextFun: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle("Tip")
            .setMessage("Whether To Disconnect The Current Connection")
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton("YES") { _, _ -> nextFun() }
            .setNegativeButton("NO", null)
            .show()
    }
}