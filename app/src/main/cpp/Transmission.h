//
// Created by dodys on 12/17/2018.
//

#ifndef VOICECLICKER_TRANSMISSION_H
#define VOICECLICKER_TRANSMISSION_H

#define ASIO_STANDALONE //prevent boost being used

#include <cstdint>
#include <string>
#include "DooDeeLOG.h"
#include <aaudio/AAudio.h>
#include "asio.hpp"


using asio::ip::udp;

class Transmission {

public:

    Transmission() {
        setupClient();
    };

    void stop() {
        socket_->release();
        socket_->close();
        delete (this);
    }


    void send(int16_t *audioData, int32_t numFrames) {
        try {
            socket_->send_to(asio::buffer(audioData, static_cast<size_t>(bufferSize)), endpoint_);
        } catch (std::exception &e) {
            LOGE("SOMETHING IS WRONG:: %s", e.what());
        }
    }
private:

    std::string ipAddrs_;
    int port_;
    int32_t numFrames_ = 192;
    int32_t bufferSize = numFrames_ * 2;
    AAudioStream *stream_;
    void *data_;
    udp::socket *socket_;
    udp::endpoint endpoint_;


    void logData() {
        LOGD("received data from mic: %d",
             AAudioStream_read(stream_, data_, numFrames_, static_cast<int64_t> (0)));
    }

    void setupClient() {
        try {

            asio::io_context io_context;
            LOGD("IO_CONTEXT GOTTEN");

            socket_ = new udp::socket(io_context, udp::endpoint(udp::v4(), 0));

            udp::resolver resolver(io_context);
            LOGD("IO_CONTEXT RESOLVED");

            endpoint_ = *resolver.resolve(udp::v4(), "192.168.43.201", "5008").begin();
        } catch (std::exception &e) {
            LOGE("Exception: %s", e.what());
        }
    }

};
#endif //VOICECLICKER_TRANSMISSION_H
