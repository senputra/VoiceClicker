//
// Created by dodys on 12/16/2018.
//

#include <string>
#include "DooDeeLOG.h"
#include "AudioEngine.h"
#include "Transmission.h"

void errorCallback(AAudioStream *stream,
                   void *userData,
                   aaudio_result_t error) {
    assert(userData);
    AudioEngine *audioEngine = reinterpret_cast<AudioEngine *>(userData);
    audioEngine->errorCallback(stream, error);
}

/**
 * This method is called every 192 frames from the recording. Set from the builder parameter
 *
 * @param stream the audio stream which is requesting data, this is the playStream_ object
 * @param userData the context in which the function is being called, in this case it will be the
 * EchoAudioEngine instance
 * @param audioData an empty buffer into which we can write our audio data
 * @param numFrames the number of audio frames which are required
 * @return Either AAUDIO_CALLBACK_RESULT_CONTINUE if the stream should continue requesting data
 * or AAUDIO_CALLBACK_RESULT_STOP if the stream should stop.
 *
 * @see EchoAudioEngine#dataCallback
 */
aaudio_data_callback_result_t dataCallback(AAudioStream *stream, void *userData,
                                           void *audioData, int32_t numFrames) {
    assert(userData && audioData);
    AudioEngine *audioEngine = reinterpret_cast<AudioEngine *>(userData);
    return audioEngine->dataCallback(stream, audioData, numFrames);
}

AAudioStreamBuilder *AudioEngine::createStreamBuilder() {

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

/**
 *  This function is called to build a recording stream
 */
void AudioEngine::createRecordingStream() {

    AAudioStreamBuilder *builder = nullptr;

    builder = createStreamBuilder();

    if (builder == nullptr) {
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

        LOGD("Sample rate_ = %d Frames per burst = %d", sampleRate_, framesPerBurst_);

        logRecordingStreamParameter(stream_);

        result = AAudioStream_requestStart(stream_);
        if (result != AAUDIO_OK) {
            LOGE("Error starting stream. %s", AAudio_convertResultToText(result));
        } else {
            LOGD("Stream_ started : %s", AAudio_convertResultToText(result));
        }

        AAudioStreamBuilder_delete(builder);
    }

}

/**
 * Set up the stream with lowest possible latency
 * Currently the latency is 1000/(48000/192) = 4ms.
 * Every 4ms the data from the microphone is transmitted to computer.
 */
void AudioEngine::setupRecordingStreamParameter(AAudioStreamBuilder *builder) {
    AAudioStreamBuilder_setDeviceId(builder, AAUDIO_UNSPECIFIED);
    AAudioStreamBuilder_setDirection(builder, AAUDIO_DIRECTION_INPUT);
    AAudioStreamBuilder_setSharingMode(builder, AAUDIO_SHARING_MODE_EXCLUSIVE);
    // We request EXCLUSIVE mode since this will give us the lowest possible latency.
    // If EXCLUSIVE mode isn't available the builder will fall back to SHARED mode.
    AAudioStreamBuilder_setPerformanceMode(builder, AAUDIO_PERFORMANCE_MODE_LOW_LATENCY);
    AAudioStreamBuilder_setFormat(builder, AAUDIO_FORMAT_PCM_I16);
    AAudioStreamBuilder_setChannelCount(builder, 1);
    AAudioStreamBuilder_setDataCallback(builder, ::dataCallback, this);
    AAudioStreamBuilder_setErrorCallback(builder, ::errorCallback, this);
//    AAudioStreamBuilder_setSampleRate(builder,44100);
    AAudioStreamBuilder_setFramesPerDataCallback(builder, 192);

}

void AudioEngine::logRecordingStreamParameter(AAudioStream *stream) {
    aaudio_result_t result;
    result = AAudioStream_getDeviceId(stream_);
    LOGD("DeviceId: %d", result);
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
    AAudioStream_close(stream_);
    return;
}

void AudioEngine::errorCallback(AAudioStream *stream, aaudio_result_t audioError) {
    LOGE("errorCallback : %s", AAudio_convertResultToText(audioError));
    return;
}

void AudioEngine::stopStream() {

    aaudio_result_t result = AAudioStream_requestStop(stream_);
    LOGD("Recording stream has been stopped :: %s ", AAudio_convertResultToText(result));

}

aaudio_data_callback_result_t
AudioEngine::dataCallback(AAudioStream *stream, void *audioData, int32_t numFrames) {

    if (isFirstDataCallback_) {
        drainRecordingStream(audioData, numFrames);
        isFirstDataCallback_ = false;
    }

    if (isTransmissionOn_) {
//        LOGD("Transmitting tu tu tu tu");

        /* So, in C++ buffer is darn weird
         * There is no out of bound error
         * I need to specify the limit
         * sample rate 480000Hz means that there is 48,000 frames a second
         * get the num of frames in audioData from numFrame
         * in this case 192
         * thus buffer is from audioData[0] to audioData[191*2] since one frame takes 2 Byte
         * */
        if (isTCP) {
            reinterpret_cast<TransmissionTCP *>(tEngine_)->send(
                    reinterpret_cast<int16_t * >(audioData), numFrames);
        } else {
            reinterpret_cast<Transmission *>(tEngine_)->send(
                    reinterpret_cast<int16_t * >(audioData), numFrames);
        }
    }

//    LOGD("numFrames of the recording %d", AAudioStream_getXRunCount(stream_));

    return AAUDIO_CALLBACK_RESULT_CONTINUE;

}

/**
 * Switch the Transmission Engine on and off.
 */
void AudioEngine::toggleTransmission() {
    if (isTransmissionOn_) {
        LOGD("Transmitting tu tu tu tu");
        isTransmissionOn_ = false;
    } else {
        isTransmissionOn_ = true;
    }
}

/**
 * Store the Transmission Engine passed down from parent class
 */
void AudioEngine::setTransmissionEngine(Transmission *tEngine) {
    tEngine_ = tEngine;
    if (tEngine == nullptr) {
        isTransmissionOn_ = false;
        LOGE("Transmission Engine is not passed well");
    }
}

void AudioEngine::setTransmissionEngine(TransmissionTCP *tEngine) {
    tEngine_ = tEngine;
    if (tEngine == nullptr) {
        isTransmissionOn_ = false;
        LOGE("Transmission Engine is not passed well");
    }
}

/**
 * Clean the recording stream when it is first started
 */
void AudioEngine::drainRecordingStream(void *audioData, int32_t numFrames) {

    aaudio_result_t clearedFrames = 0;
    do {
        clearedFrames = AAudioStream_read(stream_, audioData, numFrames, 0);
    } while (clearedFrames > 0);
}
