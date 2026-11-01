package com.example.videocatalog.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.example.videocatalog.IVideoCatalog
import com.example.videocatalog.VideoInfo
import java.io.File

class CatalogService : Service() {

    private val videosDir by lazy { File(filesDir, "videos") }
    private val samplesReady by lazy {
        copyAssetsOnce()
        true
    }

    // AIDL methods below run on the service's Binder thread pool, not the main thread, and may run
    // concurrently; only onCreate/onBind/onDestroy run on the main thread. `lazy` is synchronized, so
    // the one-time asset copy happens once, on the first caller's Binder thread, and other callers wait.
    // https://developer.android.com/develop/background-work/services/aidl#Defining
    private val binder = object : IVideoCatalog.Stub() {
        override fun listVideos(): List<VideoInfo> =
            if (!samplesReady) emptyList() else videosDir.listFiles().orEmpty()
                .filter { it.isFile && !it.name.endsWith(".tmp") }
                .sortedBy { it.name }
                .map { file ->
                    try {
                        NativeExtractor.extractVideoMetaData(file.absolutePath)
                    } catch (t: Throwable) {
                        VideoInfo(fileName = file.name, error = t.message ?: "Extraction failed")
                    }
                }

        override fun getServicePid(): Int = Process.myPid()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun copyAssetsOnce() {
        videosDir.mkdirs()
        assets.list("videos").orEmpty().forEach { name ->
            val target = File(videosDir, name)
            if (target.exists()) return@forEach
            val tmp = File(videosDir, "$name.tmp")
            assets.open("videos/$name").use { input ->
                tmp.outputStream().use { output -> input.copyTo(output) }
            }
            if (!tmp.renameTo(target)) tmp.delete()
        }
    }
}
