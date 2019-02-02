#include <jni.h>

/* @file native-lib.cpp
 * Function: bridge Java and C++ (AAudio API specifically).
 * */

#include "AudioEngine.h"
#include "DooDeeLOG.h"
#include "Transmission.h"
#include "TransmissionTCP.h"


static AudioEngine *engine = nullptr;
static Transmission *tEngine = nullptr;
static TransmissionTCP *tcpEngine = nullptr;
static bool isTCP = false;

extern "C" {

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_startEngine(JNIEnv *env, jclass type, jstring ipAddress_) {

    const char *ipAddress = env->GetStringUTFChars(ipAddress_, 0);

    if (isTCP) {
        engine = new AudioEngine();
        tcpEngine = new TransmissionTCP();
        LOGD("OKEHHHH");
    } else {
        engine = new AudioEngine();
        tEngine = new Transmission(ipAddress);
        LOGD("OKEHHHH");
    }

    env->ReleaseStringUTFChars(ipAddress_, ipAddress);

}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_stopEngine(JNIEnv *env, jclass type) {

    engine->stopStream();
}


JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_stopTransmission(JNIEnv *env, jclass type) {

    engine->toggleTransmission();
    tEngine->stop();

}
JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_startTransmission(JNIEnv *env, jclass type,
                                                            jstring ipAddress_) {
    const char *ipAddress = env->GetStringUTFChars(ipAddress_, 0);

    if (tEngine == nullptr) {
        tEngine = new Transmission(ipAddress);
    }
    engine->toggleTransmission();
    engine->setTransmissionEngine(tEngine);

    env->ReleaseStringUTFChars(ipAddress_, ipAddress);
}
}