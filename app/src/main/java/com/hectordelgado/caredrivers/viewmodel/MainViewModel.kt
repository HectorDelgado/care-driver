package com.hectordelgado.caredrivers.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    fun fetchRides() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = mainRepository.fetchRides()

                if (result.isSuccessful) {
                    val hsdRide = result.body()

                    hsdRide?.rides?.let {
                        it.forEach { Log.d("logz", it.toString()) }
                    }
                } else {
                    Log.d("logz", "Error fetching data!")
                }
            }
        }

    }
}