//
// Created by dodys on 12/16/2018.
//

#ifndef VOICECLICKER_AUDIOENGINE_H
#define VOICECLICKER_AUDIOENGINE_H

#include <aaudio/AAudio.h>



class AudioEngine{

public:

    AudioEngine();

    ~AudioEngine();
    void createRecordingStream();

private:
    AAudioStreamBuilder *createStreamBuilder();
    void setupRecordingStreamParameter(AAudioStreamBuilder *builder);


private:
    int32_t playbackDeviceId_ = AAUDIO_UNSPECIFIED;
    aaudio_format_t sampleFormat_;
    int16_t sampleChannels_;

    void closeOutputStream();
};

#endif //VOICECLICKER_AUDIOENGINE_H


