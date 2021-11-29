package com.hectordelgado.caredrivers.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hectordelgado.caredrivers.repository.ApiRepository
import com.hectordelgado.caredrivers.repository.ApiDao

class MainViewModelFactory(private val apiHelper: ApiDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(ApiRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name.")
    }
}