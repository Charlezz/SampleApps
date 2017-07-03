#pragma once

#include <jni.h>
#include "VideoPlayerManager.h"

class AndroidPlayerManager : public VideoPlayerManager
{
    public:
    AndroidPlayerManager(void);

    ~AndroidPlayerManager(void);

    void setAndroidVariables(JavaVM *pJavaVM);

    bool open(int videoId, string path);

    void setTexture(int videoId, unsigned int textureId);

    int getWidth(int videoId);

    int getHeight(int videoId);

    void start(int videoId);

    void update(int videoId);

    void pause(int videoId);

    void stop(int videoId);

    void destroyAll();

    private:
    JavaVM *javaVM;
    jobject videoPlayerManagerObj;

    jmethodID openMethodId;
    jmethodID startMethodId;
    jmethodID updateMethodId;
    jmethodID pauseMethodId;
    jmethodID stopMethodId;
    jmethodID getWidthMethodId;
    jmethodID getHeightMethodId;
    jmethodID setTextureMethodId;
    jmethodID destroyAllMethodId;
};