package com.example.weatherapp.repository

import com.example.weatherapp.network.WebService
import com.example.weatherapp.utils.Constant.API_KEY
import io.reactivex.Single
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WebService.API) {

    fun getTodayWeather(): Single<WebService.TodayWeatherResponse> {
        return api.fetchTodayWeather(q = "Lagos", appid = API_KEY, units = "metric")
                .doAfterSuccess {

                }
    }

    fun getWeeklyWeather(): Single<WebService.WeeklyWeatherResponse>{
        return api.fetchWeeklyWeather(lat = 6.45306F, lon = 3.39583F, appId = API_KEY, units = "metric", exclude = "minutely,hourly,alerts")
                .doAfterSuccess {

                }
    }
}