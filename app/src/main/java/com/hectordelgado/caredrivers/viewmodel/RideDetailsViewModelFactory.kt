package com.hectordelgado.caredrivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hectordelgado.caredrivers.repository.RideDao
import com.hectordelgado.caredrivers.repository.RideRepository

class RideDetailsViewModelFactory(private val rideDao: RideDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideDetailsViewModel::class.java)) {
            return RideDetailsViewModel(RideRepository(rideDao)) as T
        }
        throw IllegalArgumentException("Unknown class name.")
    }
}