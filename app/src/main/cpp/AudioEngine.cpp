//
// Created by dodys on 12/16/2018.
//

#include <string>
#include "DooDeeLOG.h"
#include "AudioEngine.h"


AAudioStreamBuilder* AudioEngine::createStreamBuilder() {

    AAudioStreamBuilder *builder = nullptr;
    aaudio_result_t result = AAudio_createStreamBuilder(&builder);
    if (result != AAUDIO_OK) {
        LOGE("Error creating stream builder: %s", AAudio_convertResultToText(result));
    }
    return builder;
}


AudioEngine::AudioEngine() {
    createRecordingStream();
    return;
}

AudioEngine::~AudioEngine() {
    closeRecordingStream();
}

void AudioEngine::createRecordingStream(){

    AAudioStreamBuilder* builder = nullptr;
    AAudioStream *stream = nullptr;

    builder = createStreamBuilder();

    if(builder == nullptr){
        LOGE("Failed setting up stream");
        return;
    }

    setupRecordingStreamParameter(builder);
    aaudio_result_t result = AAudioStreamBuilder_openStream(builder,&stream);
    LOGD("Recording Stream has been successfully created :: %s",
         AAudio_convertResultToText(result));
    logRecordingStreamParameter(stream);

}

void AudioEngine::setupRecordingStreamParameter(AAudioStreamBuilder *builder) {
    AAudioStreamBuilder_setDeviceId(builder, playbackDeviceId_);
    AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_INPUT);
    AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
    // We request EXCLUSIVE mode since this will give us the lowest possible latency.
    // If EXCLUSIVE mode isn't available the builder will fall back to SHARED mode.
    AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);

//    AAudioStreamBuilder_setFormat(builder, sampleFormat_);
//    AAudioStreamBuilder_setChannelCount(builder, sampleChannels_);

//    AAudioStreamBuilder_setDataCallback(builder, ::dataCallback, this);
//    AAudioStreamBuilder_setErrorCallback(builder, ::errorCallback, this);

}

void AudioEngine::logRecordingStreamParameter(AAudioStream *stream) {
    aaudio_result_t result;
//    result = AAudioStream_getDeviceId(stream);
//    LOGD("DeviceId: %d",result);
    result = AAudioStream_getDirection(stream);
    if (result == AAUDIO_DIRECTION_INPUT) {
        LOGD("Direction : input; %d", result);
    } else if (result == AAUDIO_DIRECTION_OUTPUT) {
        LOGD("Direction : output; %d", result);
    }
    result = AAudioStream_getSharingMode(stream);
    LOGD("Sharing mode: %d", result);
    result = AAudioStream_getSampleRate(stream);
    LOGD("Sample rate: %d", result);
    result = AAudioStream_getChannelCount(stream);
    LOGD("Channel Count: %d", result);
    result = AAudioStream_getFormat(stream);
    switch (result) {
        case AAUDIO_FORMAT_INVALID:
            LOGD("Format: INVALID");
            break;
        case AAUDIO_FORMAT_PCM_FLOAT:
            LOGD("Format: FLOAT");
            break;
        case AAUDIO_FORMAT_PCM_I16:
            LOGD("Format: Integer16");
            break;
        case AAUDIO_FORMAT_UNSPECIFIED:
            LOGD("Format: UNSPECIFIED");
            break;
        default:
            LOGD("Format: I also dont know");
    }
    result = AAudioStream_getBufferCapacityInFrames(stream);
    LOGD("Buffer capacity in frames: %d", result);
    result = AAudioStream_getFramesPerBurst(stream);
    LOGD("FramesPerBurst: %d", result);
    result = AAudioStream_getBufferSizeInFrames(stream);
    LOGD("Buffer size in frames: %d", result);
}

void AudioEngine::closeRecordingStream() {
    LOGD("close Stream");
    return;
}

