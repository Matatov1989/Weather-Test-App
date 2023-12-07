package com.example.weathertest.fragments.map

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weathertest.R
import com.example.weathertest.data.WeatherUiState
import com.example.weathertest.databinding.FragmentMapsBinding
import com.example.weathertest.dialogs.BottomPopup
import com.example.weathertest.fragments.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MapsFragment : BaseFragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MapsViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        initListener(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        initToolbar(binding.toolbar, getString(R.string.titleMap), true)
        initObserve()
        isGpsEnabled()
    }

    private fun initListener(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Marker"))
            viewModel.getWeather(convertLatLngToLocation(latLng))
        }
    }

    private fun initObserve() {
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherLiveData.collect { uiState ->
                    when (uiState) {
                        is WeatherUiState.Success -> {
                            val weather = uiState.weatherData
                            if (weather != null) {
                                BottomPopup(requireContext(), weather)
                            }
                        }
                        is WeatherUiState.Error -> {
                            Toast.makeText(
                                context,
                                "Error: ${uiState.exception}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is WeatherUiState.AvailableInternet -> {
                            if (uiState.isShowMessage) {
                                val message = getString(R.string.internetUnavailable)
                                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        is WeatherUiState.Loading -> {
                        }
                    }
                }
            }
        }
    }

    fun convertLatLngToLocation(latLng: LatLng): Location {
        val location = Location("custom_provider_name")
        location.latitude = latLng.latitude
        location.longitude = latLng.longitude
        return location
    }
}