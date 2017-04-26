LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := myndk2
LOCAL_SRC_FILES := ndk-source.cpp
LOCAL_LDLIBS := -llog -lGLESv2
LOCAL_CFLAGS += -std=c++11
LOCAL_SHARED_LIBRARIES := myndk2
include $(BUILD_SHARED_LIBRARY)