//
// Created by dodys on 12/16/2018.
//

#include <string>
#include "AudioEngine.h"
#include "DooDeeLOG.h"


AAudioStream *stream = nullptr;

AAudioStreamBuilder* AudioEngine::createStreamBuilder() {

    AAUDIO_API::AAudioStreamBuilder *builder = nullptr;
    aaudio_result_t result = AAUDIO_API::AAudio_createStreamBuilder(&builder);
    if (result != AAUDIO_OK) {
        LOGE("Error creating stream builder: %s", AAudio_convertResultToText(result));
    }
    return builder;
}

void AudioEngine::createRecordingStream(){

    AAudioStreamBuilder* builder = nullptr;
    builder = createStreamBuilder();

    if(builder == nullptr){
        LOGE("Failed setting up stream");
        return;
    }

    setupRecordingStreamParameter(builder);

    aaudio_result_t result = AAudioStreamBuilder_openStream(builder,&stream);
    LOGD("Recording Stream has been successfully created");
}

void AudioEngine::setupRecordingStreamParameter(AAudioStreamBuilder *builder) {

    AAudioStreamBuilder_setDeviceId(builder, playbackDeviceId_);
//    AAudioStreamBuilder_setFormat(builder, sampleFormat_);
//    AAudioStreamBuilder_setChannelCount(builder, sampleChannels_);
    AAudioStreamBuilder_setDirection(builder,AAUDIO_DIRECTION_INPUT);

    // We request EXCLUSIVE mode since this will give us the lowest possible latency.
    // If EXCLUSIVE mode isn't available the builder will fall back to SHARED mode.
    AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
    AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_OUTPUT);
//    AAudioStreamBuilder_setDataCallback(builder, ::dataCallback, this);
//    AAudioStreamBuilder_setErrorCallback(builder, ::errorCallback, this);

}


