package com.example.telemedicineapp

import android.Manifest
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.SparseArray
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.lepu.blepro.ext.BleServiceHelper
import com.lepu.blepro.objs.Bluetooth
import com.lepu.blepro.objs.BluetoothController

fun needPermission(activity: ComponentActivity) {
    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    //            PermissionX.init(this)
    //                .permissions(
    //                    Manifest.permission.BLUETOOTH_SCAN,
    //                    Manifest.permission.BLUETOOTH_CONNECT,
    //                    Manifest.permission.BLUETOOTH_ADVERTISE,
    ////                    Manifest.permission.ACCESS_FINE_LOCATION,
    ////                    Manifest.permission.ACCESS_COARSE_LOCATION,
    //                )
    //                .onExplainRequestReason { scope, deniedList ->
    //                    scope.showRequestReasonDialog(
    //                        deniedList, "location permission", "ok", "ignore"
    //                    )
    //                }
    //                .onForwardToSettings { scope, deniedList ->
    //                    scope.showForwardToSettingsDialog(
    //                        deniedList, "location setting", "ok", "ignore"
    //                    )
    //                }
    //                .request { allGranted, grantedList, deniedList ->
    //                    Log.d(TAG, "permission : $allGranted, $grantedList, $deniedList")
    //
    //                    //permission OK, check Bluetooth status
    //                    if (allGranted)
    //                        checkBt()
    //                }
    //        } else {
    //            PermissionX.init(this)
    //                .permissions(
    ////                    Manifest.permission.ACCESS_COARSE_LOCATION,
    ////                    Manifest.permission.ACCESS_FINE_LOCATION,
    //                    Manifest.permission.BLUETOOTH,
    //                    Manifest.permission.BLUETOOTH_ADMIN
    //                )
    //                .onExplainRequestReason { scope, deniedList ->
    //                    scope.showRequestReasonDialog(
    //                        deniedList, "location permission", "ok", "ignore"
    //                    )
    //                }
    //                .onForwardToSettings { scope, deniedList ->
    //                    scope.showForwardToSettingsDialog(
    //                        deniedList, "location setting", "ok", "ignore"
    //                    )
    //                }
    //                .request { allGranted, grantedList, deniedList ->
    //                    Log.d(TAG, "permission : $allGranted, $grantedList, $deniedList")
    //
    //                    //permission OK, check Bluetooth status
    //                    if (allGranted)
    //                        checkBt()
    //                }
    //        }
    val requestPermissionLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    when {
        ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED -> {
            // You can use the API that requires the permission.
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                Manifest.permission.BLUETOOTH_SCAN)
        }
    }

    checkBt(activity)
}

private fun checkBt(activity: ComponentActivity) {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    if (adapter == null) {
        Toast.makeText(activity, "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
        return
    }

    if (!adapter.isEnabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                if (adapter.enable()) {
                    initService(activity.application)
                    Toast.makeText(activity, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (adapter.enable()) {
                initService(activity.application)
                Toast.makeText(activity, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        initService(activity.application)
    }
}

private fun initService(application: Application) {
    if (BleServiceHelper.BleServiceHelper.checkService()) {
        // BleService already init
        BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)
    } else {
        // Save the original file path. Er1, VBeat and HHM1 are currently supported
        val rawFolders = SparseArray<String>()
        //            rawFolders.set(Bluetooth.MODEL_ER1, "${getExternalFilesDir(null)?.absolutePath}/er1")
        //            rawFolders.set(Bluetooth.MODEL_ER1_N, "${getExternalFilesDir(null)?.absolutePath}/vbeat")
        //            rawFolders.set(Bluetooth.MODEL_HHM1, "${getExternalFilesDir(null)?.absolutePath}/hhm1")

        BleServiceHelper.BleServiceHelper.initRawFolder(rawFolders).initService(application)
//        BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)
    }
}


@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val mContext = LocalContext.current
    Button(onClick = {
//        Toast.makeText(mContext, BluetoothController.getDevices().toString(), Toast.LENGTH_SHORT).show()
        val bpm = BluetoothController.getDevices()[0]

        BleServiceHelper.BleServiceHelper.setInterfaces(bpm.model)
//        lifecycle.addObserver(BIOL(this, intArrayOf(bpm.model)))
        BleServiceHelper.BleServiceHelper.stopScan()
        BleServiceHelper.BleServiceHelper.connect(mContext, bpm.model, bpm.device)
    }) {
        Text(text = "Scan")
    }
}