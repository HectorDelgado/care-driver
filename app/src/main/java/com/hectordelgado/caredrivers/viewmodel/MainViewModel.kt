package com.hectordelgado.caredrivers.viewmodel

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
     * If the result is successful then it is converted into [Trip] objects.
     */
    fun fetchRides() {
        viewModelScope.launch {
            // Switch to IO thread
            withContext(Dispatchers.IO) {
                val result = apiRepository.fetchRides()

                if (result.isSuccessful) {
                    result.body()?.rides?.let { rides ->
                        // Switch back to main thread
                        withContext(Dispatchers.Main) {
                            val tripCards = rides.map { Trip.TripCard(it) }
                            createTripsFromTripCards(tripCards)
                        }
                    }
                } else {
                    _errors.value = ResponseError(result.code(), result.errorBody().toString())
                }
            }
        }
    }

    /**
     * Deletes any previous data then saves the given [Ride] to the database.
     * @param ride The Ride object to store into the database.
     * @param onSuccess A callback that is called when the operation is successful.
     */
    fun saveRide(ride: Ride, onSuccess: () -> Unit) {
        viewModelScope.launch {
            rideRepository.deleteAll()
            rideRepository.insert(ride)
            onSuccess()
        }
    }

    /**
     * Creates a list of [Trip] objects containing both Header and Card variants.
     * Each header is followed by its successive cards.
     */
    private fun createTripsFromTripCards(list: List<Trip.TripCard>) {
        val allTrips = mutableListOf<Trip>()

        // Get unique dates for all trips (e.g, 6/17, 6/18, 6/20)
        val uniqueDates = HashSet<String>()
        list.forEach {
            val startLdt = it.ride.startsAt.toLocalDateTime()
            val id = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
            uniqueDates.add(id)
        }

        uniqueDates.forEach { tripDate ->
            // Get TripCards that have the same start date as the current date.
            val cardsForDate = list.filter { tripCard ->
                val startLdt = tripCard.ride.startsAt.toLocalDateTime()
                val id = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
                tripDate.contains(id)
            }

            // Start time for all trips with this date is start time of the first ride.
            val startTime = cardsForDate.first().ride.startsAt.toLocalDateTime().toFormattedTime()

            // End time for all trips with this date is end time of the last ride.
            val endTime = cardsForDate.last().ride.endsAt.toLocalDateTime().toFormattedTime()

            // Get total from all the estimated earnings
            val estimatedTotal = cardsForDate
                .fold(0) { sum, item -> sum + item.ride.estimatedEarningsCents }
                .centsToDollarsAndCents()
            val header = Trip.TripHeader(tripDate, startTime, endTime, estimatedTotal)

            // Add Header, followed by Cards
            allTrips.add(header)
            allTrips.addAll(cardsForDate)
        }

        _trips.value = allTrips
    }
}