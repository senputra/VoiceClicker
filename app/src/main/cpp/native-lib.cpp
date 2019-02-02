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
Java_com_doodee_voiceclicker_AudioEngine_startEngine(JNIEnv *env, jclass type) {

    if (isTCP) {
        engine = new AudioEngine();
        tcpEngine = new TransmissionTCP();
        LOGD("OKEHHHH");
    } else {
        engine = new AudioEngine();
        tEngine = new Transmission();
        LOGD("OKEHHHH");
    }

}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_AudioEngine_stopEngine(JNIEnv *env, jclass type) {

    engine->stopStream();
}


JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_startTransmission(JNIEnv *env, jclass type) {


    if (tEngine == nullptr) {
        tEngine = new Transmission();
    }
    engine->toggleTransmission();
    engine->setTransmissionEngine(tEngine);

}

JNIEXPORT void JNICALL
Java_com_doodee_voiceclicker_Transmission_stopTransmission(JNIEnv *env, jclass type) {

    engine->toggleTransmission();
    tEngine->stop();

}
}