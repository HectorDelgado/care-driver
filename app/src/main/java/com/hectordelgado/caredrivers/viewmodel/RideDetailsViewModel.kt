package com.hectordelgado.caredrivers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.repository.RideRepository
import kotlinx.coroutines.launch

class RideDetailsViewModel(private val rideRepository: RideRepository) : ViewModel() {
    fun getRide(id: Int, onSuccess: (Ride) -> Unit) {
        viewModelScope.launch {
            val ride = rideRepository.getById(id)
            onSuccess(ride)
        }
    }
}