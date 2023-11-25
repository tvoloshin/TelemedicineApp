package com.example.telemedicineapp.api

import com.example.telemedicineapp.data.Measure
import com.example.telemedicineapp.data.Patient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PatientsApi {
    @Headers(
        "Accept: application/json"
    )
    @POST("patients/")
    fun getPatients(): Call<List<Patient>?>?
}