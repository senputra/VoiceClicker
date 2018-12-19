//
// Created by dodys on 12/17/2018.
//

#include "Transmission.h"
#include "AudioEngine.h"
#include "DooDeeLOG.h"
#include "asio.hpp"

using asio::ip::udp;

Transmission::Transmission() {
    setupClient();
}

void Transmission::logData() {
    LOGD("received data from mic: %d",
         AAudioStream_read(stream_, data_, numFrames_, static_cast<int64_t> (0)));
}

void Transmission::setupClient() {
    try {

        asio::io_context io_context;
        LOGD("IO_CONTEXT GOTTEN");

        socket_ = new udp::socket(io_context, udp::endpoint(udp::v4(), 0));

        udp::resolver resolver(io_context);
        LOGD("IO_CONTEXT RESOLVED");

        endpoint_ = *resolver.resolve(udp::v4(), "192.168.100.45", "5008").begin();
    } catch (std::exception &e) {
        LOGE("Exception: %s", e.what());
    }
}


void Transmission::stop() {
    socket_->release();
    socket_->close();
    delete (this);
}

void Transmission::send(int16_t *audioData, int32_t numFrames) {
    try {
        socket_->send_to(asio::buffer(audioData, static_cast<size_t>(numFrames) * 2), endpoint_);

//        std::string str = "halasdfasdfasdfloooo";
//        size_t request_length = std::strlen(str.c_str());
//        LOGD("asdfasdflasdkfjlksadjflaskdjfklasjflsljakfdj %d",static_cast<size_t>(numFrames));
    } catch (std::exception &e) {
        LOGE("SOMETHING IS WRONG:: %s", e.what());
    }
}
