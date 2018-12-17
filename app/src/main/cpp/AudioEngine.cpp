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

    builder = createStreamBuilder();

    if(builder == nullptr){
        LOGE("Failed setting up stream");
        return;
    }

    setupRecordingStreamParameter(builder);
    aaudio_result_t result = AAudioStreamBuilder_openStream(builder, &stream_);
    LOGD("Recording Stream has been successfully created :: %s",
         AAudio_convertResultToText(result));
    logRecordingStreamParameter(stream_);


    if (result == AAUDIO_OK) {
        sampleRate_ = AAudioStream_getSampleRate(stream_);
        framesPerBurst_ = AAudioStream_getFramesPerBurst(stream_);

        // Set the buffer size to the burst size - this will give us the minimum possible latency
        AAudioStream_setBufferSizeInFrames(stream_, framesPerBurst_);

        logRecordingStreamParameter(stream_);

        result = AAudioStream_requestStart(stream_);
        if (result != AAUDIO_OK) {
            LOGE("Error starting stream. %s", AAudio_convertResultToText(result));
        }

        AAudioStreamBuilder_delete(builder);
    }


}

void AudioEngine::setupRecordingStreamParameter(AAudioStreamBuilder *builder) {
    AAudioStreamBuilder_setDeviceId(builder, playbackDeviceId_);
    AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_INPUT);
    AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
    // We request EXCLUSIVE mode since this will give us the lowest possible latency.
    // If EXCLUSIVE mode isn't available the builder will fall back to SHARED mode.
    AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setFormat(builder, AAUDIO_FORMAT_PCM_I16);

//    AAudioStreamBuilder_setChannelCount(builder, sampleChannels_);

//    AAudioStreamBuilder_setDataCallback(builder, ::dataCallback, this);
//    AAudioStreamBuilder_setErrorCallback(builder, ::errorCallback, this);

}

void AudioEngine::logRecordingStreamParameter(AAudioStream *stream) {
    aaudio_result_t result;
//    result = AAudioStream_getDeviceId(stream_);
//    LOGD("DeviceId: %d",result);
    result = AAudioStream_getDirection(stream_);
    if (result == AAUDIO_DIRECTION_INPUT) {
        LOGD("Direction : input; %d", result);
    } else if (result == AAUDIO_DIRECTION_OUTPUT) {
        LOGD("Direction : output; %d", result);
    }
    result = AAudioStream_getSharingMode(stream_);
    LOGD("Sharing mode: %d", result);
    result = AAudioStream_getSampleRate(stream_);
    LOGD("Sample rate: %d", result);
    result = AAudioStream_getChannelCount(stream_);
    LOGD("Channel Count: %d", result);
    result = AAudioStream_getFormat(stream_);
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
    result = AAudioStream_getBufferCapacityInFrames(stream_);
    LOGD("Buffer capacity in frames: %d", result);
    result = AAudioStream_getFramesPerBurst(stream_);
    LOGD("FramesPerBurst: %d", result);
    result = AAudioStream_getBufferSizeInFrames(stream_);
    LOGD("Buffer size in frames: %d", result);
}

void AudioEngine::closeRecordingStream() {
    LOGD("close Stream");
    return;
}

void AudioEngine::checkStreamStatus() {
    LOGD("Current Sample Rate: %d", AAudioStream_getPerformanceMode(stream_));
}

