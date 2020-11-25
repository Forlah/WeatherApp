package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.network.Resource
import com.example.weatherapp.network.Status
import com.example.weatherapp.network.WebService
import com.example.weatherapp.repository.WeatherRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    var todayWeatherResult: MutableLiveData<Resource<WebService.TodayWeatherResponse>> = MutableLiveData()
    var dailyWeatherResult: MutableLiveData<Resource<WebService.WeeklyWeatherResponse>> = MutableLiveData()

    fun getTodayWeather(){
        todayWeatherResult.value = Resource(Status.LOADING, null, null)
        val disposable = weatherRepository.getTodayWeather()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    todayWeatherResult.value = Resource(Status.SUCCESS, response, null)
                }, { e ->
                    todayWeatherResult.value = Resource(Status.ERROR, null, e.toString())
                })

        compositeDisposable.add(disposable)
    }

    fun getDailyWeather(){
        dailyWeatherResult.value = Resource(Status.LOADING, null, null)
        val disposable = weatherRepository.getWeeklyWeather()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    dailyWeatherResult.value = Resource(Status.SUCCESS, response, null)
                }, { e ->
                    dailyWeatherResult.value = Resource(Status.ERROR, null, e.toString())
                })

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}