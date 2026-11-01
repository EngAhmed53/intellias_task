package com.example.videocatalog.service

import com.example.videocatalog.VideoInfo

/**
 * JNI bridge. Only referenced from CatalogService, so libvideometa.so is loaded only in the
 * ":metadata" process (the class is never touched by the UI process).
 *
 * TODO: declare `external fun extract(path: String): VideoInfo`
 *   - VideoInfo is the AIDL-generated class in package com.example.videocatalog (import it).
 *   - The matching C symbol is Java_com_example_videocatalog_service_NativeExtractor_extract
 *     (JNIEnv*, jobject thiz, jstring path) -> jobject.
 * Docs: https://developer.android.com/training/articles/perf-jni
 */
object NativeExtractor {
    init {
        System.loadLibrary("videometa")
    }

    external fun extractVideoMetaData(path: String): VideoInfo
}
