package com.example.weathertest.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.weathertest.R

class CustomLoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_loading_dialog)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}