package com.example.telemedicineapp.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.telemedicineapp.data.Measure
import com.example.telemedicineapp.data.Patient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//    val url = "http://b253-85-143-112-90.ngrok-free.app"
//const val url = "http://192.168.0.108:8000"
const val url = "https://0462-85-143-112-242.ngrok-free.app"
//    val url = "http://10.164.5.106:8000"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun postDataUsingRetrofit(
    ctx: Context,
    measure: Measure,
    onSuccess: () -> Unit
) {
    val retrofitAPI = retrofit.create(MeasuresApi::class.java)
    val call: Call<Measure?>? = retrofitAPI.sendMeasure(measure)

    call!!.enqueue(object : Callback<Measure?> {
        override fun onResponse(call: Call<Measure?>, response: Response<Measure?>) {
            val model: Measure? = response.body()
            val resp = "Response Code : " + response.code() + "\n" + model.toString()
            Log.d("api", resp)
            if (response.code() == 200) {
                Toast.makeText(ctx, "Data posted to API", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(ctx, "Request failed with response status " + response.code(), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Measure?>, t: Throwable) {
            t.message?.let { Log.d("api fail", it) }
            Toast.makeText(ctx, "Request failed: " + t.message, Toast.LENGTH_SHORT).show()
//            result.value = "Error found is : " + t.message
        }
    })
}

fun getPatientsUsingRetrofit(
    ctx: Context,
    onSuccess: (List<Patient>?) -> Unit
) {
    val retrofitAPI = retrofit.create(PatientsApi::class.java)
    val call: Call<List<Patient>?>? = retrofitAPI.getPatients()

    call!!.enqueue(object : Callback<List<Patient>?> {
        override fun onResponse(call: Call<List<Patient>?>, response: Response<List<Patient>?>) {
            val patients: List<Patient>? = response.body()
            val resp = "Response Code : " + response.code() + "\n" + patients.toString()
            Log.d("api", resp)
            if (response.code() == 200) {
                Toast.makeText(ctx, "Data posted to API", Toast.LENGTH_SHORT).show()
                onSuccess(patients)
            } else {
                Toast.makeText(ctx, "Request failed with response status " + response.code(), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<List<Patient>?>, t: Throwable) {
            Toast.makeText(ctx, "Request failed: " + t.message, Toast.LENGTH_SHORT).show()
//            result.value = "Error found is : " + t.message
        }
    })
}