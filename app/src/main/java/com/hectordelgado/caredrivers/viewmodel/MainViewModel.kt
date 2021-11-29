package com.hectordelgado.caredrivers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.repository.ApiRepository
import com.hectordelgado.caredrivers.util.ResponseError
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.repository.RideRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val apiRepository: ApiRepository,
    private val rideRepository: RideRepository
    ) : ViewModel() {

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>>
        get() = _trips

    private val _errors = MutableLiveData<ResponseError>()
    val errors: LiveData<ResponseError>
        get() = _errors

    /**
     * Fetches the latest rides the HopSkipDrive API.
     * If the result is successful the Rides are parsed into
     */
    fun fetchRides() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = apiRepository.fetchRides()

                if (result.isSuccessful) {
                    result.body()?.rides?.let { rides ->
                        withContext(Dispatchers.Main) {
                            createTripCardsFromRide(rides)
                        }
                    }
                } else {
                    _errors.value = ResponseError(result.code(), result.errorBody().toString())
                }
            }
        }
    }

    fun saveRide(ride: Ride, onSuccess: () -> Unit) {
        viewModelScope.launch {
            rideRepository.deleteAll()
            rideRepository.insert(ride)
            onSuccess()
        }
    }

    private fun createTripCardsFromRide(rides: List<Ride>) {
        val tripCards = mutableListOf<Trip.TripCard>()

        // Create rides
        rides.forEach { ride ->
            tripCards.add(Trip.TripCard(ride))
        }

        createTripHeadersFromTripCards(tripCards)
        _trips.value = tripCards

        tripCards.forEach {
            Log.d("logz", "TripCard -> $it")
        }
    }

    private fun createTripHeadersFromTripCards(list: List<Trip.TripCard>) {
        //val dates = list.distinctBy { "${it.startLdt.monthValue}${it.startLdt.dayOfMonth}" }
        //Log.d("logz", "Unique dates are: $dates")
    }
}