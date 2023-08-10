import socket
from HammingR import receive_Message

HOST = "127.0.0.1"
PORT = 65432

def generate():
    file_path = "./Hamming/java/trials.txt"  # Replace with the actual file path
    target_length = 10000

    lines = []

    while len(lines) < target_length:
        try:
            with open(file_path, "r") as file:
                for line in file:
                    lines.append(line.strip())
                    if len(lines) >= target_length:
                        break
        except IOError as e:
            print("Error reading the file:", e)

    lyrics_list = lines
    return lyrics_list

true_message = generate()


receiver_data = []

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((HOST, PORT))
server_socket.listen()

print("Server listening on", (HOST, PORT))

def stop_server():
    global server_running
    server_running = False

server_running = True

i = 0
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
                    #receiver_data[addr] = received_data
                    message, error, parity = receive_Message(received_data)
                    probability_noise = 0.015
                    correct_message = True if message == true_message[i] else False
                    info = {
                        "error": error,
                        "parity": parity,
                        "probability_noise": probability_noise,
                        "correct_message": correct_message
                    }
                    i += 1
                    receiver_data.append(info)
                    print(f"Received from {addr}: {message}")
                    conn.send(b"Message received")
            stop_server()
        except ConnectionResetError:
            print("Connection reset by client", addr)

        conn.close()

except Exception as e:
    print("An error occurred:", e)

finally:
    print("Server shutting down...")
    server_socket.close()


print("Server stopped")
print("Received data:")
statictics = {
    "error": 0,
    "parity": 0,
    "probability_noise": 0,
    "correct_message": 0
}

for e in receiver_data:
    statictics["error"] += e["error"]
    statictics["parity"] += e["parity"]
    statictics["probability_noise"] += e["probability_noise"]
    statictics["correct_message"] += 1 if e["correct_message"] else 0

statictics["probability_noise"] /= len(receiver_data)

print("Probabilidad de ruido:", statictics["probability_noise"])
print("Cantidad total de errores en mensajes causados por ruido: ", statictics["error"])
print("Cantidad promedio de errores en mensajes causados por ruido: ", statictics["error"] / len(receiver_data))
print("Cantidad total de bits de paridad por mensaje: ", statictics["parity"] / len(receiver_data))
print("Cantidad promedio de bits de paridad por mensaje: ", statictics["parity"])
print("Cantidad de mensajes recibidos: ", len(receiver_data))
print("Cantidad de mensajes recibidos correctamente: ", statictics["correct_message"])