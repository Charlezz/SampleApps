#include <jni.h>
#include <string>

extern "C"
jstring
Java_charlezz_nativemodule_NativeInterface_hiThere(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "from module";
    return env->NewStringUTF(hello.c_str());
}
