package com.hectordelgado.caredrivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.repository.RideRepository
import kotlinx.coroutines.launch

class RideDetailsViewModel(private val rideRepository: RideRepository) : ViewModel() {
    /**
     * Gets the [Ride] object from the database with the given id.
     * @param id the id of the ride object.
     * @param onSuccess callback that is triggered when the operation is successful.
     */
    fun getRide(id: Int, onSuccess: (Ride) -> Unit) {
        viewModelScope.launch {
            val ride = rideRepository.getById(id)
            onSuccess(ride)
        }
    }
}