cmake_minimum_required(VERSION 3.4.1)
add_library( native-module
             SHARED
             src/main/cpp/native-module.cpp )

find_library( log-lib
              log )

target_link_libraries( native-module
                       ${log-lib} )
