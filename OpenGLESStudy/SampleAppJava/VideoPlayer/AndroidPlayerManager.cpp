#define LOG_TAG "AndroidPlayerManager"

#include "AndroidPlayerManager.h"

using namespace std;

//namespace maxstARVideo {
    shared_ptr<VideoPlayerManager> VideoPlayerManager::getInstance() {
        if (instance == nullptr) {
            instance = shared_ptr<AndroidPlayerManager>(new AndroidPlayerManager());
        }

        return instance;
    }

    AndroidPlayerManager::AndroidPlayerManager(void) {
    }

    AndroidPlayerManager::~AndroidPlayerManager(void) {
    }

    void AndroidPlayerManager::setAndroidVariables(JavaVM *pJavaVM) {
        javaVM = pJavaVM;

        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        jclass tVideoPlayerManagerClass = env->FindClass("com/maxst/media/VideoPlayerManager");
        if (tVideoPlayerManagerClass == 0) {
            //Log("MoviePlayerManagerClass is null");
            return;
        }

        jmethodID tGetMoviePlayerManagerMethodld = env->GetStaticMethodID(tVideoPlayerManagerClass, "getInstance", "()Lcom/maxst/media/VideoPlayerManager;");
        if (tGetMoviePlayerManagerMethodld == 0) {
            //Log("GetVideoPlayerManagerMethodld is null");
            return;
        }

        jobject tVideoPlayerManagerObject = env->CallStaticObjectMethod(tVideoPlayerManagerClass, tGetMoviePlayerManagerMethodld);
        if (tVideoPlayerManagerObject == 0) {
//            Log("VideoPlayerManagerObject is null");
            return;
        }

        videoPlayerManagerObj = reinterpret_cast<jobject>(env->NewGlobalRef(tVideoPlayerManagerObject));

        tVideoPlayerManagerClass = env->GetObjectClass(videoPlayerManagerObj);
        if (tVideoPlayerManagerClass == 0) {
//            Log("VideoPlayerManager Class is null");
            return;
        }

        openMethodId = env->GetMethodID(tVideoPlayerManagerClass, "open", "(Ljava/lang/String;I)Z");
        if (openMethodId == 0) {
//            Log("openMethodId method is not found");
            return;
        }

        getWidthMethodId = env->GetMethodID(tVideoPlayerManagerClass, "getWidth", "(I)I");
        if (getWidthMethodId == 0) {
//            Log("getWidthMethodId method is not found");
            return;
        }

        getHeightMethodId = env->GetMethodID(tVideoPlayerManagerClass, "getHeight", "(I)I");
        if (getHeightMethodId == 0) {
//            Log("getHeightMethodId method is not found");
            return;
        }

        setTextureMethodId = env->GetMethodID(tVideoPlayerManagerClass, "setTexture", "(II)V");
        if (setTextureMethodId == 0) {
//            Log("setTextureMethodId method is not found");
            return;
        }

        startMethodId = env->GetMethodID(tVideoPlayerManagerClass, "start", "(I)V");
        if (startMethodId == 0) {
//            Log("startMethodId method is not found");
            return;
        }

        pauseMethodId = env->GetMethodID(tVideoPlayerManagerClass, "pause", "(I)V");
        if (pauseMethodId == 0) {
//            Log("pauseMethodId method is not found");
            return;
        }

        updateMethodId = env->GetMethodID(tVideoPlayerManagerClass, "update", "(I)V");
        if (updateMethodId == 0) {
//            Log("updateMethodId method is not found");
            return;
        }

        stopMethodId = env->GetMethodID(tVideoPlayerManagerClass, "stop", "(I)V");
        if (stopMethodId == 0) {
//            Log("stopMethodId method is not found");
            return;
        }

        destroyAllMethodId = env->GetMethodID(tVideoPlayerManagerClass, "destroyAll", "()V");
        if (destroyAllMethodId == 0) {
//            Log("destroyAllMethodId method is not found");
            return;
        }
    }

    bool AndroidPlayerManager::open(int videoId, string path) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return false;
        }

        return env->CallBooleanMethod(videoPlayerManagerObj, openMethodId, videoId, env->NewStringUTF(path.c_str()));
    }

    void AndroidPlayerManager::setTexture(int videoId, unsigned int textureId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        env->CallVoidMethod(videoPlayerManagerObj, setTextureMethodId, videoId, textureId);

    }

    int AndroidPlayerManager::getWidth(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return 0;
        }

        return env->CallIntMethod(videoPlayerManagerObj, getWidthMethodId, videoId);

    }

    int AndroidPlayerManager::getHeight(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return 0;
        }

        return env->CallIntMethod(videoPlayerManagerObj, getHeightMethodId, videoId);

    }

    void AndroidPlayerManager::start(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        env->CallVoidMethod(videoPlayerManagerObj, startMethodId, videoId);
    }


    void AndroidPlayerManager::update(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        env->CallVoidMethod(videoPlayerManagerObj, updateMethodId, videoId);
    }

    void AndroidPlayerManager::pause(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        env->CallVoidMethod(videoPlayerManagerObj, pauseMethodId, videoId);
    }

    void AndroidPlayerManager::stop(int videoId) {
        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

        env->CallVoidMethod(videoPlayerManagerObj, stopMethodId, videoId);

    }

    void AndroidPlayerManager::destroyAll() {

        JNIEnv *env;

        if (javaVM->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
//            Log("Failed to get JNIEnv");
            return;
        }

//        env->DeleteGlobalRef(videoPlayerManagerObj);
        env->CallVoidMethod(videoPlayerManagerObj, destroyAllMethodId);
    }
//}
