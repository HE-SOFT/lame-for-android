cmake_minimum_required(VERSION 3.4.1)
project(lame-java)

set(DIST_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../../../dist)

# 添加lame-java源文件
aux_source_directory(${CMAKE_HOME_DIRECTORY} AME_LITE_SRC_LIST)

add_library(
        ${PROJECT_NAME}
        SHARED
        ${AME_LITE_SRC_LIST}
)

# 添加lame动态链接库
add_library(lame SHARED IMPORTED)
set_target_properties(
        lame PROPERTIES IMPORTED_LOCATION
        ${DIST_DIR}/lame/lib/${ANDROID_ABI}/liblame.so
)

# 添加lame中的头文件
target_include_directories(
        ${PROJECT_NAME}
        PRIVATE
        ${DIST_DIR}/lame/include
)

find_library(log-lib log)

target_link_libraries(
        ${PROJECT_NAME}
        lame
        ${log-lib}
)

# 设定动态链接库输出路径
set_target_properties(
        ${PROJECT_NAME}
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY
        "${DIST_DIR}/${PROJECT_NAME}/lib/${ANDROID_ABI}"
)