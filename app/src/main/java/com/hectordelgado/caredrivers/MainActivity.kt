package com.hectordelgado.caredrivers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.hectordelgado.caredrivers.network.ApiHelper
import com.hectordelgado.caredrivers.network.RetrofitBuilder
import com.hectordelgado.caredrivers.viewmodel.MainViewModel
import com.hectordelgado.caredrivers.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiHelper = ApiHelper(RetrofitBuilder.apiService)
        val factory = MainViewModelFactory(apiHelper)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
}