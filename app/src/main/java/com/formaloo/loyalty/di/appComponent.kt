package com.formaloo.loyalty.di

import com.formaloo.loyalty.BuildConfig.*
import com.formaloo.loyalty.data.local.di.localModule
import com.formaloo.loyalty.data.remote.customer.di.remoteCustomerModule
import com.formaloo.loyalty.data.repository.di.repositoryModule
import com.formaloo.loyalty.ui.di.uiModule

/**
 * list of [Module]. each module contains components
 */
val appComponent = listOf(
    localModule,
    repositoryModule,
    remoteCustomerModule(BASE_URL, X_API_KEY),
    uiModule
)