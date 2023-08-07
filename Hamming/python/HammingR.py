def calculate_parity_bits(m):
    positions = []
    acu = 0
    i = 0
    while acu < m:
        acu += 2 ** i
        i += 1
    for j in range(i):
        positions.append(2 ** j)
    return positions


def from_binary_to_decimal(binary):
    decimal = 0
    n = len(binary)
    for i in range(n):
        decimal += int(binary[i]) * 2 ** (n - i - 1)
    return decimal


def is_in_array(array, value):
    for i in range(len(array)):
        if value == array[i]:
            return True
    return False


def even_parity(bits):
    count = 0
    for bit in bits:
        if bit == 1:
            count += 1
    return '0' if count % 2 == 0 else '1'


def odd_parity(bits):
    count = 0
    for bit in bits:
        if bit == 1:
            count += 1
    return '1' if count % 2 == 0 else '0'


def generate_hamming_parity(bits, r, m):
    map = {}

    # Guardar en un mapa los valores de las potencias de 2 de cada m
    for i in range(len(m)):
        powers = sum_powers_of_2(m[i])
        map[m[i]] = powers

    parities = []
    for i in range(len(r)):
        parities.append(calc_parity(map, r[i], bits))

    new_parity_bits = ""
    for parity in parities:
        new_parity_bits += even_parity(parity)

    return new_parity_bits


def calc_parity(map, bit, plot):
    powers = []
    for key, values in map.items():
        if bit in values:
            powers.append(key)

    parity_check = []
    for power in powers:
        parity_check.append(int(plot[power - 1]))

    return parity_check


def sum_powers_of_2(v):
    copy_v = v
    powers = []
    while copy_v > 0:
        power = 1
        acu = 0
        while power <= copy_v:
            power *= 2
            acu += 1
        power //= 2
        copy_v -= power
        powers.append(acu - 1)

    powers_array = [2 ** power for power in powers]
    return powers_array


def find_error(b1, b2):
    b3 = ""
    for i in range(len(b1)):
        if b1[i] == b2[i]:
            b3 += '0'
        else:
            b3 += '1'

    b3 = b3[::-1]

    error = from_binary_to_decimal(b3)
    if error == 0:
        #print("No hay error")
        return 0
    else:
        #print("Error en la posicion:", error)
        return error


def fix_error(bits, error):
    new_bits = ""
    for i in range(len(bits)):
        if i == error - 1:
            new_bits += '1' if bits[i] == '0' else '0'
        else:
            new_bits += bits[i]
    return new_bits


def original(bits, r):
    new_bits = ""
    for i in range(len(bits)):
        if (i + 1) not in r:
            new_bits += bits[i]
    return new_bits


def binary_to_ascii(bits):
    n = len(bits)
    ascii = ""
    for i in range(0, n, 8):
        ascii += chr(from_binary_to_decimal(bits[i:i + 8]))
    return ascii




def main(received_hamming_code):
    # Received Hamming code (You can receive this code from the transmitter)
    #
    #print("Received Hamming code:", received_hamming_code)

    m = len(received_hamming_code)
    r = calculate_parity_bits(m)

    received_parity_bits = ""
    for i in r:
        received_parity_bits += received_hamming_code[i - 1]

    m_pos = [i + 1 for i in range(m) if (i + 1) not in r]

    new_parity_bits = generate_hamming_parity(received_hamming_code, r, m_pos)
    car = ''

    error = find_error(received_parity_bits, new_parity_bits)
    if error > 0:
        corrected_hamming_code = fix_error(received_hamming_code, error)
        #print("Corrected Hamming code:", corrected_hamming_code)
        corrected_message = original(corrected_hamming_code, r)
        #print("Corrected message:", corrected_message)
        ascii = binary_to_ascii(corrected_message)
        car = ascii
    else:
        original_message = original(received_hamming_code, r)
        #print("Mensaje:", original_message)
        ascii = binary_to_ascii(original_message)
        #print("ASCII:", ascii)
        car = ascii

    #print('Caracter: ', car)
    return car


if __name__ == "__main__":
    #received_hamming_code = input("Ingrese el código de Hamming recibido: ")
    hello_world = ['01100001100', '10111001001', '01101101010', '00101010011', '1110000000', '00110100011', '10101011111', '01101100101', '00101011100', '10101100011', '1110000000', '1011100011']
    mensaje = ''
    for e in hello_world:
        mensaje += main(e)
    print(mensaje)
