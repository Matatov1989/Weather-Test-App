package com.example.weathertest.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.weathertest.R

class BottomPopup (private val context: Context) {

    private val popupView: View = LayoutInflater.from(context).inflate(R.layout.popup_layout, null)
    private val popupWindow = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    init {
        popupWindow.isTouchable = true
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        val insetDrawable = InsetDrawable(ColorDrawable(Color.TRANSPARENT), 16, 0, 16, 16)
        popupWindow.setBackgroundDrawable(insetDrawable)
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0)
    }
    fun dismiss() {
        popupWindow.dismiss()
    }
}