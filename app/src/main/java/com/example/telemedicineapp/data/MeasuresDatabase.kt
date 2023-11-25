package com.example.telemedicineapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Measure::class, Patient::class], version = 4)
abstract class MeasuresDatabase: RoomDatabase() {
    abstract val measureDao: MeasureDao
    abstract val patientDao: PatientDao
}