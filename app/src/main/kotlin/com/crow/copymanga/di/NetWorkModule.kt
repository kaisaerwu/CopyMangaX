package com.crow.copymanga.di

import com.crow.base.current_project.BaseStrings
import com.crow.base.tools.extensions.baseMoshi
import com.crow.base.tools.network.FlowCallAdapterFactory
import com.crow.copymanga.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: app/src/main/java/com/ssti/laser/app/module
 * @Time: 2022/5/29 1:48
 * @Author: CrowForKotlin
 * @Description: NetWorkModule
 * @formatter:off
 **************************/

val netWorkModule = module {

    single {
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().also { interceptor ->
                interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            addInterceptor(Interceptor { chain: Interceptor.Chain ->
                chain.proceed(chain.request().newBuilder()
                    .addHeader("User-Agent", "Dart/2.16 (dart:io)")
                    .addHeader("Platform", "1")
                    .addHeader("region", "0")
                    .build()
                )
            })
            pingInterval(10, TimeUnit.SECONDS)
            connectTimeout(20, TimeUnit.SECONDS)
            callTimeout(15, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            retryOnConnectionFailure(false)
            proxy(null)
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BaseStrings.URL.CopyManga)
            .client(get())
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(baseMoshi))
            .build()
    }

    single (qualifier = named("Custom")) {
        Retrofit.Builder()
            .baseUrl("https://www.baidu.com/")
            .build()
    }

}