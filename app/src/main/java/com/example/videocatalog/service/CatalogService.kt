package com.example.videocatalog.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.example.videocatalog.IVideoCatalog
import com.example.videocatalog.VideoInfo

/**
 * Runs in the ":metadata" process (see AndroidManifest.xml).
 * Owns the folder filesDir/videos; nothing outside this class knows the path.
 */
class CatalogService : Service() {

    private val binder = object : IVideoCatalog.Stub() {
        override fun listVideos(): List<VideoInfo> = listOf(
            VideoInfo(
                fileName = "h264_aac_720p.mp4",
                durationUs = 5_000_000,
                width = 1280, height = 720,
                videoMime = "video/avc",
                hasAudio = true
            ),
            VideoInfo(
                fileName = "hevc_noaudio_1080p.mp4",
                durationUs = 3_000_000,
                width = 1920, height = 1080,
                videoMime = "video/hevc",
                hasAudio = false
            ),
            VideoInfo(
                fileName = "vp9_opus_360p.webm",
                durationUs = 4_000_000,
                width = 640, height = 360,
                videoMime = "video/x-vnd.on2.vp9",
                hasAudio = true
            ),
            VideoInfo(
                fileName = "audio_only.m4a",
                hasAudio = true,
                error = "No video track"
            ),
            VideoInfo(
                fileName = "broken.mp4",
                error = "Unsupported or corrupt file"
            )
        )

        override fun getSerivcePID(): Int = Process.myPid()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}
