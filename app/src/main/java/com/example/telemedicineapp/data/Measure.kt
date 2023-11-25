package com.example.telemedicineapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("patientId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Measure(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: Int,
    val sys: Int,
    val dia: Int,
    val pr: Int,
    val synced: Boolean = false
)
