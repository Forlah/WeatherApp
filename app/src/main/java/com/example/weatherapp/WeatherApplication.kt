package com.example.weatherapp

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.weatherapp.dagger.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class WeatherApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
       return  dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

}
val Activity.app get() = application as WeatherApplication

val AndroidViewModel.app get() = getApplication<WeatherApplication>()