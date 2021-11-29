package com.hectordelgado.caredrivers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.adapter.TripAdapter
import com.hectordelgado.caredrivers.databinding.ActivityMainBinding
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.repository.ApiDao
import com.hectordelgado.caredrivers.network.RetrofitBuilder
import com.hectordelgado.caredrivers.viewmodel.MainViewModel
import com.hectordelgado.caredrivers.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TripAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeViewModel()

        viewModel.trips.observe(this) { tripCards ->
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
        val apiHelper = ApiDao(RetrofitBuilder.apiService)
        val factory = MainViewModelFactory(apiHelper)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    private fun initializeRecyclerView(data: List<Trip>) {
        adapter = TripAdapter(data) {
            Intent(this, RideDetailsActivity::class.java).also {
                startActivity(it)
            }
        }
        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}