package com.formaloo.loyalty.data.remote.customer

import okhttp3.RequestBody

/**
 * Implementation of [CustomerService] interface
 */
class CustomerDataSource(private val service: CustomerService) {

    fun createCustomers(body: HashMap<String, RequestBody>?) = service.createCustomers(body)
    fun editCustomer(code: String, req: HashMap<String, RequestBody>?) =
        service.editCustomer(code, req)

    fun getCustomer(code: String) = service.getCustomer(code)

}