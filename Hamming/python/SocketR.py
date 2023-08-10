import socket
from HammingR import receive_Message

HOST = "127.0.0.1"
PORT = 65432

receiver_data = {}

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen()

print("Server listening on", (HOST, PORT))

def stop_server():
    global server_running
    server_running = False

server_running = True

try:
    while server_running:
        conn, addr = server_socket.accept()
        print("Connected by", addr)

        try:
            while server_running:
                output = conn.recv(1024)
                if not output:
                    print("Connection closed by client", addr)
                    break
                else:
                    received_data = output.decode("utf-8")
                    receiver_data[addr] = received_data
                    message, error, parity = receive_Message(received_data)
                    print(f"Received from {addr}: {message}")
                    conn.send(b"Message received")
            print("Aquí")
            stop_server()
        except ConnectionResetError:
            print("Connection reset by client", addr)

        conn.close()

except Exception as e:
    print("An error occurred:", e)

finally:
    print("Server shutting down...")
    server_socket.close()

print("Receiver data:", receiver_data)
