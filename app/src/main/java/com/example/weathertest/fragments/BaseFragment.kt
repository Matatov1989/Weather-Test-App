package com.example.weathertest.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weathertest.dialogs.CustomLoadingDialog
import com.example.weathertest.dialogs.DialogSettingsInformation
import com.example.weathertest.util.Constants.PERMISSION_LOCATION
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    private lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = CustomLoadingDialog(requireContext())
    }

    fun initToolbar(toolbar: Toolbar, title: String, isBackButton: Boolean = false) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = title

        if (isBackButton) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

            toolbar.setNavigationOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    fun isGpsEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isEnable)
            DialogSettingsInformation().show(childFragmentManager, "DialogSettingsInformation")

        return isEnable
    }

    fun openToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    fun openSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoader() {
        loadingDialog.show()
    }

    fun hideLoader() {
        loadingDialog.dismiss()
    }

    fun checkPermissionLocation(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION
            )
            false
        }
    }
}