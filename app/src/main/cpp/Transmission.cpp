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

        udp::socket s(io_context, udp::endpoint(udp::v4(), 0));

        udp::resolver resolver(io_context);
        LOGD("IO_CONTEXT RESOLVED");

        udp::endpoint endpoint = *resolver.resolve(udp::v4(), "192.168.100.45", "5008").begin();
        std::string str = "gaasdfasdf";
        size_t request_length = std::strlen(str.c_str());
        asio::error_code ec;
        s.send_to(asio::buffer(str, request_length), endpoint);
    } catch (std::exception &e) {
        LOGE("Exception: %s", e.what());
    }
}

void Transmission::stop() {

}
