package com.hectordelgado.caredrivers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.hectordelgado.caredrivers.R
import com.hectordelgado.caredrivers.databinding.ActivityRideDetailsBinding
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.repository.AppDatabase
import com.hectordelgado.caredrivers.util.centsToDollarsAndCents
import com.hectordelgado.caredrivers.util.toFormattedTime
import com.hectordelgado.caredrivers.util.toLocalDateTime
import com.hectordelgado.caredrivers.util.toShortName
import com.hectordelgado.caredrivers.viewmodel.RideDetailsViewModel
import com.hectordelgado.caredrivers.viewmodel.RideDetailsViewModelFactory
import java.time.format.TextStyle
import java.util.*

class RideDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: RideDetailsViewModel
    private lateinit var binding: ActivityRideDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rideId = intent.getIntExtra("rideId", 0)

        initializeViewModel()

        viewModel.getRide(rideId) { ride ->
            initializeUI(ride)
        }
    }

    private fun initializeViewModel() {
        val rideDao = AppDatabase.getInstance(applicationContext).rideDao()
        val factory = RideDetailsViewModelFactory(rideDao)
        viewModel = ViewModelProvider(this, factory).get(RideDetailsViewModel::class.java)
    }

    private fun initializeUI(ride: Ride) {
        val startLdt = ride.startsAt.toLocalDateTime()
        val endLdt = ride.endsAt.toLocalDateTime()

        val date = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
        val startTime = startLdt.toFormattedTime()
        val endTime = endLdt.toFormattedTime()
        val estimatedAmount = "$${ride.estimatedEarningsCents.centsToDollarsAndCents()}"
        val pickupAddress = ride.orderedWaypoints.first().location.address
        val dropOffAddress = ride.orderedWaypoints.last().location.address
        val tripId = ride.tripId.toString()
        val rideMiles = "${ride.estimatedRideMiles} mi"
        val rideMinutes = "${ride.estimatedRideMinutes} min"

        binding.dateTV.text = date
        binding.startTimeTV.text = startTime
        binding.endTimeTV.text = endTime
        binding.estimatedAmountTV.text = estimatedAmount
        binding.pickupTV.text = pickupAddress
        binding.dropoffTV.text = dropOffAddress
        binding.tripIdTV.text = tripId
        binding.rideMilesTV.text = rideMiles
        binding.rideMinutesTV.text = rideMinutes
    }
}