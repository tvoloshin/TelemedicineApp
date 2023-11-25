package com.example.telemedicineapp.api

import com.example.telemedicineapp.data.Measure
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MeasuresApi {
    @Headers(
        "Accept: application/json"
    )
    @POST("measures/")
    fun sendMeasure(@Body measure: Measure?): Call<Measure?>?
}