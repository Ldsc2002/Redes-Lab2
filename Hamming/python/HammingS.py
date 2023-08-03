def calculate_parity_bits(m):
    r = 1
    while 2**r < m + r + 1:
        r += 1
    return r

def generate_hamming_code(binary_plot):
    #print("Data bits:", binary_plot)
    m = len(binary_plot)
    #print("Number of data bits to be encoded:", m)	
    r = calculate_parity_bits(m)
    #print("Number of parity bits to be added:", r)
    n = m + r
    #print("Total number of bits in Hamming code:", n)

    hamming_code = [-1] * n

    for i in range(r):
        hamming_code[2**i - 1] = 0

    #print("Hamming code in the beginning:", hamming_code)

    true_message_bits_pos = []
    parity_bits_pos = []

    for i in range(n):
        if hamming_code[i] == -1:
            hamming_code[i] = binary_plot.pop(0)
            true_message_bits_pos.append(i + 1)
        else:
            parity_bits_pos.append(i + 1)

    #print("Position of message bits:", true_message_bits_pos)
    #print("Hamming code:", hamming_code)
    tablita = {}
    for i in true_message_bits_pos:
        #sum_powers_of_2(i)
        tablita[i] = sum_powers_of_2(i)

    #print(tablita)
    #print("Position of parity bits:", parity_bits_pos)
    for e in parity_bits_pos:
        #print(f"Calculating parity bit for position {e}: {calculate_parity_bit(tablita, e)}")
        #print('parity bit: ', e)
        calculate_parity_bit(tablita, e, hamming_code)
    
    #print("Hamming code:", hamming_code)
    return ''.join(str(e) for e in hamming_code)

def calculate_parity_bit(tablita, bit, trama):
    # regla par, si hay un numero impar de 1's es 1, si hay un numero par de 1's es 0
    l = []
    for key in tablita:
        if bit in tablita[key]:
            l.append(key - 1)
    #print(l)
    parity_check = []
    for i in l:
        parity_check.append(trama[i])
    trama[bit - 1] = even_parity(parity_check)
    #print(f"Parity bit for position {bit} with parity {parity_check}: {trama[bit - 1]}")

def even_parity(bits):
    count = 0
    for i in bits:
        if i == '1':
            count += 1
    if count % 2 == 0:
        return 0
    else:
        return 1

def odd_parity(bits):    
    count = 0
    for i in bits:
        if i == '1':
            count += 1
    if count % 2 == 0:
        return 1
    else:
        return 0

def is_power_of_2(n):
    return n != 0 and (n & (n - 1)) == 0

def sum_powers_of_2(number):
    if is_power_of_2(number):
        print(f"{number} is already a power of 2.")
        return

    current_number = number
    powers = []

    while current_number > 0:
        power_of_2 = 1
        acum = 0
        while power_of_2 <= current_number:
            power_of_2 *= 2
            acum += 1

        power_of_2 //= 2
        current_number -= power_of_2
        powers.append(acum - 1)

    #print(powers)
    positions = []
    for i in range(len(powers)):
        positions.append(2**powers[i])

    #print(f"Positions to check: {positions}")
    return positions


trama = '1000001'
trama = input('Ingrese la trama: ')
print('Trama original: ', trama)
trama = list(trama)
print('Trama en código de hamming: ', generate_hamming_code(trama))