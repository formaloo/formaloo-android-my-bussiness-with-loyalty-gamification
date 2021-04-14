package com.formaloo.loyalty.ui.offerDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseFragment
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_offer.*
import org.koin.android.viewmodel.ext.android.viewModel


class OfferFragment : BaseFragment() {
    private val homeVM: HomeViewModel by viewModel()

    private val args: OfferFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_offer, container, false)
    }

    override fun getViewModel(): BaseViewModel = homeVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {

        args.offer?.let { item ->
            loadBanner(item.imageUrl)
            offer_description.text = item.description ?: ""
            offer_title.text = item.title ?: ""
        }

    }

    private fun loadBanner(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(detail_image)
        }
    }

}