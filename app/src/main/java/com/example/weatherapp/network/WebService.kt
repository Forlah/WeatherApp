package com.example.weatherapp.network

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

object WebService {
    interface API {

        @GET("weather")
        fun fetchTodayWeather(@Query("q") q: String,
                              @Query("appid") appid: String,
                              @Query("units") units: String): Single<TodayWeatherResponse>

        @GET("onecall")
        fun fetchWeeklyWeather(@Query("lat") lat: Float,
                               @Query("lon") lon: Float,
                               @Query( "appid") appId: String,
                               @Query("units") units: String,
                               @Query("exclude") exclude: String): Single<WeeklyWeatherResponse>

    }

    data class Weather(
            @SerializedName("id") val id: Int,
            @SerializedName("main") val main: String,
            @SerializedName("description") val description: String
    )

    data class Main(
            @SerializedName("temp") val temp: Float,
            @SerializedName("feels_like") val feelsLike: Float,
            @SerializedName("pressure") val pressure: Int,
            @SerializedName("humidity") val humidity: Int
    )

    data class Wind(
            @SerializedName("speed") val speed: Float,
            @SerializedName("degree") val degree: Int
    )

    data class TodayWeatherResponse(
            @SerializedName("name") val name: String,
            @SerializedName("weather") val weather: List<Weather>,
            @SerializedName("main") val main: Main,
            @SerializedName("wind") val wind: Wind,
            @SerializedName("dt") val timeStamp: Long
    )

    data class Current(
            @SerializedName("dt") val timeStamp: Long,
            @SerializedName("temp") val temperature: Float,
            @SerializedName("weather") val weather: List<Weather>
    )

    data class Temp(
            @SerializedName("min") val min: Float,
            @SerializedName("max") val max: Float,
            @SerializedName("weather") val weather: List<Weather>
    )

    data class Daily(
            @SerializedName("dt") val timeStamp: Long,
            @SerializedName("temp") val temp: Temp,
            @SerializedName("weather") val weather: List<Weather>
    )

    data class WeeklyWeatherResponse(
            @SerializedName("current") val current: Current,
            @SerializedName("timezone") val timezone: String,
            @SerializedName("daily") val daily: List<Daily>
    )
}

