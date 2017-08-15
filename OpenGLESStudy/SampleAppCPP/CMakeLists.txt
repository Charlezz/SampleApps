cmake_minimum_required(VERSION 3.4.1)

include_directories(src/main/cpp)
add_library( SampleApp
             SHARED
             src/main/cpp/SampleRenderer.cpp
             )

find_library(OPENGLES2_gl_LIBRARY NAMES GLESv2)
find_library( log-lib log )


target_link_libraries( SampleApp
                       ${log-lib}
                       ${OPENGLES2_gl_LIBRARY})



