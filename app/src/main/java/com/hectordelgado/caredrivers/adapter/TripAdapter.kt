package com.hectordelgado.caredrivers.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.R
import com.hectordelgado.caredrivers.databinding.ItemTripCardBinding
import com.hectordelgado.caredrivers.databinding.ItemTripHeaderBinding
import com.hectordelgado.caredrivers.model.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripAdapter(private val list: List<Trip>, private val onClick: (Trip.TripCard) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private inner class TripHeaderVH(private val itemBinding: ItemTripHeaderBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Trip.TripHeader) {
            itemBinding.dateTV.text = item.tripDate
            itemBinding.timeStartTV.text = item.startTime
            itemBinding.timeEndTV.text = item.endTime
            itemBinding.estimatedAmountTV.text = item.endTime
        }
    }

    private inner class TripCardVH(private val itemBinding: ItemTripCardBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Trip.TripCard) {
            itemBinding.cardView.setOnClickListener { onClick(item) }
            itemBinding.timeStartTV.text = item.startTime
            itemBinding.timeEndTV.text = item.endTime
            itemBinding.rideDescriptionTV.text = item.riderBoosterDescription
            itemBinding.rideEstimateTV.text = item.tripEstimate
            itemBinding.waypointsTV.text = item.waypoints
                .mapIndexed { index, orderedWaypoint ->
                    "${index + 1}. ${orderedWaypoint.location.address}"
                }
                .joinToString("\n")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            TYPE_TRIP_HEADER -> {
                val itemBinding = ItemTripHeaderBinding.inflate(layoutInflater, parent, false)
                TripHeaderVH(itemBinding)
            }
            TYPE_TRIP_CARD -> {
                val itemBinding = ItemTripCardBinding.inflate(layoutInflater, parent, false)
                TripCardVH(itemBinding)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            TYPE_TRIP_HEADER -> (holder as TripHeaderVH).bind(list[position] as Trip.TripHeader)
            TYPE_TRIP_CARD -> (holder as TripCardVH).bind(list[position] as Trip.TripCard)
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int {
        return when (list[position].viewType) {
            0 -> TYPE_TRIP_HEADER
            1 -> TYPE_TRIP_CARD
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    companion object {
        private const val TYPE_TRIP_HEADER = 0
        private const val TYPE_TRIP_CARD = 1
    }
}