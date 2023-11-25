package com.example.telemedicineapp

import com.example.telemedicineapp.data.Measure

data class MeasureState(
    val measures: List<Measure> = emptyList(),
    val patientID: Int? = null,
    val sys: Int? = 0,
    val dia: Int? = 0,
    val pr: Int? = 0,
    val isAddingMeasure: Boolean = false
)
