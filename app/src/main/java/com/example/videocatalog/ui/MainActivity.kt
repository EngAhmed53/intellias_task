package com.example.videocatalog.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text

/**
 * Client side (default process).
 *
 * TODO:
 *  - Hold a ServiceConnection; bind to CatalogService in onStart (explicit Intent, Context.BIND_AUTO_CREATE),
 *    unbind in onStop. Handle onServiceDisconnected (clear the stub, show error / allow retry).
 *  - In onServiceConnected: IVideoCatalog.Stub.asInterface(binder).
 *  - Call listVideos() OFF the main thread (lifecycleScope.launch(Dispatchers.IO)); catch RemoteException.
 *  - UI state: loading / empty / error / list. Format durationUs as mm:ss and "WxH".
 *  - Refresh action re-runs the call.
 * Docs: https://developer.android.com/develop/background-work/services/bound-services#Binding
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // TODO: replace with the catalog screen (LazyColumn of VideoInfo rows + states).
            Text("TODO: video catalog")
        }
    }
}
