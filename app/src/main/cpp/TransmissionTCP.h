//
// Created by dodys on 1/5/2019.
//

#ifndef VOICECLICKER_TRANSMISSIONTCP_H
#define VOICECLICKER_TRANSMISSIONTCP_H


#define ASIO_STANDALONE //prevent boost being used

#include <cstdint>
#include <string>
#include "DooDeeLOG.h"
#include <aaudio/AAudio.h>
#include <asio.hpp>


using asio::ip::tcp;

class TransmissionTCP {

public:

    TransmissionTCP() {
        setupClient();
    };

    void stop() {
        socket_->release();
        socket_->close();
        delete (this);
    }


    void send(const int16_t *audioData, int32_t numFrames) {
        try {
            numFrames_ = numFrames;
//            asio::write(socket_,asio::buffer(audioData, static_cast<size_t>(bufferSize)));
            socket_->async_send(asio::buffer(audioData, static_cast<size_t>(bufferSize)),
                                std::bind(&TransmissionTCP::sendHandler,
                                          std::placeholders::_1,
                                          std::placeholders::_2));

        } catch (std::exception &e) {
            LOGE("SOMETHING IS WRONG:: %s", e.what());
        }
    }

private:

    std::string ipAddrs_;
    int port_{};
    int32_t numFrames_ = 192;
    int32_t bufferSize = numFrames_ * 2;
    AAudioStream *stream_{};
    void *data_{};
    tcp::socket *socket_{};

private:

    static void sendHandler(const std::error_code &ec, size_t bytes_transferred) {
        int a = 10;
        return;
    }


    void logData() {
        LOGD("received data from mic: %d",
             AAudioStream_read(stream_, data_, numFrames_, static_cast<int64_t> (0)));
    }

    void setupClient() {
        try {

            asio::io_context io_context;
            LOGD("IO_CONTEXT GOTTEN");

            socket_ = new tcp::socket(io_context, tcp::endpoint(tcp::v4(), 0));

            LOGD("Stucked at socket,connect");
            //connection
            socket_->connect(tcp::endpoint(asio::ip::address::from_string("192.168.137.1"), 5008));
            LOGD("NOT stucked at socket,connect");


        } catch (std::exception &e) {
            LOGE("Exception: %s", e.what());
        }
    }

};

#endif //VOICECLICKER_TRANSMISSIONTCP_H
