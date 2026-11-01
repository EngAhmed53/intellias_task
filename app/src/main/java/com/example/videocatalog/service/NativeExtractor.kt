package com.example.videocatalog.service

import com.example.videocatalog.VideoInfo

/**
 * Only referenced from CatalogService, so libvideometa.so is loaded only in the ":metadata" process.
 */
object NativeExtractor {
    init {
        System.loadLibrary("videometa")
    }

    external fun extractVideoMetaData(path: String): VideoInfo
}
