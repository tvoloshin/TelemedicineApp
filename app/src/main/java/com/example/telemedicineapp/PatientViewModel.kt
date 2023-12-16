package com.example.telemedicineapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.telemedicineapp.data.Patient
import com.example.telemedicineapp.data.PatientDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PatientViewModel(
    private val dao: PatientDao
): ViewModel() {
    fun getPatients(): Flow<List<Patient>> {
        return dao.getPatients()
    }

    fun savePatients(patient: Patient) {
        viewModelScope.launch {
            dao.upsertPatient(patient)
        }
    }

//    //first state whether the search is happening or not
//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()
//
//    //second state the text typed by the user
//    private val _searchText = MutableStateFlow("")
//    val searchText = _searchText.asStateFlow()
//
//    //third state the list to be filtered
//    private val _patientsList = MutableStateFlow(getPatients())
//    val patientsList = searchText
//        .combine(_patientsList) { text, patients ->//combine searchText with _contriesList
//            if (text.isBlank()) { //return the entery list of countries if not is typed
//                patients
//            }
//            patients.filter { patient ->// filter and return a list of countries based on the text the user typed
//                patient.name.uppercase().contains(text.trim().uppercase())
//            }
//        }.stateIn(//basically convert the Flow returned from combine operator to StateFlow
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
//            initialValue = _patientsList.value
//        )
//
//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//    }
//
//    fun onToggleSearch() {
//        _isSearching.value = !_isSearching.value
//        if (!_isSearching.value) {
//            onSearchTextChange("")
//        }
//    }
}