package com.formaloo.loyalty.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.model.Offer
import com.formaloo.loyalty.data.repository.ClubRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ClubRepository) : BaseViewModel() {

    private lateinit var offerId: String

    private val _offers = MutableLiveData<List<Offer>>().apply { value = null }
    val offers: LiveData<List<Offer>> = _offers
    private val _offer = MutableLiveData<Offer>().apply { value = null }
    val offer: LiveData<Offer> = _offer


    fun getOffers() = launch {
        _offers.value = repository.getOffers()
    }

    fun getOfferById() = launch {
        _offer.value = repository.getOfferById(offerId)
    }

    fun initOfferId(id: String) {
        offerId = id
    }
}