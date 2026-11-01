#include <jni.h>
#include <android/log.h>
#include <media/NdkMediaExtractor.h>
#include <media/NdkMediaFormat.h>

#define LOG_TAG "videometa"

#include <jni.h>
#include <cstring>

extern "C" JNIEXPORT jobject JNICALL
Java_com_example_videocatalog_service_NativeExtractor_extractVideoMetaData(
        JNIEnv* env, jobject /* thiz */, jstring jpath) {

    const char* path = env->GetStringUTFChars(jpath, nullptr);
    if (path == nullptr) return nullptr;

    const char* slash = strrchr(path, '/');
    const char* name = slash ? slash + 1 : path;

    jclass cls = env->FindClass("com/example/videocatalog/VideoInfo");
    if (cls == nullptr) { env->ReleaseStringUTFChars(jpath, path); return nullptr; }

    jmethodID ctor = env->GetMethodID(cls, "<init>",
                                      "(Ljava/lang/String;JIILjava/lang/String;ZLjava/lang/String;)V");
    if (ctor == nullptr) { env->ReleaseStringUTFChars(jpath, path); return nullptr; }

    jstring jname = env->NewStringUTF(name);
    jstring jmime = env->NewStringUTF("video/mock");
    env->ReleaseStringUTFChars(jpath, path);

    jobject result = env->NewObject(cls, ctor,
                                    jname, (jlong) 5000000, (jint) 1280, (jint) 720, jmime, (jboolean) JNI_TRUE, (jstring) nullptr);

    env->DeleteLocalRef(jname);
    env->DeleteLocalRef(jmime);
    return result;
}