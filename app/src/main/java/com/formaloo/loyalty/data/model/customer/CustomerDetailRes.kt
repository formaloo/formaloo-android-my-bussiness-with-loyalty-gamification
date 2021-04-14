package com.formaloo.loyalty.data.model.customer

import java.io.Serializable

data class CustomerDetailRes(
    var status: Int? = null,
    var data: CustomerDetailData? = null
) : Serializable {
    companion object {
        fun empty() = CustomerDetailRes(0, null)
    }

    fun toCustomerDetailRes() = CustomerDetailRes(status, data)
}