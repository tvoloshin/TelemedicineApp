package com.example.telemedicineapp

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telemedicineapp.api.getDataUsingRetrofit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsList(
    navController: NavController,
    viewModel: PatientViewModel
) {
    var selectedItem by remember{mutableStateOf( -1)}
    val ctx = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        getDataUsingRetrofit(ctx) { patients ->
            patients?.map { viewModel.savePatients(it) }
        }
        items(viewModel.getPatients()) {
            Row(
                modifier = Modifier.fillMaxWidth().selectable(
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
