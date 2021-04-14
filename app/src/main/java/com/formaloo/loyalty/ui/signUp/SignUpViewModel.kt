package com.formaloo.loyalty.ui.signUp

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.model.LoginFormState
import com.formaloo.loyalty.data.repository.CustomerRepository

class SignUpViewModel(private val repository: CustomerRepository) : BaseViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun loginDataChanged(name: String, phone: String, email: String, password: String) {
        if (name.isEmpty() || (phone.isEmpty() && email.isEmpty())) {

        } else if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }


}