package com.hectordelgado.caredrivers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.repository.ApiRepository
import com.hectordelgado.caredrivers.ResponseError
import com.hectordelgado.caredrivers.centsToDollarsAndCents
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.repository.RideRepository
import com.hectordelgado.caredrivers.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val apiRepository: ApiRepository,
    private val rideRepository: RideRepository
    ) : ViewModel() {

    private val _tripCards = MutableLiveData<List<Trip.TripCard>>()
    val trips: LiveData<List<Trip.TripCard>>
        get() = _tripCards

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

    private fun createTripCardsFromRide(rides: List<Ride>) {
        val tripCards = mutableListOf<Trip.TripCard>()

        // Create rides
        rides.forEach { ride ->
            Log.d("logz", "Start Date: ${ride.startsAt}")
            val startLdt = ride.startsAt.toLocalDateTime()
            val endLdt = ride.endsAt.toLocalDateTime()

            val startTime = "${startLdt.hour}:${startLdt.minute}"
            val endTime = "${endLdt.hour}:${endLdt.minute}"
            val numberOfPassengers = ride.orderedWaypoints.maxOf { it.passengers.size }
            val boostersRequired = ride.orderedWaypoints
                .first()
                .passengers
                .fold(0) { sum, element -> sum + if (element.boosterSeat) 1 else 0 }
            val boosterDescription = when(boostersRequired) {
                0 -> ""
                1 -> "• 1 booster"
                else -> "• $boostersRequired boosters"
            }
            val boosterRiderDescription = when(numberOfPassengers) {
                1 -> "(1 rider$boosterDescription)"
                else -> "($numberOfPassengers riders$boosterDescription)"
            }
            val tripEstimate = "$${ride.estimatedEarningsCents.centsToDollarsAndCents()}"


            tripCards.add(Trip.TripCard(startLdt, endLdt, startTime, endTime, boosterRiderDescription, tripEstimate, ride.orderedWaypoints))
        }

        createTripHeadersFromTripCards(tripCards)
        _tripCards.value = tripCards

        tripCards.forEach {
            Log.d("logz", "TripCard -> $it")
        }
    }

    private fun createTripHeadersFromTripCards(list: List<Trip.TripCard>) {
        val dates = list.distinctBy { "${it.startLdt.monthValue}${it.startLdt.dayOfMonth}" }
        Log.d("logz", "Unique dates are: $dates")
    }
}