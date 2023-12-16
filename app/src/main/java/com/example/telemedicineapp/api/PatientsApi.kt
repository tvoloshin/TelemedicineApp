package com.example.telemedicineapp.api

import com.example.telemedicineapp.data.Patient
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface PatientsApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("patients/")
    fun getPatients(): Call<List<Patient>?>?
}