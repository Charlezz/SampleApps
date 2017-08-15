//
// Created by Charles on 18/05/2017.
//

#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <string>
#include <android/log.h>
#include <VideoPlayerAPI.h>
#include <VideoPlayerManager.h>


extern "C" {
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "JNI_OnLoad");
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }
    return JNI_VERSION_1_6;
}

/**
 * VideoLibraryInterface.java
 */

JNIEXPORT bool JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_open(JNIEnv *env, jobject obj, jint videoId, jstring path) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "open");

    const char *nativeString = env->GetStringUTFChars(path, 0);
    bool result = PLAYER_API::open(videoId, nativeString);
    env->ReleaseStringUTFChars(path, nativeString);
    return result;
}

JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_setTexture(JNIEnv *env, jobject obj, jint videoId, jint textureId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "getMovieWidth");
    PLAYER_API::setTexture(videoId, textureId);
}

JNIEXPORT jint JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_getWidth(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "getWidth");
    return PLAYER_API::getWidth(videoId);
}
JNIEXPORT jint JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_getHeight(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "getHeight");
    return PLAYER_API::getHeight(videoId);
}

JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_start(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "start");
    VideoPlayerManager::getInstance()->start(videoId);
}
JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_update(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "start");
    VideoPlayerManager::getInstance()->update(videoId);
}
JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_pause(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "pause");
    VideoPlayerManager::getInstance()->pause(videoId);

}
JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_stop(JNIEnv *env, jobject obj, jint videoId) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "stop");
    VideoPlayerManager::getInstance()->stop(videoId);
}

JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoLibraryInterface_destroyAll(JNIEnv *env, jobject obj) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "destroyMovieAll");
    VideoPlayerManager::getInstance()->destroyAll();
}

/**
 * VideoGLRenderer.java
 */
JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoGLRenderer_initRendering(JNIEnv *env, jobject obj) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "initRendering");
}

JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoGLRenderer_updateRendering(JNIEnv *env, jobject obj, jint width, jint height) {
    __android_log_print(ANDROID_LOG_ERROR, "SampleApp", "updateRendering");
}
JNIEXPORT void JNICALL
Java_maxst_sampleapp_VideoGLRenderer_drawScene(JNIEnv *env, jobject obj) {
    __android_log_print(ANDROID_LOG_INFO, "SampleApp", "drawScene");

}

}
