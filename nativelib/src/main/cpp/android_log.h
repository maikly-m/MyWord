
#ifndef _ANDROIDLOG_H
#define _ANDROIDLOG_H


#include <android/log.h>

#define LOG_DEBUGA 1
#define LOG_ERRORA 1
#define LOG_ERRORA_P 1

#if LOG_DEBUGA
#define LOGD(FORMAT,...) __android_log_print(ANDROID_LOG_DEBUG, "test", FORMAT,##__VA_ARGS__);
#else
#define LOGD(FORMAT,...)
#endif

#if LOG_ERRORA
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR, "test", FORMAT,##__VA_ARGS__);
#else
#define LOGE(FORMAT,...)
#endif

#if LOG_ERRORA_P
#define LOGP(FORMAT,...) __android_log_print(ANDROID_LOG_WARN, "test", FORMAT,##__VA_ARGS__);
#else
#define LOGP(FORMAT,...)
#endif

#endif