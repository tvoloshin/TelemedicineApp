package com.example.telemedicineapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeasureDialog(
    navController: NavController,
    state: MeasureState,
    onEvent: (MeasureEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(MeasureEvent.HideDialog)
        },
        title = { Text(text = "Add measurement") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.patientID?.toString() ?: "",
                    onValueChange = {
//                        onEvent(MeasureEvent.SetPatientId(it.toIntOrNull() ?: state.patientID))
                    },
                    placeholder = {
                        Text(text = "Patient ID")
                    }
                )
                Text(
                    text = "${state.sys}/${state.dia}, pr=${state.pr}"
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(MeasureEvent.SaveMeasure)
                    navController.navigate("main")
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}