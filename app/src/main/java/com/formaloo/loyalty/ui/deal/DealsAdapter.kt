package com.formaloo.loyalty.ui.deal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.formaloo.loyalty.R
import com.formaloo.loyalty.data.model.Deal

/**
 * Adapter for the [RecyclerView] in [DealFragment].
 */
class DealAdapter(private val listener: DealsListener) :
    ListAdapter<Deal, RecyclerView.ViewHolder>(DealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_deals, parent, false)

        return DealViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val deal = getItem(position)
        (holder as DealViewHolder).bind(deal, listener)
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Deal, listener: DealsListener) {
            if (!item.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.findViewById(R.id.deal_item_image))
            }
            itemView.findViewById<TextView>(R.id.deal_item_title).text = item.title ?: ""
            itemView.findViewById<TextView>(R.id.deal_item_score_needed).text =
                "${item.score_needed} ${itemView.context.getString(R.string.score)}" ?: ""
            itemView.findViewById<TextView>(R.id.deal_item_desc).text = item.description ?: ""
            itemView.findViewById<Button>(R.id.get_deal_btn).setOnClickListener {
                listener.getDeal(item)

            }

            itemView.findViewById<Button>(R.id.deal_more_btn).setOnClickListener {
                listener.openDealLink(item)
            }


            itemView.setOnClickListener {
                navigateToDeal(item, it)
            }

        }

        private fun navigateToDeal(deal: Deal, view: View) {
//            val direction = DealFragmentDirections.actionDealFragmentToDealDetailFragment(deal)
//            view.findNavController().navigate(direction)
        }

    }
}

private class DealDiffCallback : DiffUtil.ItemCallback<Deal>() {

    override fun areItemsTheSame(oldItem: Deal, newItem: Deal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Deal, newItem: Deal): Boolean {
        return oldItem == newItem
    }
}
