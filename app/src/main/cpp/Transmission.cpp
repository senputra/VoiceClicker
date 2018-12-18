//
// Created by dodys on 12/17/2018.
//

#include "Transmission.h"
#include "AudioEngine.h"
#include "DooDeeLOG.h"
#include "asio.hpp"

using asio::ip::tcp;

Transmission::Transmission(AAudioStream *stream) {
    stream_ = stream;

    logData();
}

void Transmission::logData() {
    LOGD("received data from mic: %d",
         AAudioStream_read(stream_, data_, numFrames_, static_cast<int64_t> (0)));
}

void Transmission::setupClient() {

}