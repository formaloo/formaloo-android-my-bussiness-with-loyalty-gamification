package com.formaloo.loyalty.ui.deal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.formaloo.loyalty.R
import com.formaloo.loyalty.common.BaseFragment
import com.formaloo.loyalty.common.BaseViewModel
import com.formaloo.loyalty.data.model.Deal
import com.formaloo.loyalty.data.model.customer.Customer
import com.formaloo.loyalty.ui.CustomerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_deal.*
import kotlinx.android.synthetic.main.layout_deal_not_login.view.*
import net.glxn.qrgen.android.QRCode
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * In this page, every customer see their code, qr code (auto generate via their id), and their current score.
 */
class DealFragment : BaseFragment() {
    private var customerData: Customer? = null
    private val dealVM: DealViewModel by viewModel()
    private val customerVM: CustomerViewModel by viewModel()

    private lateinit var adapter: DealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_deal, container, false)


        return root
    }

    override fun getViewModel(): BaseViewModel = dealVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()

    }

    private fun initView() {
        customer_code.setImageBitmap(QRCode.from("www.google.com").bitmap())

        adapter = DealAdapter(object : DealsListener {
            override fun openDealLink(item: Deal) {
                if (item.link?.isNotEmpty() == true) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(item.link)
                    startActivity(intent)
                }

            }

            override fun getDeal(item: Deal) {
                customerData?.let { customer ->
                    val itemNeededScore: Int = (item.score_needed ?: 0)

                    customerVM.initCustomerCode(customer.code)
                    customerVM.editCustomerScore(itemNeededScore)

                }
            }

        })

        deals_list.adapter = adapter

        deal_not_login_lay.deal_login_btn.setOnClickListener {
            openSignUpPage(it)
        }
    }

    private fun openSignUpPage(view: View) {
        val direction = DealFragmentDirections.actionDealFragmentToSignUpFragment()
        view.findNavController().navigate(direction)

    }

    private fun initData() {
        //retrieve deals from database
        dealVM.getDeals()
        //retrieve customer data from database
        customerVM.retrieveCustomerData()

        customerVM.customerData.observe(viewLifecycleOwner, {
            it?.let {
                customerDataIsReady(it)
            }
        })

        customerVM.customerDBData.observe(viewLifecycleOwner, {
            if (it == null) {
                openLoginView()
            } else {
                it?.let {
                    retrieveCustomerData(it)
                    customerDataIsReady(it)
                    hideLoginView()

                }
            }

        })

        customerVM.customerDataEdit.observe(viewLifecycleOwner, {
            it?.let {
                //Fetch customer data to get customer's new score
                retrieveCustomerData(it)
                openCouponDialog(requireActivity())
            }
        })

        dealVM.deals.observe(viewLifecycleOwner, { it ->
            adapter.submitList(it)

        })

    }

    private fun hideLoginView() {
        deal_progress.visibility = View.GONE
        deal_not_login_lay.visibility = View.GONE
        deal_sv.visibility = View.VISIBLE

    }

    private fun openLoginView() {
        deal_progress.visibility = View.GONE
        deal_not_login_lay.visibility = View.VISIBLE
        deal_sv.visibility = View.GONE

    }

    private fun retrieveCustomerData(it: Customer) {
        /**getCustomer from CDP return 401 status only if authenticate your business.
         * or retrieve customer data from your own server
         * X_API_KEY is a WRITE_ONLY key due to security issues.
         */
        customerVM.initCustomerCode(it.code)
        customerVM.getCustomer()
    }

    private fun customerDataIsReady(it: Customer) {
        customerData = it
        fillViews(it)
    }

    private fun openCouponDialog(activity: FragmentActivity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("Coupon detail")
            .setMessage("COUPON_CODE")
            .setPositiveButton("Copy coupon") { _, _ -> }
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun fillViews(it: Customer) {
        customer_score.text = "${it.score ?: ""}"
        customer_code.setImageBitmap(QRCode.from(it.code).bitmap())

    }
}