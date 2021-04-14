package com.formaloo.loyalty.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.formaloo.loyalty.MainActivity
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseFragment
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.model.customer.Customer
import com.formaloo.loyalty.ui.CustomerViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment() {

    private val customerVM: CustomerViewModel by viewModel()
    private val profileVM: ProfileViewModel by viewModel()
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun getViewModel(): BaseViewModel = profileVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideSignUpBtn()

        initView()

        initData()
    }

    private fun initData() {
        customerVM.retrieveCustomerData()
        customerVM.customerData.observe(viewLifecycleOwner, {
            it?.let {
                fillViews(it)
            }
        })

    }

    private fun initView() {
        args.customer?.let {
            fillViews(it)
        }
        logout_btn.setOnClickListener {
            profileVM.logout()
            (activity as MainActivity).showSignUpBtn()
            findNavController().popBackStack()
        }
    }

    private fun fillViews(customer: Customer) {
        with(customer) {
            full_name_txv.text = full_name ?: getString(R.string.prompt_full_name)
            email_txv.text = email ?: getString(R.string.prompt_email)
            score_txv.text = "$score" ?: getString(R.string.score)
            phone_txv.text = phone_number ?: getString(R.string.prompt_phone)
        }
    }
}