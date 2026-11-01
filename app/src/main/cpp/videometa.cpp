#include <jni.h>
#include <android/log.h>
#include <media/NdkMediaExtractor.h>
#include <media/NdkMediaFormat.h>

#include <cstring>
#include <string>
#include <string_view>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <cerrno>

#define LOG_TAG "videometa"

static jobject errorObject(JNIEnv *env, jclass cls, jmethodID ctor,
                           const char *name, const std::string &message) {
    jstring jname = env->NewStringUTF(name);
    jstring jerr = env->NewStringUTF(message.c_str());
    jobject result = env->NewObject(cls, ctor,
                                    jname, (jlong) -1, (jint) -1, (jint) -1,
                                    (jstring) nullptr, (jboolean) JNI_FALSE, jerr);
    env->DeleteLocalRef(jname);
    env->DeleteLocalRef(jerr);
    return result;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_example_videocatalog_service_NativeExtractor_extractVideoMetaData(
        JNIEnv *env, jobject /* thiz */, jstring jpath) {

    const char *path = env->GetStringUTFChars(jpath, nullptr);
    if (path == nullptr) return nullptr;

    const char *slash = strrchr(path, '/');
    const char *name = slash ? slash + 1 : path;

    jclass cls = env->FindClass("com/example/videocatalog/VideoInfo");
    if (cls == nullptr) {
        env->ReleaseStringUTFChars(jpath, path);
        return nullptr;
    }

    jmethodID ctor = env->GetMethodID(cls, "<init>","(Ljava/lang/String;JIILjava/lang/String;ZLjava/lang/String;)V");
    if (ctor == nullptr) {
        env->ReleaseStringUTFChars(jpath, path);
        return nullptr;
    }

    int fd = open(path, O_RDONLY);
    struct stat st{};
    if (fd < 0 || fstat(fd, &st) != 0) {
        __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "%s: open failed (errno %d)", name, errno);
        jobject err = errorObject(env, cls, ctor, name, "Cannot open file");
        if (fd >= 0) close(fd);
        env->ReleaseStringUTFChars(jpath, path);
        return err;
    }

    AMediaExtractor *extractor = AMediaExtractor_new();
    if (extractor == nullptr) {
        jobject err = errorObject(env, cls, ctor, name, "Cannot create extractor");
        close(fd);
        env->ReleaseStringUTFChars(jpath, path);
        return err;
    }
    media_status_t status = AMediaExtractor_setDataSourceFd(extractor, fd, 0, st.st_size);
    close(fd);
    if (status != AMEDIA_OK) {
        __android_log_print(ANDROID_LOG_WARN, LOG_TAG, "%s: setDataSourceFd failed (%d)", name, status);
        jobject err = errorObject(env, cls, ctor, name,
                                  "Unreadable or unsupported file (status " + std::to_string(status) + ")");
        AMediaExtractor_delete(extractor);
        env->ReleaseStringUTFChars(jpath, path);
        return err;
    }

    std::string videoMime;
    int64_t durationUs = -1;
    int32_t width = -1, height = -1;
    bool hasVideo = false, hasAudio = false;

    size_t count = AMediaExtractor_getTrackCount(extractor);
    for (size_t i = 0; i < count; i++) {
        AMediaFormat *fmt = AMediaExtractor_getTrackFormat(extractor, i);
        const char *mime = nullptr;
        if (AMediaFormat_getString(fmt, AMEDIAFORMAT_KEY_MIME, &mime) && mime != nullptr) {
            std::string_view type(mime);
            if (!hasVideo && type.starts_with("video/")) {
                hasVideo = true;
                videoMime = mime;
                AMediaFormat_getInt32(fmt, AMEDIAFORMAT_KEY_WIDTH, &width);
                AMediaFormat_getInt32(fmt, AMEDIAFORMAT_KEY_HEIGHT, &height);
                AMediaFormat_getInt64(fmt, AMEDIAFORMAT_KEY_DURATION, &durationUs);
            } else if (type.starts_with("audio/")) {
                hasAudio = true;
            }
        }
        AMediaFormat_delete(fmt);
    }
    AMediaExtractor_delete(extractor);

    if (!hasVideo) {
        jobject err = errorObject(env, cls, ctor, name, "No video track");
        env->ReleaseStringUTFChars(jpath, path);
        return err;
    }

    jstring jname = env->NewStringUTF(name);
    jstring jmime = env->NewStringUTF(videoMime.c_str());
    env->ReleaseStringUTFChars(jpath, path);

    jobject result = env->NewObject(cls, ctor,
                                    jname, (jlong) durationUs, (jint) width, (jint) height, jmime,
                                    (jboolean) (hasAudio ? JNI_TRUE : JNI_FALSE), (jstring) nullptr);

    env->DeleteLocalRef(jname);
    env->DeleteLocalRef(jmime);
    if (env->ExceptionCheck()) return nullptr;
    return result;
}
