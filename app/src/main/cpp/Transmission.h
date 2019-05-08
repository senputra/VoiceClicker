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

    explicit Transmission(const char *ipAddress, int port) {
        setupClient(ipAddress, port);
    };

    void tearDown() {
        socket_->release();
        socket_->close();
        delete (this);
    }

    void stop(){
        return;
    }


    void send(int16_t *audioData, int32_t numFrames) {
        try {
            socket_->send_to(asio::buffer(audioData, static_cast<size_t>(bufferSize)), endpoint_);
        } catch (std::exception &e) {
            LOGE("SOMETHING IS WRONG:: %s", e.what());
        }
    }
private:

    int32_t numFrames_ = 256;
    int32_t bufferSize = numFrames_ * 2;

    udp::socket *socket_{};
    udp::endpoint endpoint_;


    void setupClient(const char *ipAddress, int port) {
        try {

            asio::io_context io_context;
            LOGD("IO_CONTEXT GOTTEN");

            socket_ = new udp::socket(io_context, udp::endpoint(udp::v4(), 0));

            udp::resolver resolver(io_context);
            LOGD("IO_CONTEXT RESOLVED");

            std::string str(ipAddress);

            endpoint_ = *resolver.resolve(udp::v4(), str, std::to_string(port)).begin();
        } catch (std::exception &e) {
            LOGE("Exception: %s", e.what());
        }
    }

};
#endif //VOICECLICKER_TRANSMISSION_H
