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

    private val binder = object : IVideoCatalog.Stub() {
        override fun listVideos(): List<VideoInfo> =
            videosDir.listFiles().orEmpty()
                .filter { it.isFile }
                .sortedBy { it.name }
                .map { file ->
                    try {
                        NativeExtractor.extractVideoMetaData(file.absolutePath)
                    } catch (t: Throwable) {
                        VideoInfo(fileName = file.name, error = t.message ?: "Extraction failed")
                    }
                }

        override fun getSerivcePID(): Int = Process.myPid()
    }

    override fun onCreate() {
        super.onCreate()
        copyAssetsOnce()
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
