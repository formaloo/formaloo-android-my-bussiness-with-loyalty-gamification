package com.formaloo.loyalty.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseFragment
import com.formaloo.loyalty.common.BaseViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val homeVM: HomeViewModel by viewModel()
    private lateinit var adapter: OfferAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun getViewModel(): BaseViewModel = homeVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()

    }

    private fun initView() {
        adapter = OfferAdapter()
        offers_list.adapter = adapter

    }

    private fun initData() {
        homeVM.getOffers()
        homeVM.offers.observe(viewLifecycleOwner, { it ->
            it?.let { offers ->
                if (offers.isNotEmpty()) {
                    val offer = offers[0]
                    loadBanner(offer.imageUrl)

                }
            }

            adapter.submitList(it)

        })

    }

    private fun loadBanner(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(main_image)
        }

    }
}