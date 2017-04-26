LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := ndkbuild
LOCAL_SRC_FILES := NativeSource.cpp
LOCAL_LDLIBS := -llog -lGLESv2
LOCAL_CFLAGS += -std=c++11
LOCAL_SHARED_LIBRARIES := ndkbuild
include $(BUILD_SHARED_LIBRARY)