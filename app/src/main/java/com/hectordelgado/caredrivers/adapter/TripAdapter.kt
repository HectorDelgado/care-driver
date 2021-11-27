package com.hectordelgado.caredrivers.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.R
import com.hectordelgado.caredrivers.model.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripAdapter(private val list: List<Trip>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        Log.d("logz", "Entering primary init with ${list.size} items")
    }

    inner class TripHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Trip.TripHeader) {
            val dateTV: TextView = itemView.findViewById(R.id.dateTV)
            val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
            val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
            val estimatedAmountTV: TextView = itemView.findViewById(R.id.estimatedAmountTV)

            dateTV.text = item.tripDate
            startTimeTV.text = item.startTime
            endTimeTV.text = item.endTime
            estimatedAmountTV.text = item.endTime
        }
    }

    inner class TripCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Trip.TripCard) {
            Log.d("logz", "binding an tripcard")
            val startTime = item.startTime
            val endTime = item.endTime
            val description = item.riderBoosterDescription
            val estimate = item.tripEstimate
            val waypoints = item.waypoints.map { it.location.address }.joinToString("\n")

            val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
            val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
            val descriptionTV: TextView = itemView.findViewById(R.id.rideDescriptionTV)
            val estimateTV: TextView = itemView.findViewById(R.id.rideEstimateTV)
            val waypointsTV: TextView = itemView.findViewById(R.id.waypointsTV)

            startTimeTV.text = startTime
            endTimeTV.text = endTime
            descriptionTV.text = description
            estimateTV.text = estimate
            waypointsTV.text = waypoints
        }
    }

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun bindTripHeader(item: Trip.TripHeader) {
            Log.d("logz", "binding an trip header")
            val dateTV: TextView = itemView.findViewById(R.id.dateTV)
            val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
            val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
            val estimatedAmountTV: TextView = itemView.findViewById(R.id.estimatedAmountTV)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            //final java.util.Date dateObj = sdf.parse("2013-10-22T01:37:56");

            //aRevisedDate = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a").format(dateObj);
            //System.out.println(aRevisedDate);

            dateTV.text = item.tripDate
            startTimeTV.text = item.startTime
            endTimeTV.text = item.endTime
            estimatedAmountTV.text = item.endTime
        }

        private fun bindTripCard(item: Trip.TripCard) {
            Log.d("logz", "binding an tripcard")
            val startTime = item.startTime
            val endTime = item.endTime
            val description = item.riderBoosterDescription
            val estimate = item.tripEstimate
            val waypoints = item.waypoints.map { it.location.address }.joinToString("\n")

            val startTimeTV: TextView = itemView.findViewById(R.id.timeStartTV)
            val endTimeTV: TextView = itemView.findViewById(R.id.timeEndTV)
            val descriptionTV: TextView = itemView.findViewById(R.id.rideDescriptionTV)
            val estimateTV: TextView = itemView.findViewById(R.id.estimatedAmountTV)
            val waypointsTV: TextView = itemView.findViewById(R.id.waypointsTV)

            startTimeTV.text = startTime
            endTimeTV.text = endTime
            descriptionTV.text = description
            estimateTV.text = estimate
            waypointsTV.text = waypoints
        }
        fun bind(trip: Trip) {
            Log.d("logz", "Attempting to bind Data...")
            when (trip) {
                is Trip.TripHeader -> bindTripHeader(trip)
                is Trip.TripCard -> bindTripCard(trip)
            }
        }
    }

    //private val adapterData = mutableListOf<Trip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("logz", "creating VH")
        val view = when (viewType) {
            TYPE_TRIP_HEADER -> R.layout.item_trip_header
            TYPE_TRIP_CARD -> R.layout.item_trip_card
            else -> throw IllegalArgumentException("invalid type")
        }.run {
            LayoutInflater
                .from(parent.context)
                .inflate(this, parent, false)
        }


        return when(viewType) {
            TYPE_TRIP_HEADER -> TripHeaderVH(view)
            TYPE_TRIP_CARD -> TripCardVH(view)
            else -> throw IllegalArgumentException("invalid type")
        }
        //return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("logz", "calling onBindViewHolder")
        when (list[position].viewType) {
            TYPE_TRIP_HEADER -> (holder as TripHeaderVH).bind(list[position] as Trip.TripHeader)
            TYPE_TRIP_CARD -> (holder as TripCardVH).bind(list[position] as Trip.TripCard)
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun getItemCount(): Int {
        Log.d("logz", "Count is ${list.size}")
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("logz", "View type is $position")

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