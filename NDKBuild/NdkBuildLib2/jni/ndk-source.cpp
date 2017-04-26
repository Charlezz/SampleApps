//
// Created by Charles on 26/04/2017.
//
#include <jni.h>
#include <string>

extern "C"
//charlezz.ndk_build
JNIEXPORT jstring JNICALL Java_charlezz_ndkbuildlibtwo_NativeManager_hello(JNIEnv *env, jclass obj) {
    std::string hello = "Hello from NdkBuildLib2";
    return env->NewStringUTF(hello.c_str());
}


