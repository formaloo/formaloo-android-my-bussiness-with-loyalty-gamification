package com.formaloo.loyalty.data.remote.customer.di

import com.formaloo.loyalty.data.remote.customer.CustomerDataSource
import com.formaloo.loyalty.data.remote.customer.CustomerService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun remoteCustomerModule(baseUrl: String, appToken: String) = module {
    /**
     * To access API token you need an account on https://cdp.formaloo.net/.
        After sign in to your account go to connection tab and copy the provided key.
     */

    factory(named("customerInterceptor")) {
        Interceptor { chain ->
            val original = chain.request()
            val request: Request
            request = original.newBuilder()
                .header("x-api-key", appToken)
                .method(original.method, original.body)
                .build()


            chain.proceed(request)
        }
    }

    single(named("customerClient")) {
        OkHttpClient.Builder()
            .addInterceptor(get(named("customerInterceptor")) as Interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    factory(named("customerRetrofit")) {
        Retrofit.Builder()
            .client(get(named("customerClient")))
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory(named("customerService")) {
        get<Retrofit>(named("customerRetrofit")).create(CustomerService::class.java)
    }

    factory(named("CustomerDataSource")) {
        CustomerDataSource(get(named("customerService")))
    }

}


