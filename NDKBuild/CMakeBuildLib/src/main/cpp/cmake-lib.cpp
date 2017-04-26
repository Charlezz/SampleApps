#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_charlezz_cmakebuildlib_NativeManager_hello(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++, CMakeBuildLib";
    return env->NewStringUTF(hello.c_str());
}
