package com.example.mobiledev

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class WeatherResponse (

  @SerializedName("name"      ) var name      : String? = null,
  @SerializedName("temp"      ) var temp      : Int?    = null,
  @SerializedName("condition" ) var condition : String? = null

): Serializable