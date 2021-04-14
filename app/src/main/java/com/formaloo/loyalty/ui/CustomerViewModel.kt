package com.formaloo.loyalty.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.common.Constants
import com.formaloo.loyalty.data.model.customer.Customer
import com.formaloo.loyalty.data.model.customer.CustomerDetailRes
import com.formaloo.loyalty.data.repository.CustomerRepository
import com.formaloo.loyalty.ui.signUp.SignUpFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.util.*

class CustomerViewModel(private val repository: CustomerRepository) : BaseViewModel() {

    private val _customerData = MutableLiveData<Customer>().apply { value = null }
    val customerData: LiveData<Customer> = _customerData
   private val _customerDBData = MutableLiveData<Customer>().apply { value = null }
    val customerDBData: LiveData<Customer> = _customerDBData
    private val _customerDataEdit = MutableLiveData<Customer>().apply { value = null }
    val customerDataEdit: LiveData<Customer> = _customerDataEdit

    val _isLoading = MutableLiveData<Boolean>().apply { value = null }
    val isLoading: LiveData<Boolean> = _isLoading

    val _customerCode = MutableLiveData<String>().apply { value = null }
    val customerSlug: LiveData<String> = _customerCode

    private val jsonParams: HashMap<String, RequestBody> = HashMap()
    private lateinit var customerCode: String

    private fun createCustomer() = launch {
        val result = withContext(Dispatchers.IO) {
            repository.createCustomer(jsonParams)
        }
        result.either(::handleFailure, ::handleCreateCustomerData)
    }

    private fun handleCreateCustomerData(res: CustomerDetailRes?) {
        res?.let { it ->
            it.data?.let {
                _isLoading.value = false

                it.customer?.let { customer ->
                    _customerData.value = customer

                    saveCustomerDataToDB(customer)
                    navigateToProfilePage(customer)

                }

            }
        }

    }

    private fun editCustomer() = launch {
        val result = withContext(Dispatchers.IO) {
            repository.editCustomer(customerCode, jsonParams)
        }
        result.either(::handleFailure, ::handleEditCustomerData)
    }

    private fun handleEditCustomerData(res: CustomerDetailRes?) {
        res?.let { it ->
            it.data?.let {
                _isLoading.value = false

                it.customer?.let { customer ->
                    _customerDataEdit.value = customer

                    saveCustomerDataToDB(customer)

                }

            }
        }

    }

    /**getCustomer from CDP return 401 status only if authenticate your business.
     * or retrieve customer data from your own server
     * X_API_KEY is a WRITE_ONLY key due to security issues.
     */
     fun getCustomer() = launch {
        val result = withContext(Dispatchers.IO) {
            repository.getCustomer(customerCode)
        }
        result.either(::handleFailure, ::handleCustomerData)
    }


    private fun handleCustomerData(res: CustomerDetailRes?) {
        res?.let { it ->
            it.data?.let {
                _isLoading.value = false

                it.customer?.let { customer ->
                    _customerData.value = customer
                    saveCustomerDataToDB(customer)

                }

            }
        }

    }

    private fun navigateToProfilePage(customer: Customer?) =
        navigate(SignUpFragmentDirections.actionSignUpFragmentToProfileFragment(customer))

    private fun saveCustomerDataToDB(customer: Customer) = launch {
        repository.saveCustomerData(customer)
        repository.saveCustomerCode(customer.code)
    }

    fun retrieveCustomerCode() {
        _customerCode.value = repository.retrieveCustomerCode()

    }

    fun retrieveCustomerData() = launch {
        repository.retrieveCustomerCode()?.let { customerCode ->
            _customerDBData.value = repository.retriveProfileDb(customerCode)

        }
    }

    fun login(name: String, phone: String, email: String) {
        jsonParams["full_name"] = RequestBody.create(MultipartBody.FORM, name)
        jsonParams["email"] = RequestBody.create(MultipartBody.FORM, email)
        jsonParams["phone_number"] = RequestBody.create(MultipartBody.FORM, phone)
        jsonParams["score"] =
            RequestBody.create(MultipartBody.FORM, Constants.INITIAL_CUSTOMER_SCORE)

        //full_name & (phone or email) is required to create new customer
        //score is optional
        createCustomer()

    }

    fun stopLoading() {
        _isLoading.value = false
    }

    fun initCustomerCode(it: String) {
        customerCode = it
    }

    fun editCustomerScore(score: Int) {
        Timber.e("customer.score $score")

        jsonParams["score"] = RequestBody.create(MultipartBody.FORM, "-$score")

        editCustomer()
    }

}