package com.hectordelgado.caredrivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hectordelgado.caredrivers.repository.ApiRepository
import com.hectordelgado.caredrivers.repository.ApiDao
import com.hectordelgado.caredrivers.repository.RideDao
import com.hectordelgado.caredrivers.repository.RideRepository

class MainViewModelFactory(private val apiDao: ApiDao, private val rideDao: RideDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(ApiRepository(apiDao), RideRepository(rideDao)) as T
        }
        throw IllegalArgumentException("Unknown class name.")
    }
}