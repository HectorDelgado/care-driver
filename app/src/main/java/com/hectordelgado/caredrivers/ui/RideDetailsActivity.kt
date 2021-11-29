package com.hectordelgado.caredrivers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hectordelgado.caredrivers.R
import com.hectordelgado.caredrivers.adapter.WaypointAdapter
import com.hectordelgado.caredrivers.databinding.ActivityRideDetailsBinding
import com.hectordelgado.caredrivers.model.OrderedWaypoint
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.repository.RideDatabase
import com.hectordelgado.caredrivers.util.centsToDollarsAndCents
import com.hectordelgado.caredrivers.util.toFormattedTime
import com.hectordelgado.caredrivers.util.toLocalDateTime
import com.hectordelgado.caredrivers.util.toShortName
import com.hectordelgado.caredrivers.viewmodel.RideDetailsViewModel
import com.hectordelgado.caredrivers.viewmodel.RideDetailsViewModelFactory

class RideDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var viewModel: RideDetailsViewModel
    private lateinit var binding: ActivityRideDetailsBinding
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val rideId = intent.getIntExtra("rideId", 0)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initializeViewModel()

        viewModel.getRide(rideId) { ride ->
            initializeUI(ride)
            initializeMapView(ride.orderedWaypoints)
        }
    }

    /**
     * Initializes the ViewModel for the UI.
     */
    private fun initializeViewModel() {
        val rideDao = RideDatabase.getInstance(applicationContext).rideDao()
        val factory = RideDetailsViewModelFactory(rideDao)
        viewModel = ViewModelProvider(this, factory).get(RideDetailsViewModel::class.java)
    }

    /**
     * Initializes various UI components with data from the retrieved Ride.
     */
    private fun initializeUI(ride: Ride) {
        val startLdt = ride.startsAt.toLocalDateTime()
        val endLdt = ride.endsAt.toLocalDateTime()

        val date = "${startLdt.dayOfWeek.toShortName()} ${startLdt.monthValue}/${startLdt.dayOfMonth}"
        val startTime = startLdt.toFormattedTime()
        val endTime = endLdt.toFormattedTime()
        val estimatedAmount = "$${ride.estimatedEarningsCents.centsToDollarsAndCents()}"
        val inSeries = if (ride.inSeries) getString(R.string.part_of_series) else ""
        val tripId = ride.tripId.toString()
        val rideMiles = "${ride.estimatedRideMiles} mi"
        val rideMinutes = "${ride.estimatedRideMinutes} min"

        binding.dateTV.text = date
        binding.startTimeTV.text = startTime
        binding.endTimeTV.text = endTime
        binding.estimatedAmountTV.text = estimatedAmount
        binding.inSeriesTV.text = inSeries
        binding.tripIdTV.text = tripId
        binding.rideMilesTV.text = rideMiles
        binding.rideMinutesTV.text = rideMinutes

        initializeRecyclerView(ride.orderedWaypoints)
    }

    /**
     * Initializes the RecyclerView with the list of OrderedWaypoint objects.
     */
    private fun initializeRecyclerView(waypoints: List<OrderedWaypoint>) {
        val adapter = WaypointAdapter(waypoints)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Initializes the MapView with markers on all OrderedWaypoints, and updates the camera
     * location to the center of all the locations.
     */
    private fun initializeMapView(waypoints: List<OrderedWaypoint>) {
        val latLngs = waypoints.map { LatLng(it.location.lat, it.location.lng) }
        val averageLat = latLngs.fold(0.0) { sum, latLng -> sum + latLng.latitude}.div(latLngs.size)
        val averageLng = latLngs.fold(0.0) { sum, latLng -> sum + latLng.longitude}.div(latLngs.size)
        val averageLatLng = LatLng(averageLat, averageLng)
        latLngs.forEach { latLng ->
            googleMap?.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
        }
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(averageLatLng, 10.0f))
        googleMap?.animateCamera(cameraUpdate)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }
}