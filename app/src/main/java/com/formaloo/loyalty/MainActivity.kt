package com.formaloo.loyalty

import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.formaloo.loyalty.common.BaseMethod
import com.formaloo.loyalty.ui.CustomerViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivity : AppCompatActivity(), KoinComponent {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val customerVM: CustomerViewModel by viewModel()
    val baseMethod: BaseMethod by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initData()

    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_signup,
                R.id.nav_profile,
                R.id.nav_about
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_home -> {
                }
                R.id.offer_detail_fragment -> {
                }
                R.id.nav_signup -> {

                }
                R.id.nav_profile -> {

                }
                R.id.nav_about -> {

                }
            }
        }
        nav_view.setupWithNavController(navController)

        showSignUpBtn()
    }

    private fun initData() {
        //check user exists
        customerVM.retrieveCustomerCode()
        //observe user exists
        customerVM.customerSlug.observe(this, {
            it?.let { customerSlug ->
                hideSignUpBtn()
            }
        })

    }

    fun showSignUpBtn() {
        //Menu items changed after user logout
        nav_view.menu.findItem(R.id.nav_profile).isVisible = false
        nav_view.menu.findItem(R.id.nav_signup).isVisible = true

    }

    fun hideSignUpBtn() {
        //Menu items changed after new user login
        nav_view.menu.findItem(R.id.nav_profile).isVisible = true
        nav_view.menu.findItem(R.id.nav_signup).isVisible = false

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        try {
            return super.dispatchTouchEvent(ev)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

}