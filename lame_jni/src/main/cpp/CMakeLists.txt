cmake_minimum_required(VERSION 3.6)
project(lame-jni)

# add lame lib.
find_package(lame REQUIRED CONFIG)
# add log lib
find_library(log-lib log)

# add source code files of lame-jni
aux_source_directory(${CMAKE_HOME_DIRECTORY} SRC_LIST)
add_library(${PROJECT_NAME} SHARED ${SRC_LIST})

# link_libraries
target_link_libraries(
        ${PROJECT_NAME}
        lame::lame
        ${log-lib}
)
