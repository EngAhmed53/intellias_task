package com.example.videocatalog;

// Structured parcelable: fields are listed HERE and AGP generates the Java class.
// There is NO hand-written Kotlin/Java VideoInfo class.
// Docs: https://developer.android.com/develop/background-work/services/aidl#PassingObjects
//       https://source.android.com/docs/core/architecture/aidl/aidl-language#structured-parcelables
parcelable VideoInfo {
    // TODO: fileName, durationUs, width, height, videoMime, hasAudio, error.
    //       Decide nullability of the String fields (generated Java fields are nullable) and document it.
}
