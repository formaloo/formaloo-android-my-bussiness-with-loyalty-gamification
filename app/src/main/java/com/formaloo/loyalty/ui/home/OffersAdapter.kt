package com.formaloo.loyalty.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.formaloo.loyalty.R
import com.formaloo.loyalty.data.model.Offer

/**
 * Adapter for the [RecyclerView] in [HomeFragment].
 */
class OfferAdapter : ListAdapter<Offer, RecyclerView.ViewHolder>(OfferDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_offer, parent, false)

        return OfferViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val offer = getItem(position)
        (holder as OfferViewHolder).bind(offer)
    }

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(item: Offer) {
            if (!item.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.findViewById(R.id.offer_item_image))
            }
            itemView.findViewById<TextView>(R.id.offer_item_title).text = item.title ?: ""
            itemView.findViewById<TextView>(R.id.offer_item_desc).text = item.description ?: ""
            itemView.setOnClickListener {
                navigateToOffer(item, it)
            }

        }

        private fun navigateToOffer(offer: Offer, view: View) {
            val direction = HomeFragmentDirections.actionHomeFragmentToOfferFragment(offer)
            view.findNavController().navigate(direction)
        }

    }
}

private class OfferDiffCallback : DiffUtil.ItemCallback<Offer>() {

    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem == newItem
    }
}
