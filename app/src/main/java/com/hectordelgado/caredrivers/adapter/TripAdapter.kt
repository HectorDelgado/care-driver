package com.hectordelgado.caredrivers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.util.centsToDollarsAndCents
import com.hectordelgado.caredrivers.databinding.ItemTripCardBinding
import com.hectordelgado.caredrivers.databinding.ItemTripHeaderBinding
import com.hectordelgado.caredrivers.model.Ride
import com.hectordelgado.caredrivers.model.Trip
import com.hectordelgado.caredrivers.util.toFormattedTime
import com.hectordelgado.caredrivers.util.toLocalDateTime

/**
 * Class responsible for inflating and binding views in a RecyclerView with [Trip] items.
 * @param list The list of elements this adapter will display.
 * @param onClick Callback method that is triggered when a TripCard is clicked.
 */
class TripAdapter(private val list: List<Trip>, private val onClick: (Ride) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private inner class TripHeaderVH(private val itemBinding: ItemTripHeaderBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Trip.TripHeader) {
            itemBinding.dateTV.text = item.tripDate
            itemBinding.timeStartTV.text = item.startTime
            itemBinding.timeEndTV.text = item.endTime
            itemBinding.estimatedAmountTV.text = "$${item.estimatedTotal}"
        }
    }

    private inner class TripCardVH(private val itemBinding: ItemTripCardBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Trip.TripCard) {
            val startLdt = item.ride.startsAt.toLocalDateTime()
            val endLdt = item.ride.endsAt.toLocalDateTime()

            val startTime = startLdt.toFormattedTime()
            val endTime = endLdt.toFormattedTime()
            val boosterSeatsRequired = item.ride.orderedWaypoints
                .first()
                .passengers
                .fold(0) { sum, element -> sum + if (element.boosterSeat) 1 else 0 }
            val boosterDescription = when(boosterSeatsRequired) {
                0 -> ""
                else -> " • $boosterSeatsRequired booster"
            }.plus(if (boosterSeatsRequired > 1) "s" else "")
            val riders = item.ride.orderedWaypoints.maxOf { it.passengers.size }
            val boosterRiderDescription = "($riders rider"
                .plus(if (riders > 1) "s" else "")
                .plus(boosterDescription)
                .plus(")")
            val rideEstimate = "$${item.ride.estimatedEarningsCents.centsToDollarsAndCents()}"
            val waypoints = item.ride.orderedWaypoints
                .mapIndexed { index, orderedWaypoint ->
                    "${index + 1}. ${orderedWaypoint.location.address}"
                }
                .joinToString("\n")

            itemBinding.cardView.setOnClickListener { onClick(item.ride) }
            itemBinding.timeStartTV.text = startTime
            itemBinding.timeEndTV.text = endTime
            itemBinding.rideDescriptionTV.text = boosterRiderDescription
            itemBinding.rideEstimateTV.text = rideEstimate
            itemBinding.waypointsTV.text = waypoints
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