def calculateChecksum(binaryMessage):
    crc = 0xFFFFFFFF
    table = [0] * 256
    polynomial = 0xEDB88320

    for i in range(256):
        crc = i
        for j in range(8):
            if crc & 1:
                crc = (crc >> 1) ^ polynomial
            else:
                crc >>= 1
        table[i] = crc

    crc = 0xFFFFFFFF
    for byte in binaryMessage:
        crc = (crc >> 8) ^ table[(crc ^ ord(byte)) & 0xFF]

    return crc ^ 0xFFFFFFFF

if __name__ == "__main__":
    print("----- CRC-32 Checksum Encoder -----")

    binaryMessage = input("Enter the binary message: ")
    
    checksum = calculateChecksum(binaryMessage)
    checksumBinary = bin(checksum & 0xFFFFFFFF)[2:].zfill(32)
    print("Full Message:", binaryMessage + checksumBinary)
