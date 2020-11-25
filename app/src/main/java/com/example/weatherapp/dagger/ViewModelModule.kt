package com.example.weatherapp.dagger

import androidx.lifecycle.ViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel::class)
    abstract fun bindTodayWeatherViewModel(weatherViewModel: WeatherViewModel): ViewModel
}