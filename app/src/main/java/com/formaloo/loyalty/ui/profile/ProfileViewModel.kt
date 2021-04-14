package com.formaloo.loyalty.ui.profile

import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.repository.CustomerRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: CustomerRepository) : BaseViewModel() {

    fun logout() = launch {
        repository.clearRoom()
        repository.clearAllSharedPref()
    }

}