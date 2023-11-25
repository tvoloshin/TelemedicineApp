package com.example.telemedicineapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemedicineapp.data.Measure
import com.example.telemedicineapp.data.MeasureDao
import com.example.telemedicineapp.data.Patient
import com.example.telemedicineapp.data.PatientDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientViewModel(
    private val dao: PatientDao
): ViewModel() {
    fun getPatients(): List<Patient> {
        return dao.getPatients()
    }
}