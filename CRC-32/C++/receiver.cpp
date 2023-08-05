#include <iostream>
#include <string>
#include <bitset>
#include <cstdint>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

#define PORT 8080
#define ITERATIONS 10000

uint32_t calculateCRC32(const std::string& binaryMessage) {
    uint32_t crc = 0xFFFFFFFF;
    uint32_t crcTable[256];
    uint32_t polynomial = 0xEDB88320;

    // Generate CRC-32 table
    for (uint32_t i = 0; i < 256; ++i) {
        crc = i;
        for (uint32_t j = 0; j < 8; ++j) {
            if (crc & 1) {
                crc = (crc >> 1) ^ polynomial;
            } else {
                crc >>= 1;
            }
        }
        crcTable[i] = crc;
    }

    // Calculate CRC-32
    crc = 0xFFFFFFFF;
    for (char byte : binaryMessage) {
        crc = (crc >> 8) ^ crcTable[(crc ^ byte) & 0xFF];
    }

    return crc ^ 0xFFFFFFFF;
}

int main() {
    int socket_fd, new_socket, valread;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[1024] = {0};

    // create socket file descriptor
    if ((socket_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // set socket options
    if (setsockopt(socket_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt))) {
        perror("setsockopt failed");
        exit(EXIT_FAILURE);
    }

    // set address parameters
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons(PORT);

    // bind socket to address
    if (bind(socket_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }

    // listen for incoming connections
    if (listen(socket_fd, 3) < 0) {
        perror("listen failed");
        exit(EXIT_FAILURE);
    }

    // accept incoming connection
    if ((new_socket = accept(socket_fd, (struct sockaddr *)&address, (socklen_t*)&addrlen)) < 0) {
        perror("accept failed");
        exit(EXIT_FAILURE);
    }

    int valid = 0;

    for (int i = 0; i < ITERATIONS; i++) {
        valread = read(new_socket, buffer, 1024);

        std::string fullMessage;
        for (int i = 0; i < valread; i++) {
            fullMessage += buffer[i];
        }

        std::string binaryMessage = fullMessage.substr(0, fullMessage.length() - 32);
        std::string receivedChecksum = fullMessage.substr(fullMessage.length() - 32);

        uint32_t crc32Checksum = calculateCRC32(binaryMessage);

        std::bitset<32> computedChecksumBits(crc32Checksum);
        std::string computedChecksumBinary = computedChecksumBits.to_string();

        if (computedChecksumBinary == receivedChecksum) {
            valid++;
        } 

        char *response = "Ready for next message.";
        send(new_socket, response, 1024, 0);
    }

    std::cout << "Valid messages percentage: " << (float)valid / ITERATIONS * 100 << "%" << std::endl;

    close(new_socket);
    close(socket_fd);

    return 0;
}
