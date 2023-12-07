package com.example.weathertest.fragments.main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.weathertest.R
import com.example.weathertest.data.WeatherUiState
import com.example.weathertest.databinding.FragmentMainBinding
import com.example.weathertest.fragments.BaseFragment
import com.example.weathertest.model.WeatherData
import com.example.weathertest.services.GPSService
import com.example.weathertest.util.Constants.ICON_WEATHER_URL
import kotlinx.coroutines.launch


class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    var isEnable: Boolean = false

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "location_update") {
                var location = intent.getParcelableExtra<Location>("location")
                if (location == null || isEnable) {
                    location = Location("default_provider")
                    location.latitude = 32.1602438
                    location.longitude = 34.8095785
                }
                Log.d("LOCATION_APP", "${location}")
                viewModel.getWeather(location)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar, getString(R.string.titleWeather))
        initMenuToolBar()
        initObserve()
        registerLocationService()
    }

    private fun initObserve() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        val location = Location("default_provider")
        location.latitude = 32.1602438
        location.longitude = 34.8095785

        viewModel.getWeather(location)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherLiveData.collect { uiState ->
                    when (uiState) {
                        is WeatherUiState.Success -> {
                            val weather = uiState.weatherData
                            if (weather != null) {
                                updateUI(weather)
                            }
                        }
                        is WeatherUiState.Error -> {
                            openToast("Error: ${uiState.exception}")
                        }
                        is WeatherUiState.AvailableInternet -> {
                            if (uiState.isShowMessage) {
                                openSnackBar(getString(R.string.internetUnavailable))
                            }
                        }
                        is WeatherUiState.Loading -> {
                            if (uiState.isLoading) {
                                showLoader()
                            } else {
                                hideLoader()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

    }

    private fun updateUI(weather: WeatherData) {
        val temperature = ((weather.temperature - 32) * 5 / 9).toInt()

        binding.tvDegree.text = "$temperature\u00B0 C"
        binding.tvPlace.text = weather.timezone
        binding.tvSummary.text = weather.summary

        val iconUrl = "$ICON_WEATHER_URL${weather.icon}.png"

        Glide.with(requireContext())
            .load(iconUrl)
            .into(binding.ivWeather)
    }

    private fun initMenuToolBar() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            @SuppressLint("NonConstantResourceId")
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_open_map -> {
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_mainFragment_to_mapsFragment)
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun registerLocationService() {
        val isLocationPermission = checkPermissionLocation()
        if (isLocationPermission) {
            isEnable = isGpsEnabled()
            if (isEnable) {
                requireContext().registerReceiver(locationReceiver, IntentFilter("location_update"))

                val startServiceIntent = Intent(requireContext(), GPSService::class.java)
                requireContext().startService(startServiceIntent)
            }
        }
    }
}