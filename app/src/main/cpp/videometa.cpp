#include <jni.h>
#include <android/log.h>
#include <media/NdkMediaExtractor.h>
#include <media/NdkMediaFormat.h>

#define LOG_TAG "videometa"

// TODO: JNI entry point
//   extern "C" JNIEXPORT jobject JNICALL
//   Java_com_example_videocatalog_service_NativeExtractor_extract(JNIEnv* env, jobject thiz, jstring jpath)
//
// Steps (NDK media API: https://developer.android.com/ndk/reference/group/media):
//   1. GetStringUTFChars(jpath) -> const char* path; release it when done.
//   2. AMediaExtractor_new(); AMediaExtractor_setDataSource(ex, path); check the media_status_t.
//   3. AMediaExtractor_getTrackCount(); for each track AMediaExtractor_getTrackFormat(ex, i):
//        - AMediaFormat_getString(fmt, AMEDIAFORMAT_KEY_MIME, &mime)
//        - first "video/" track (lowest index) -> width/height (AMEDIAFORMAT_KEY_WIDTH/HEIGHT),
//          duration (AMEDIAFORMAT_KEY_DURATION, may be absent), copy the mime string BEFORE AMediaFormat_delete
//        - any "audio/" track -> hasAudio = true (keep scanning all tracks)
//        - AMediaFormat_delete(fmt) for every track format
//   4. AMediaExtractor_delete(ex).
//   5. Build the Java VideoInfo: FindClass("com/example/videocatalog/VideoInfo"), GetMethodID("<init>", "()V"),
//      NewObject, then SetXxxField via GetFieldID. Check every lookup for null and ExceptionCheck.
//      No video track or setDataSource failure -> set the `error` field instead of throwing.
//   6. Delete local refs created in loops (NewStringUTF etc.).
// JNI tips: https://developer.android.com/training/articles/perf-jni
