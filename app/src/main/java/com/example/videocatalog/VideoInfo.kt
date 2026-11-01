package com.example.videocatalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoInfo(
    val fileName: String,
    val durationUs: Long = -1,
    val width: Int = -1,
    val height: Int = -1,
    val videoMime: String? = null,
    val hasAudio: Boolean = false,
    val error: String? = null
) : Parcelable
