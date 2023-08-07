import socket
import random

iterations = 10000
errorProbability = 0.1
messageLength = 8

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
    port = 8080

    s = socket.socket()
    host = socket.gethostname()
    s.connect((host, port))

    withError = 0
    
    for i in range(iterations):
        message = ""

        for j in range(messageLength):
            message += str(random.randint(0, 1))

        checksum = calculateChecksum(message)
        checksumBinary = bin(checksum & 0xFFFFFFFF)[2:].zfill(32)
        message = message + checksumBinary

        # randomly flip a bit
        if random.random() < errorProbability:
            withError += 1
            index = random.randint(0, len(message) - 1)
            message = message[:index] + str(1 - int(message[index])) + message[index + 1:]
        
        s.send(message.encode())
        data = s.recv(1024)
    
    withError = 100 - (withError / iterations * 100)
    s.send(str(withError).encode())
    s.close()