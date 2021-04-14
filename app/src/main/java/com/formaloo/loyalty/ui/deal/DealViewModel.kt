package com.formaloo.loyalty.ui.deal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.model.Deal
import com.formaloo.loyalty.data.repository.ClubRepository
import kotlinx.coroutines.launch


class DealViewModel(private val repository: ClubRepository) : BaseViewModel() {

    private lateinit var dealId: String

    private val _deals = MutableLiveData<List<Deal>>().apply { value = null }
    val deals: LiveData<List<Deal>> = _deals
    private val _deal = MutableLiveData<Deal>().apply { value = null }
    val deal: LiveData<Deal> = _deal


    fun getDeals() = launch {
        _deals.value = repository.getDeals()
    }

    fun getDealById() = launch {
        _deal.value = repository.getDealById(dealId)
    }

    fun initDealId(id: String) {
        dealId = id
    }
}