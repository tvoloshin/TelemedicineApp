package com.example.telemedicineapp

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telemedicineapp.api.getPatientsUsingRetrofit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsList(
    navController: NavController,
    viewModel: PatientViewModel
) {
    var selectedItem by remember{mutableStateOf( -1)}
    val ctx = LocalContext.current
    val patients = viewModel.getPatients().collectAsState(initial = emptyList())

//    val searchText by viewModel.searchText.collectAsState()
//    val isSearching by viewModel.isSearching.collectAsState()
//    val patientsList by viewModel.patientsList.collectAsState()

    Scaffold(
//        topBar = {
//            SearchBar(
//                query = searchText,//text showed on SearchBar
//                onQueryChange = viewModel::onSearchTextChange, //update the value of searchText
//                onSearch = viewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
//                active = isSearching, //whether the user is searching or not
//                onActiveChange = { viewModel.onToggleSearch() }, //the callback to be invoked when this search bar's active state is changed
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {}
//        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                getPatientsUsingRetrofit(ctx) { patients ->
                    patients?.map { viewModel.savePatients(it) }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Add measure"
                )
            }
        }
    ) { _ ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            items(patientsList.value) {
            items(patients.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedItem == it.id,
                            onClick = {
                                Log.d("patients list", "main?newMeasureFor=${it.id}")
                                navController.navigate("main?newMeasureFor=${it.id}")
                            }
                        )
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = it.name,
                            fontSize = 20.sp
                        )
                        //                    Text(
                        //                        text = "sys: ${measure.sys}, dia: ${measure.dia}, sys: ${measure.pr}",
                        //                        fontSize = 12.sp
                        //                    )
                    }
                }
            }
        }
    }
}
