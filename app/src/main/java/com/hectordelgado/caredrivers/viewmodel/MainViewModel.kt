package com.hectordelgado.caredrivers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectordelgado.caredrivers.repository.ApiRepository
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.repository.RideRepository
import com.hectordelgado.caredrivers.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashSet

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
        val trips = rides.map { Trip.TripCard(it) }
        createTripHeadersFromTripCards(trips)
        //_trips.value = trips
    }

    private fun createTripHeadersFromTripCards(list: List<Trip.TripCard>) {
        val allTrips = mutableListOf<Trip>()


        val uniqueDates = HashSet<String>()
        list.forEach {
            val startLdt = it.ride.startsAt.toLocalDateTime()
            val id = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
            uniqueDates.add(id)
        }

        uniqueDates.forEach { tripDate ->
            val cardsForDate = list.filter { tripCard ->
                val startLdt = tripCard.ride.startsAt.toLocalDateTime()
                val id = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
                tripDate.contains(id)
            }
            val startTime = cardsForDate.first().ride.startsAt.toLocalDateTime().toFormattedTime()
            val endTime = cardsForDate.last().ride.endsAt.toLocalDateTime().toFormattedTime()
            val estimatedTotal = cardsForDate
                .fold(0) { sum, item ->
                    sum + item.ride.estimatedEarningsCents
                }
                .centsToDollarsAndCents()
            val header = Trip.TripHeader(tripDate, startTime, endTime, estimatedTotal)
            allTrips.add(header)
            allTrips.addAll(cardsForDate)
        }

        _trips.value = allTrips

    }

}