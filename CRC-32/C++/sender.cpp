#include <iostream>
#include <string>
#include <bitset>
#include <cstdint>

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
    std::cout << "CRC-32 Checksum Sender" << std::endl;
    std::cout << "----------------------" << std::endl;

    std::string binaryMessage;
    std::cout << "Enter the binary message: ";
    std::cin >> binaryMessage;
    
    uint32_t crc32Checksum = calculateCRC32(binaryMessage);

    std::bitset<32> checksumBits(crc32Checksum);
    std::string checksumBinary = checksumBits.to_string();
    std::string fullMessage = binaryMessage + checksumBinary;
    std::cout << "Full Message: " << fullMessage << std::endl;

    return 0;
}
