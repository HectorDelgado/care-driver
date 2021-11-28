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
import com.hectordelgado.caredrivers.model.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripAdapter(private val list: List<Trip>, private val onClick: (Trip.TripCard) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private inner class TripHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTV: TextView = itemView.findViewById(R.id.dateTV)
        private val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
        private val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
        private val estimatedAmountTV: TextView = itemView.findViewById(R.id.estimatedAmountTV)

        fun bind(item: Trip.TripHeader) {
            dateTV.text = item.tripDate
            startTimeTV.text = item.startTime
            endTimeTV.text = item.endTime
            estimatedAmountTV.text = item.endTime
        }
    }

    private inner class TripCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
        private val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
        private val descriptionTV: TextView = itemView.findViewById(R.id.rideDescriptionTV)
        private val estimateTV: TextView = itemView.findViewById(R.id.rideEstimateTV)
        private val waypointsTV: TextView = itemView.findViewById(R.id.waypointsTV)

        fun bind(item: Trip.TripCard) {
            cardView.setOnClickListener { onClick(item) }
            val startTime = item.startTime
            val endTime = item.endTime
            val description = item.riderBoosterDescription
            val estimate = item.tripEstimate
            val waypoints = item.waypoints
                .mapIndexed { index, orderedWaypoint ->
                    "${index + 1}. ${orderedWaypoint.location.address}"
                }
                .joinToString("\n")

            startTimeTV.text = startTime
            endTimeTV.text = endTime
            descriptionTV.text = description
            estimateTV.text = estimate
            waypointsTV.text = waypoints
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = when (viewType) {
            TYPE_TRIP_HEADER -> R.layout.item_trip_header
            TYPE_TRIP_CARD -> R.layout.item_trip_card
            else -> throw IllegalArgumentException("Invalid View Type")
        }.run {
            LayoutInflater
                .from(parent.context)
                .inflate(this, parent, false)
        }

        return when(viewType) {
            TYPE_TRIP_HEADER -> TripHeaderVH(view)
            TYPE_TRIP_CARD -> TripCardVH(view)
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