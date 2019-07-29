package com.labs.sauti.di.module

import android.content.Context
import com.google.gson.Gson
import com.labs.sauti.R
import com.labs.sauti.api.SautiApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val READ_TIMEOUT = 30000L
        private const val CONNECT_TIMEOUT = 30000L
    }

    @Provides
    @Singleton
    fun provideSautiApiService(context: Context, gson: Gson) : SautiApiService {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.sauti_api_base_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync()) // createAsync. observer call async by default
            .client(okHttpClient)
            .build()

        return retrofit.create(SautiApiService::class.java)
    }
}