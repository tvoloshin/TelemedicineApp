package com.example.telemedicineapp

import com.example.telemedicineapp.data.Measure

sealed interface MeasureEvent {
    object SaveMeasure: MeasureEvent
    data class SetPatientId(val patientId: Int?): MeasureEvent
    object ShowDialog: MeasureEvent
    object HideDialog: MeasureEvent
    data class DeleteMeasure(val measure: Measure): MeasureEvent
    data class SetMeasurementData(val sys: Int, val dia: Int, val pr: Int, val storeId: Int): MeasureEvent
    data class SetSynced(val measureId: Int): MeasureEvent
}