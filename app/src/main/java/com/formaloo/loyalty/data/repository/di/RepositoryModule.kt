package com.formaloo.loyalty.data.repository.di

import com.formaloo.loyalty.data.repository.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    factory(named("CustomerRepositoryImpl")) {
        CustomerRepositoryImpl(
            get(named("CustomerDataSource")),
            get(),
            get()
        ) as CustomerRepository
    }

    factory(named("ClubRepositoryImpl")) {
        ClubRepositoryImpl(
            get(),
            get()
        ) as ClubRepository
    }
}