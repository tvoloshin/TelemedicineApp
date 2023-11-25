package com.example.telemedicineapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM Patient")
    fun getPatients(): List<Patient>

    @Upsert
    suspend fun upsertPatient(patient: Patient)
}