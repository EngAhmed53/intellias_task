cmake_minimum_required(VERSION 3.22.1)
project(videometa LANGUAGES CXX)

add_library(videometa SHARED videometa.cpp)

# mediandk: AMediaExtractor / AMediaFormat (API 21+). log: __android_log_print.
# Docs: https://developer.android.com/ndk/reference/group/media
target_link_libraries(videometa PRIVATE mediandk log)
