package com.example.weathertest.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import com.example.weathertest.R

class DialogSettingsInformation : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialogSettingsGpsTitle)
            .setMessage(R.string.dialogSettingsGpsMessage)
            .setPositiveButton(R.string.btnOK, null)
            .create().apply {
                isCancelable = false
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        dismiss()
                        val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                        startActivity(settingsIntent)
                    }
                }
            }
    }
}