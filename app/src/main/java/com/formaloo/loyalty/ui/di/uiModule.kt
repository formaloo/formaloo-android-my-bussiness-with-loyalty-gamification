package com.formaloo.loyalty.ui.di

import com.formaloo.loyalty.ui.CustomerViewModel
import com.formaloo.loyalty.ui.deal.DealViewModel
import com.formaloo.loyalty.ui.home.HomeViewModel
import com.formaloo.loyalty.ui.profile.ProfileViewModel
import com.formaloo.loyalty.ui.signUp.SignUpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val uiModule = module {
    viewModel { SignUpViewModel(get(named("CustomerRepositoryImpl"))) }
    viewModel { CustomerViewModel(get(named("CustomerRepositoryImpl"))) }
    viewModel { ProfileViewModel(get(named("CustomerRepositoryImpl"))) }
    viewModel { HomeViewModel(get(named("ClubRepositoryImpl"))) }
    viewModel { DealViewModel(get(named("ClubRepositoryImpl"))) }
}