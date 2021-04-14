package com.formaloo.loyalty.data.remote.customer

import com.formaloo.loyalty.data.model.customer.CustomerDetailRes
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * you can access to customer doc here: https://docs.formaloo.com/v1.0/?python#formaloo-api-documentation-customers
 */
interface CustomerService {

    companion object {
        const val VERSION10 = "v1.0/"

        //Create a new customer on the your business.
        private const val CUSTOMERS_CREATE = "${VERSION10}customers/"
        //Get/PATCH a customer's data.
        private const val CUSTOMER_Detail = "${VERSION10}customers/{code}/"

    }

    @Multipart
    @POST(CUSTOMERS_CREATE)
    fun createCustomers(
        @PartMap req:  HashMap<String, RequestBody>?
    ): Call<CustomerDetailRes>

    @Multipart
    @PATCH(CUSTOMER_Detail)
    fun editCustomer(
        @Path("code") code: String,
        @PartMap req:  HashMap<String, RequestBody>?
    ): Call<CustomerDetailRes>

    /**getCustomer from CDP return 401 status only if authenticate your business.
     * or retrieve customer data from your own server
     * X_API_KEY is a WRITE_ONLY key due to security issues.
     */
    @GET(CUSTOMER_Detail)
    fun getCustomer(@Path("code") code: String, ): Call<CustomerDetailRes>

}