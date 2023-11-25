package com.example.telemedicineapp

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lepu.blepro.event.EventMsgConst
import com.lepu.blepro.event.InterfaceEvent
import com.lepu.blepro.ext.BleServiceHelper
import com.lepu.blepro.ext.bpm.RecordData
import com.lepu.blepro.objs.Bluetooth
import com.lepu.blepro.objs.BluetoothController

object BleMeasurements {
    fun scanForResult(context: ComponentActivity, onEvent: (MeasureEvent) -> Unit) {
        BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)

        LiveEventBus.get<Bluetooth>(EventMsgConst.Discovery.EventDeviceFound)
            .observe(context) {
                val bpm = BluetoothController.getDevices()[0]

                BleServiceHelper.BleServiceHelper.setInterfaces(bpm.model)
//        lifecycle.addObserver(BIOL(this, intArrayOf(bpm.model)))
                BleServiceHelper.BleServiceHelper.stopScan()
                BleServiceHelper.BleServiceHelper.connect(context, bpm.model, bpm.device)
            }

//        LiveEventBus.get<InterfaceEvent>(InterfaceEvent.BPM.EventBpmSyncTime)
//            .observe(context) {
//                BleServiceHelper.BleServiceHelper.bpmGetFileList(Bluetooth.MODEL_BPM)
//            }

        LiveEventBus.get<InterfaceEvent>(InterfaceEvent.BPM.EventBpmRecordData)
            .observe(context) {
                val data = it.data as RecordData
                if (data.sys * data.dia * data.pr == 0) return@observe;

                Log.d("rec data", data.toString())
//                onEvent(MeasureEvent.SetMeasurementData(123, 234, 345))
                onEvent(MeasureEvent.SetMeasurementData(data.sys, data.dia, data.pr, data.storeId))
            }
    }
}