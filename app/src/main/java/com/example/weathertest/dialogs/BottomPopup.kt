package com.example.weathertest.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.weathertest.R
import com.example.weathertest.model.WeatherData
import com.example.weathertest.util.Constants

class BottomPopup (private val context: Context, private val weather: WeatherData) {

    private lateinit var icon: ImageView
    private lateinit var country: TextView
    private lateinit var summary: TextView

    private val popupView: View = LayoutInflater.from(context).inflate(R.layout.popup_layout, null)
    private val popupWindow = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    init {
        initView()
        createPopUp()
        updateUI()
    }

    private fun initView() {
        icon = popupView.findViewById(R.id.ivWeather)
        country = popupView.findViewById(R.id.tvCountry)
        summary = popupView.findViewById(R.id.tvSummary)
    }

    private fun createPopUp() {
        popupWindow.isTouchable = true
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        val insetDrawable = InsetDrawable(ColorDrawable(Color.TRANSPARENT), 16, 0, 16, 16)
        popupWindow.setBackgroundDrawable(insetDrawable)
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0)
    }

    private fun updateUI() {
        val temperature = ((weather.temperature - 32) * 5 / 9).toInt()
        val strSummary = "$temperature° C, ${weather.summary.split(".")[0]}"

        country.text = weather.timezone
        summary.text = strSummary

        val iconUrl = "${Constants.ICON_WEATHER_URL}${weather.icon}.png"
        Glide.with(context)
            .load(iconUrl)
            .into(icon)
    }

    fun dismiss() {
        popupWindow.dismiss()
    }
}