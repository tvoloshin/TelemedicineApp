package com.example.telemedicineapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)