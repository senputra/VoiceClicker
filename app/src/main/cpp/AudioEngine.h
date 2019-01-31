//
// Created by dodys on 12/16/2018.
//

#ifndef VOICECLICKER_AUDIOENGINE_H
#define VOICECLICKER_AUDIOENGINE_H

#include <aaudio/AAudio.h>
#include "TransmissionTCP.h"
#include "Transmission.h"


class AudioEngine{

public:

    AudioEngine();

    ~AudioEngine();
    void createRecordingStream();

    void errorCallback(AAudioStream *stream, aaudio_result_t audioError);

    aaudio_data_callback_result_t
    dataCallback(AAudioStream *stream, void *audioData, int32_t numFrames);

    void stopStream();

    void toggleTransmission();

    void setTransmissionEngine(Transmission *tEngine);

    void setTransmissionEngine(TransmissionTCP *tEngine);

private:
    bool isTCP = false;
    bool isTransmissionOn_ = false;

    AAudioStreamBuilder *createStreamBuilder();
    void setupRecordingStreamParameter(AAudioStreamBuilder *builder);
    int32_t playbackDeviceId_ = AAUDIO_UNSPECIFIED;
    aaudio_format_t sampleFormat_;
    int16_t sampleChannels_;
    int32_t sampleRate_;
    int32_t framesPerBurst_;
    AAudioStream *stream_ = nullptr;
    void *tEngine_ = nullptr;
    bool isFirstDataCallback_ = true;

private:

    void closeRecordingStream();

    void logRecordingStreamParameter(AAudioStream *stream);

    void drainRecordingStream(void *audioData, int32_t numFrames);
};

#endif //VOICECLICKER_AUDIOENGINE_H


