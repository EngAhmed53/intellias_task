package com.example.videocatalog.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.os.RemoteException
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.videocatalog.IVideoCatalog
import com.example.videocatalog.service.CatalogService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Client side (default process).
 */
class MainActivity : ComponentActivity() {

    private var catalogService: IVideoCatalog? = null
    private var bound = false
    private var uiState by mutableStateOf<UiState>(UiState.Loading)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            catalogService = IVideoCatalog.Stub.asInterface(binder)
            load()
        }
        override fun onServiceDisconnected(name: ComponentName) {
            catalogService = null
            uiState = UiState.Error("Service disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogScreen(
                state = uiState,
                onRefresh = ::load,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        bound = bindService(
            Intent(this, CatalogService::class.java),
            connection,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
        catalogService = null
    }

    private fun load() {
        val catalog = catalogService ?: run {
            uiState = UiState.Error("Not connected")
            return
        }
        uiState = UiState.Loading
        lifecycleScope.launch {
            uiState = try {
                UiState.Loaded(withContext(Dispatchers.IO) {
                    Log.i(TAG, "client pid = ${Process.myPid()}, service pid = ${catalog.servicePid}")
                    catalog.listVideos() ?: emptyList()
                })
            } catch (e: RemoteException) {
                UiState.Error("Service unavailable: ${e.message}")
            }
        }
    }

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }
}
