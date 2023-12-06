package com.example.weathertest.fragments.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.example.weathertest.R
import com.example.weathertest.data.WeatherUiState
import com.example.weathertest.databinding.FragmentMainBinding
import com.example.weathertest.fragments.BaseFragment
import com.example.weathertest.model.WeatherData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel


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
    }

    private fun initObserve() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

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

    private fun updateUI(weather: WeatherData) {
        binding.tvDegree.text = weather.temperature.toString()
        binding.tvPlace.text = weather.timezone
        binding.tvWeather.text = weather.summary
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
}