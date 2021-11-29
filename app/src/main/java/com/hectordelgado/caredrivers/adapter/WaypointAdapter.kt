package com.hectordelgado.caredrivers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hectordelgado.caredrivers.R
import com.hectordelgado.caredrivers.databinding.ItemWaypointBinding
import com.hectordelgado.caredrivers.model.OrderedWaypoint

/**
 * Class responsible for inflating and binding views in a RecyclerView with [OrderedWaypoint] items.
 * @param list The list of elements this adapter will display.
 */
class WaypointAdapter(private val list: List<OrderedWaypoint>)
    : RecyclerView.Adapter<WaypointAdapter.WaypointVH>() {
    class WaypointVH(private val itemBinding: ItemWaypointBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: OrderedWaypoint) {
            val waypointIcon = if (item.anchor) R.drawable.diamond else R.drawable.circle
            val pickupDropOff = if (item.anchor) "Pickup" else "Drop-off"
            val address = item.location.address

            itemBinding.waypointIV.setImageResource(waypointIcon)
            itemBinding.pickupDropOffTV.text = pickupDropOff
            itemBinding.addressTV.text = address
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaypointVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemWaypointBinding.inflate(layoutInflater, parent, false)
        return WaypointVH(itemBinding)
    }

    override fun onBindViewHolder(holder: WaypointVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}