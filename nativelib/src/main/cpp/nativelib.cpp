#include <jni.h>
#include <string>
#include "crc.h"
#include "android_log.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_testFromJNI(JNIEnv *env, jobject thiz, jint i) {
    //test error
    int b = 2/i;
    std::string hello(reinterpret_cast<const char *>(b & 255));
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_nativelib_NativeLib_testCrcFromJNI(JNIEnv *env, jobject thiz) {
    std::string s("abcd_focdjlasdasdasd1234453dasasasfasfsaqewqweqweqrqwtewtewtwy");
    unsigned int i = crc16((unsigned char *)s.c_str(), s.size());
    unsigned int _i = _crc_16((unsigned char *)s.c_str(), s.size());
    LOGE("i = %d", i)
    LOGE("_i = %d", _i)
    return true;
}