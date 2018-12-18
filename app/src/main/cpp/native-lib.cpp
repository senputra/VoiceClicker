#include <jni.h>
#include <string>

/* @file native-lib.cpp
 * Function: bridge Java and C++ (AAudio API) specifically.
 * */

#include "AudioEngine.h"
#include "DooDeeLOG.h"
#include "Transmission.h"


static AudioEngine *engine = nullptr;
static Transmission *tEngine = nullptr;

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_doodee_voiceclicker_AudioEngine_stringFromJNI(JNIEnv *env, jclass type) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_startEngine(JNIEnv *env, jclass type) {

    engine = new AudioEngine();
    LOGD("OKEHHHH");

}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_stopEngine(JNIEnv *env, jclass type) {

    engine->stopStream();
}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_checkStat(JNIEnv *env, jclass type) {

    engine->checkStreamStatus();
    LOGD("Check status triggered");

}


JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_startTransmission(JNIEnv *env, jclass type) {

    Transmission *tEngine = new Transmission();
}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_stopTransmission(JNIEnv *env, jclass type) {

    tEngine->stop();
}

}