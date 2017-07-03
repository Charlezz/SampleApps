cmake_minimum_required(VERSION 3.4.1)

include_directories( ${CMAKE_SOURCE_DIR}/../../../VideoPlayer/
                     ${CMAKE_SOURCE_DIR}/../../../VideoPlayer/include/ )

add_library( VideoPlayer SHARED IMPORTED )
#set_target_properties(VideoPlayer PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/imported-libs/obj/${ANDROID_ABI}/libVideoPlayer.so)
set_target_properties(VideoPlayer PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libVideoPlayer.so)

add_library( VideoSample SHARED ${CMAKE_SOURCE_DIR}/src/main/cpp/SampleJni.cpp)

find_library( log-lib log )

target_link_libraries( VideoSample VideoPlayer ${log-lib} )
