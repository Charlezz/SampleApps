//
// Created by Charles on 26/04/2017.
//
#include <jni.h>
#include <string>

extern "C"
//charlezz.ndk_build
JNIEXPORT jstring JNICALL Java_charlezz_ndkbuild2_MainActivity_hello(JNIEnv *env, jclass obj) {
    std::string hello = "Hello from C++, NDK-BUILD2";
    return env->NewStringUTF(hello.c_str());
}



//
//
//#include <jni.h>
//#include <string>
//
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_charlezz_ndkbuild_MainActivity_stringFromJNI(
//        JNIEnv *env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}
