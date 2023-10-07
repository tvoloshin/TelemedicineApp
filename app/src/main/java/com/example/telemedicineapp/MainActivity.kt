package com.example.telemedicineapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.telemedicineapp.ui.theme.TelemedicineAppTheme
import com.lepu.blepro.ext.BleServiceHelper
import com.lepu.blepro.objs.Bluetooth
import com.lepu.blepro.objs.BluetoothController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelemedicineAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }

    private fun needPermission() {
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
            registerForActivityResult(
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
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.BLUETOOTH)
            }
        }

        checkBt()
    }

    private fun checkBt() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
            return
        }

        if (!adapter.isEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    if (adapter.enable()) {
                        initService()
                        Toast.makeText(this, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (adapter.enable()) {
                    initService()
                    Toast.makeText(this, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            initService()
        }
    }

    private fun initService() {
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
            BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)
        }
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelemedicineAppTheme {
        Greeting()
    }
}
