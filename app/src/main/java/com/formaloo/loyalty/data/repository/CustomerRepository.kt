package com.formaloo.loyalty.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.formaloo.loyalty.common.Constants
import com.formaloo.loyalty.common.Either
import com.formaloo.loyalty.common.Failure
import com.formaloo.loyalty.common.ViewFailure
import com.formaloo.loyalty.data.local.dao.CustomerDao
import com.formaloo.loyalty.data.model.customer.Customer
import com.formaloo.loyalty.data.model.customer.CustomerDetailRes
import com.formaloo.loyalty.data.remote.customer.CustomerDataSource
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.net.UnknownHostException

interface CustomerRepository {

    suspend fun createCustomer(
        body: HashMap<String, RequestBody>?
    ): Either<Failure, CustomerDetailRes>

    suspend fun saveCustomerData(customer: Customer)
    suspend fun clearRoom()
    suspend fun retriveProfileDb(code: String): Customer?
    fun saveCustomerCode(code: String)
    fun retrieveCustomerCode(): String?
    fun clearAllSharedPref()
    suspend fun editCustomer(
        code: String,
        body: HashMap<String, RequestBody>?
    ): Either<Failure, CustomerDetailRes>

    suspend fun getCustomer(code: String): Either<Failure, CustomerDetailRes>
}

class CustomerRepositoryImpl(
    private val source: CustomerDataSource,
    private val dao: CustomerDao,
    private val sharedPreferences: SharedPreferences
) : CustomerRepository {


    override suspend fun createCustomer(body: HashMap<String, RequestBody>?): Either<Failure, CustomerDetailRes> {
        Timber.e("createCustomer")
        val call = source.createCustomers(body)
        return try {
            request(call, { it.toCustomerDetailRes() }, CustomerDetailRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }


    }

    override suspend fun editCustomer(
        code: String,
        body: HashMap<String, RequestBody>?
    ): Either<Failure, CustomerDetailRes> {
        Timber.e("editCustomer")
        val call = source.editCustomer(code, body)
        return try {
            request(call, { it.toCustomerDetailRes() }, CustomerDetailRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }


    }

    override suspend fun getCustomer(code: String): Either<Failure, CustomerDetailRes> {
        Timber.e("getCustomer")
        val call = source.getCustomer(code)
        return try {
            request(call, { it.toCustomerDetailRes() }, CustomerDetailRes.empty())
        } catch (e: Exception) {
            Either.Left(Failure.Exception)
        }


    }

    override suspend fun saveCustomerData(customer: Customer) {
        dao.save(customer)
    }

    override suspend fun clearRoom() {
        dao.deleteAllFromTable()
    }

    override suspend fun retriveProfileDb(code: String): Customer? {
        return dao.getCustomer(code)
    }

    override fun saveCustomerCode(code: String) {
        sharedPreferences.edit().putString(Constants.PREFERENCES_CUSTOMER_Code, code).apply()
    }

    override fun retrieveCustomerCode(): String? {
        return sharedPreferences.getString(Constants.PREFERENCES_CUSTOMER_Code, null)

    }

    override fun clearAllSharedPref() {
        sharedPreferences.edit().clear().apply()
    }

    @SuppressLint("LogNotTimber")
    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            Timber.e("raw ${response.raw()}")
            Timber.e("body ${response.body()}")
            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Timber.e("Repo responseErrorBody jObjError-> $jObjError")
            } catch (e: Exception) {

            }

            when (response.code()) {
                200 -> Either.Right(transform((response.body() ?: default)))
                201 -> Either.Right(transform((response.body() ?: default)))
                400 -> Either.Left(ViewFailure.responseError("$jObjError"))
                401 -> Either.Left(Failure.UNAUTHORIZED_Error)
                500 -> Either.Left(Failure.ServerError)
                else -> Either.Left(ViewFailure.responseError("$jObjError"))
            }

        } catch (exception: Throwable) {
            Timber.e("exception $exception")
            if (exception is UnknownHostException) {
                Either.Left(Failure.NetworkConnection)

            } else {
                Either.Left(ViewFailure.responseError("exception++>  $exception"))

            }
        }

    }
}