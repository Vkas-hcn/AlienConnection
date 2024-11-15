package com.beetle.chili.triggers.connection.wekgisa

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.beetle.chili.triggers.connection.R

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        // 设置为无标题的对话框
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)  // 禁止点击外部取消
        setCanceledOnTouchOutside(false)  // 禁止点击外部关闭弹框

        // 设置布局
        setContentView(R.layout.dialog_loading)

        // 使对话框全屏
        window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)

        // 使背景透明
        window?.setBackgroundDrawable(ContextCompat.getDrawable(context, android.R.color.transparent))
    }

    // 显示加载框
    fun showLoading() {
        if (!isShowing) {
            show()
        }
    }

    // 隐藏加载框
    fun hideLoading() {
        if (isShowing) {
            dismiss()
        }
    }
}
