package com.example.telemedicineapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemedicineapp.data.Measure
import com.example.telemedicineapp.data.MeasureDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MeasureViewModel(
    private val dao: MeasureDao
): ViewModel() {

    private var lastMeasure = 0;

    private val _state = MutableStateFlow(MeasureState())
    private val _measures = dao.getMeasures().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//    val state: StateFlow<MeasureState> = _state.asStateFlow()
    val state: StateFlow<MeasureState> = combine(_state, _measures) { state, measures ->
        state.copy(
            measures = measures
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MeasureState())

    fun onEvent(event: MeasureEvent) {
        when(event) {
            is MeasureEvent.DeleteMeasure -> {
                viewModelScope.launch {
                    dao.deleteMeasure(event.measure)
                }
            }
            MeasureEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingMeasure = false
                ) }
            }
            MeasureEvent.SaveMeasure -> {
                val patientId = state.value.patientID
                val sys = state.value.sys
                val dia = state.value.dia
                val pr = state.value.pr

                Log.d("save", "$patientId, $sys, $dia, $pr")

                if (patientId == null || sys == null || dia == null || pr == null) {
                    return
                }

                val measure = Measure(
                    patientId = patientId,
                    sys = sys,
                    dia = dia,
                    pr = pr
                )
                viewModelScope.launch {
                    dao.upsertMeasure(measure)
                }
                _state.update { it.copy(
                    isAddingMeasure = false,
                    patientID = null,
                    sys = null,
                    dia = null,
                    pr = null
                ) }
            }
            is MeasureEvent.SetPatientId -> {
                _state.update { it.copy(
                    patientID = event.patientId
                ) }
            }
            MeasureEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingMeasure = true
                ) }
            }
            is MeasureEvent.SetMeasurementData -> {
                if (event.storeId > lastMeasure) {
                    lastMeasure = event.storeId;
                    _state.update {
                        it.copy(
                            sys = event.sys,
                            dia = event.dia,
                            pr = event.pr
                        )
                    }
                }
            }
            is MeasureEvent.SetSynced -> {
                viewModelScope.launch {
                    dao.setSynced(event.measureId)
                }
            }
        }
    }
}