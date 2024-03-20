package com.example.mobiledev

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {
    @GET("api/v1/weather/{city}")
    fun getWeather(@Path("city") city: String): Call<WeatherResponse>
}
