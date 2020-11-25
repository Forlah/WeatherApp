package com.example.weatherapp.network

data class Resource<out T>(val status: Status, val data: T?, val message: String?)
