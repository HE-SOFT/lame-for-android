cmake_minimum_required(VERSION 3.6)
project(lame)

# add source code of lame
aux_source_directory(${CMAKE_HOME_DIRECTORY} LAME_SRC_LIST)
aux_source_directory(${CMAKE_HOME_DIRECTORY}/vector LAME_VECTOR_SRC_LIST)

add_library(
        ${PROJECT_NAME}
        SHARED
        ${LAME_SRC_LIST}
        ${LAME_VECTOR_SRC_LIST}
)

target_include_directories(${PROJECT_NAME} PUBLIC include)
