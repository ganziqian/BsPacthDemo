# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


file(GLOB bzip_source src/main/cpp/bzip2/*.c)

add_library(
        native-lib

        SHARED

        src/main/cpp/native-lib.h
        src/main/cpp/bspatch.c
        ${bzip_source})
include_directories(src/main/cpp/bzip2)

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )


target_link_libraries( # Specifies the target library.
        native-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )