#include <android/log.h>

#define LOG_TAG "lame_java"

#ifdef NDEBUG
#define LOGD(...)
#define LOGI(...)
#define LOGE(...)
#else
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#endif
