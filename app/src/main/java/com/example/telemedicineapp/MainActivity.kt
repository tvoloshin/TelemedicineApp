package com.example.telemedicineapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.telemedicineapp.data.MeasuresDatabase
import com.example.telemedicineapp.ui.theme.TelemedicineAppTheme
import com.lepu.blepro.ext.BleServiceHelper
import com.lepu.blepro.objs.Bluetooth

class MainActivity : ComponentActivity() {

    private val shownData = MutableLiveData<String>()

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MeasuresDatabase::class.java,
            "measures.db"
        ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

    private val measureViewModel by viewModels<MeasureViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MeasureViewModel(db.measureDao) as T
                }
            }
        }
    )

    private val patientViewModel by viewModels<PatientViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PatientViewModel(db.patientDao) as T
                }
            }
        }
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        needPermission(this)
        setContent {
            TelemedicineAppTheme {
//                val state by viewModel.state.collectAsState()
//                MeasuresScreen(state = state, onEvent = viewModel::onEvent)
                Navigation(measureViewModel = measureViewModel, patientViewModel = patientViewModel)
            }
        }
    }

//    private fun needPermission() {
//    //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//    //            PermissionX.init(this)
//    //                .permissions(
//    //                    Manifest.permission.BLUETOOTH_SCAN,
//    //                    Manifest.permission.BLUETOOTH_CONNECT,
//    //                    Manifest.permission.BLUETOOTH_ADVERTISE,
//    ////                    Manifest.permission.ACCESS_FINE_LOCATION,
//    ////                    Manifest.permission.ACCESS_COARSE_LOCATION,
//    //                )
//    //                .onExplainRequestReason { scope, deniedList ->
//    //                    scope.showRequestReasonDialog(
//    //                        deniedList, "location permission", "ok", "ignore"
//    //                    )
//    //                }
//    //                .onForwardToSettings { scope, deniedList ->
//    //                    scope.showForwardToSettingsDialog(
//    //                        deniedList, "location setting", "ok", "ignore"
//    //                    )
//    //                }
//    //                .request { allGranted, grantedList, deniedList ->
//    //                    Log.d(TAG, "permission : $allGranted, $grantedList, $deniedList")
//    //
//    //                    //permission OK, check Bluetooth status
//    //                    if (allGranted)
//    //                        checkBt()
//    //                }
//    //        } else {
//    //            PermissionX.init(this)
//    //                .permissions(
//    ////                    Manifest.permission.ACCESS_COARSE_LOCATION,
//    ////                    Manifest.permission.ACCESS_FINE_LOCATION,
//    //                    Manifest.permission.BLUETOOTH,
//    //                    Manifest.permission.BLUETOOTH_ADMIN
//    //                )
//    //                .onExplainRequestReason { scope, deniedList ->
//    //                    scope.showRequestReasonDialog(
//    //                        deniedList, "location permission", "ok", "ignore"
//    //                    )
//    //                }
//    //                .onForwardToSettings { scope, deniedList ->
//    //                    scope.showForwardToSettingsDialog(
//    //                        deniedList, "location setting", "ok", "ignore"
//    //                    )
//    //                }
//    //                .request { allGranted, grantedList, deniedList ->
//    //                    Log.d(TAG, "permission : $allGranted, $grantedList, $deniedList")
//    //
//    //                    //permission OK, check Bluetooth status
//    //                    if (allGranted)
//    //                        checkBt()
//    //                }
//    //        }
//        val requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                if (isGranted) {
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    // Explain to the user that the feature is unavailable because the
//                    // feature requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            }
//
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.BLUETOOTH
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//            }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissionLauncher.launch(
//                    Manifest.permission.BLUETOOTH)
//            }
//        }
//
//        checkBt()
//    }
//
//    private fun checkBt() {
//        val adapter = BluetoothAdapter.getDefaultAdapter()
//        if (adapter == null) {
//            Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (!adapter.isEnabled) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
//                    if (adapter.enable()) {
//                        initService()
//                        Toast.makeText(this, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                if (adapter.enable()) {
//                    initService()
//                    Toast.makeText(this, "Bluetooth open successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Bluetooth open failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } else {
//            initService()
//        }
//    }
//
//    private fun initService() {
//        if (BleServiceHelper.BleServiceHelper.checkService()) {
//            // BleService already init
//            BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)
//        } else {
//            // Save the original file path. Er1, VBeat and HHM1 are currently supported
//            val rawFolders = SparseArray<String>()
//    //            rawFolders.set(Bluetooth.MODEL_ER1, "${getExternalFilesDir(null)?.absolutePath}/er1")
//    //            rawFolders.set(Bluetooth.MODEL_ER1_N, "${getExternalFilesDir(null)?.absolutePath}/vbeat")
//    //            rawFolders.set(Bluetooth.MODEL_HHM1, "${getExternalFilesDir(null)?.absolutePath}/hhm1")
//
//            BleServiceHelper.BleServiceHelper.initRawFolder(rawFolders).initService(application)
//            BleServiceHelper.BleServiceHelper.startScan(Bluetooth.MODEL_BPM)
//        }
//    }
}

//@Composable
//fun Greeting(modifier: Modifier = Modifier) {
//    val mContext = LocalContext.current
//    Button(onClick = {
////        Toast.makeText(mContext, BluetoothController.getDevices().toString(), Toast.LENGTH_SHORT).show()
//        val bpm = BluetoothController.getDevices()[0]
//
//        BleServiceHelper.BleServiceHelper.setInterfaces(bpm.model)
////        lifecycle.addObserver(BIOL(this, intArrayOf(bpm.model)))
//        BleServiceHelper.BleServiceHelper.stopScan()
//        BleServiceHelper.BleServiceHelper.connect(mContext, bpm.model, bpm.device)
//    }) {
//        Text(text = "Scan")
//    }
//}

@Composable
fun Navigation(patientViewModel: PatientViewModel, measureViewModel: MeasureViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable(
            "main?newMeasureFor={newMeasureFor}",
            arguments = listOf(navArgument("newMeasureFor") { defaultValue = 0 })
        ) {
            val state by measureViewModel.state.collectAsState()
            MeasuresScreen(
                navController = navController,
                state = state,
                onEvent = measureViewModel::onEvent,
                it.arguments!!.getInt("newMeasureFor")
            )
        }
        composable("patients") {
//            val state by patientViewModel.state.collectAsState()
            PatientsList(navController = navController, viewModel = patientViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BpmMeasures(shownData: LiveData<String>, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "BPM",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
//            bottomBar = {
//                BottomAppBar{
//                    IconButton(onClick = {  }) { Icon(Icons.Filled.Menu, contentDescription = "Меню")}
//                    Spacer(Modifier.weight(1f, true))
//                    IconButton(onClick = {  }) { Icon(Icons.Filled.Search, contentDescription = "Поиск")}
//                }
//            },
        floatingActionButton = {
            FloatingActionButton(
                content = { Icon(Icons.Filled.Refresh, contentDescription = "Обновить") },
                onClick = { BleServiceHelper.BleServiceHelper.bpmGetFileList(Bluetooth.MODEL_BPM) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            val text: String? by shownData.observeAsState()
            text?.let { Text(text = it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelemedicineAppTheme {
        BpmMeasures(MutableLiveData())
    }
}
