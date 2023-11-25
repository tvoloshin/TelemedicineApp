package com.example.telemedicineapp.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.example.telemedicineapp.data.Measure
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


data class DataModel(
    var name: String,
    var job: String
)

fun postDataUsingRetrofit(
    ctx: Context,
    measure: Measure,
    onSuccess: () -> Unit
//    userName: MutableState<TextFieldValue>,
//    job: MutableState<TextFieldValue>,
//    result: MutableState<String>
) {
    val url = "https://b685-85-143-112-84.ngrok-free.app"
//    val url = "http://localhost:8000"
//    val url = "http://192.168.0.108:8000"
//    val url = "http://10.164.5.106:8000"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create(MeasuresApi::class.java)
    val dataModel = DataModel(measure.pr.toString(), measure.sys.toString())
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
            Toast.makeText(ctx, "Request failed: " + t.message, Toast.LENGTH_SHORT).show()
//            result.value = "Error found is : " + t.message
        }
    })

}