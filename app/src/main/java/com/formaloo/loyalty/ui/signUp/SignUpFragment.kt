package com.formaloo.loyalty.ui.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import com.formaloo.loyalty.MainActivity
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseFragment
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.common.Constants.EMAIL
import com.formaloo.loyalty.common.Constants.ERRORS
import com.formaloo.loyalty.common.Constants.FORM_ERRORS
import com.formaloo.loyalty.common.Constants.FULL_NAME
import com.formaloo.loyalty.common.Constants.GENERAL_ERRORS
import com.formaloo.loyalty.common.Constants.PHONE_NUMBER
import com.formaloo.loyalty.common.Failure
import com.formaloo.loyalty.ui.CustomerViewModel
import kotlinx.android.synthetic.main.fragment_signup.*
import org.json.JSONObject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 *  We provide only a database layer in the app that act as login/signup,
 *  but not really works on cloud.
 *  You should disconnect our login/signup system and connect your own.
 *  Our side is serverless.
 *  Everything happens in the app, the only back-end is APi connection to Formaloo CDP.
 */
class SignUpFragment : BaseFragment() {
    private val signUpVM: SignUpViewModel by viewModel()
    private val customerVM: CustomerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initView()

    }

    override fun getViewModel(): BaseViewModel = customerVM

    private fun initData() {
        //observe customerData After successful signUp
        customerVM.customerData.observe(viewLifecycleOwner, {
            it?.let {
                (activity as MainActivity).hideSignUpBtn()
            }
        })

        //After editText fields updates loginFormState be updated too
        signUpVM.loginFormState.observe(viewLifecycleOwner, {
            it?.let { loginState ->
                // disable login button unless email / password is valid and
                // fullname and (email or phone) isNotEmpty
                login.isEnabled = loginState.isDataValid

                if (loginState.emailError != null) {
                    email.error = getString(loginState.emailError)
                }
                if (loginState.passwordError != null) {
                    password.error = getString(loginState.passwordError)
                }

                if (loginState.isDataValid) {
                    login.setBackgroundColor(resources.getColor(R.color.teal_700))
                    login.setTextColor(resources.getColor(R.color.white))

                } else {
                    login.setBackgroundColor(resources.getColor(R.color.gray_light))
                    login.setTextColor(resources.getColor(R.color.gray))

                }
            }
        })

        //observe createCustomer failure response
        customerVM.failure.observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is Failure.FeatureFailure -> renderFailure(it.msgRes)
                    else -> {

                    }
                }
                hideLoading()
            }
        })
    }

    private fun initView() {
        email.doAfterTextChanged {
            checkFieldsData()
        }
        phone.doAfterTextChanged {
            checkFieldsData()
        }
        full_name.doAfterTextChanged {
            checkFieldsData()
        }

        password.apply {
            doAfterTextChanged {
                checkFieldsData()
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        checkFieldsData()
                    }

                }
                false
            }
        }

        //you have to attach your company signUp/In set up to use signUpForm.
        login.setOnClickListener {
            showLoading()
            //after user signUp we create new CDP customer.
            customerVM.login(
                full_name.text.toString(),
                phone.text.toString(),
                email.text.toString()
            )
        }

    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE

    }

    private fun hideLoading() {
        loading.visibility = View.GONE

    }

    private fun checkFieldsData() {
        signUpVM.loginDataChanged(
            full_name.text.toString(),
            phone.text.toString(),
            email.text.toString(),
            password.text.toString()
        )
    }

    private fun renderFailure(message: String?) {
        Timber.e("renderFailure $message")
        message?.let {
            try {
                val jObjError = JSONObject(message)
                setErrorsToViews(jObjError)
            } catch (e: Exception) {
                Timber.e("${e.localizedMessage}")

            }
        }
    }

    private fun setErrorsToViews(jObjError: JSONObject) {
        val errors = baseMethod.getJSONObject(jObjError, ERRORS)
        val formErrors = baseMethod.getJSONObject(errors, FORM_ERRORS)
        val gErrors = baseMethod.getJSONArray(jObjError, GENERAL_ERRORS)

        errorFind(formErrors)
        val retrieveGeneralErr = baseMethod.retrieveJSONArrayFirstItem(gErrors)
        if (retrieveGeneralErr.isNotEmpty()) {
            baseMethod.showMsg(sign_up_container, retrieveGeneralErr)
        }

    }

    private fun errorFind(it: JSONObject) {
        full_name.error =
            baseMethod.retrieveJSONArrayFirstItem(baseMethod.getJSONArray(it, FULL_NAME))
        email.error = baseMethod.retrieveJSONArrayFirstItem(baseMethod.getJSONArray(it, EMAIL))
        phone.error =
            baseMethod.retrieveJSONArrayFirstItem(baseMethod.getJSONArray(it, PHONE_NUMBER))

    }

}