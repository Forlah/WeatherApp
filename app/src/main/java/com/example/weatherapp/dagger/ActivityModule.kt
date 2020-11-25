package com.example.weatherapp.dagger

import android.app.Application
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.network.WebService
import com.example.weatherapp.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
internal class AppModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideWeatherService(okHttpClient: OkHttpClient): WebService.API {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WebService.API::class.java)
    }

    @Singleton
    @Provides
    fun provideApplication(weatherApplication: WeatherApplication): Application {
        return weatherApplication
    }

    companion object {
        private val REQUEST_TIMEOUT = 120
    }

}