package com.hectordelgado.caredrivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.adapter.TripAdapter
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.network.ApiHelper
import com.hectordelgado.caredrivers.network.RetrofitBuilder
import com.hectordelgado.caredrivers.viewmodel.MainViewModel
import com.hectordelgado.caredrivers.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViewModel()

//        val passengers = listOf(Passenger(3332, false, "Jop"))
//        val waypoints = listOf(OrderedWaypoint(33, true, passengers, Location("33 st", 33.3, 22.2)))
//        val temp = Trip.TripCard(
//            "Start Time",
//            "End Time",
//            "(2 riders)",
//            "$44.44",
//            waypoints
//        )
//
//        initializeRecyclerView(listOf(temp))

        viewModel.trips.observe(this) { tripCards ->
            Log.d("logz", "observing ${tripCards.size} tripcards")

            //tripCards.forEach { Log.d("logz", it.toString()) }
            initializeRecyclerView(tripCards)
        }
        viewModel.errors.observe(this) { responseError ->
            Toast.makeText(
                this,
                "There was an error getting current rides. Code: ${responseError.code}",
                Toast.LENGTH_SHORT)
                .show()
        }

        viewModel.fetchRides()
    }

    private fun initializeViewModel() {
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)
        val factory = MainViewModelFactory(apiHelper)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private fun initializeRecyclerView(data: List<Trip>) {
        Log.d("logz", "Initializing RV")

        adapter = TripAdapter(data)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}