package com.formaloo.loyalty.ui.deal

import com.formaloo.loyalty.data.model.Deal

interface DealsListener {
    fun openDealLink(item: Deal)
    fun getDeal(item: Deal)
}