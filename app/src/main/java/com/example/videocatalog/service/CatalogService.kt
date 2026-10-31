package com.example.videocatalog.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Runs in the ":metadata" process (see AndroidManifest.xml).
 * Owns the folder filesDir/videos; nothing outside this class knows the path.
 *
 * TODO:
 *  - onCreate: copy every bundled file under assets/videos into filesDir/videos once (skip files that already exist).
 *  - Implement IVideoCatalog.Stub: listVideos() lists regular files in that folder in a stable order
 *    and calls NativeExtractor.extract(path) per file; per-file failures go into VideoInfo.error,
 *    never thrown across Binder.
 *  - onBind returns the Stub instance. Binder methods run on binder threads, not the main thread.
 * Docs: https://developer.android.com/develop/background-work/services/aidl#Implement
 */
class CatalogService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        // TODO: return the IVideoCatalog.Stub implementation
        return null
    }
}
