package com.example.telemedicineapp

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.telemedicineapp.api.postDataUsingRetrofit
import com.lepu.blepro.ext.BleServiceHelper
import com.lepu.blepro.objs.Bluetooth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasuresScreen(
    navController: NavController,
    state: MeasureState,
    onEvent: (MeasureEvent) -> Unit,
    newMeasureFor: Int
) {
    val mContext = LocalContext.current
    BleMeasurements.scanForResult(mContext as ComponentActivity, onEvent)
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = {
                        state.measures.filter { !it.synced }.map {
                            postDataUsingRetrofit(mContext, it) {
                                onEvent(MeasureEvent.SetSynced(it.id))
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Sync measurement"
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        BleServiceHelper.BleServiceHelper.bpmGetFileList(Bluetooth.MODEL_BPM)
//                BleMeasurements.scanForResult(mContext as ComponentActivity, onEvent)
                        onEvent(MeasureEvent.ShowDialog)
                        navController.navigate("patients")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add measure"
                        )
                    }
                },
            )
        },
        ) { _ ->
        Log.d("asd", "newMeasureFor = $newMeasureFor")
        if(newMeasureFor != 0) {
            onEvent(MeasureEvent.SetPatientId(newMeasureFor))
            AddMeasureDialog(navController = navController, state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Log.d("qwe", state.measures.toString())
            items(state.measures) { measure ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Patient ${measure.patientId}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "sys: ${measure.sys}, dia: ${measure.dia}, sys: ${measure.pr}",
                            fontSize = 12.sp
                        )
                    }
                    if (!measure.synced) {
                        Icon(
                            modifier = Modifier.fillMaxHeight(),
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Not saved"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Saved"
                        )
                    }
                    IconButton(onClick = {
                        onEvent(MeasureEvent.DeleteMeasure(measure))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete measurement"
                        )
                    }
                }
            }
        }
    }
}