def xor(a, b):
    if a == b:
        return 0
    else:
        return 1

def output1(i, sr1, sr2):
    return xor(xor(i, sr1), sr2)

def output2(i, sr2):
    return xor(i, sr2)

def convolucion(input):
    sr1 = 0
    sr2 = 0
    output = []

    for e in input:
        print(output1(e, sr1, sr2), output2(e, sr2))
        output.append(output1(e, sr1, sr2))
        output.append(output2(e, sr2))
        sr1 = sr2
        sr2 = e

    return output


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

    # Now you have the lines from the file in the 'lyrics_list' list
    # You can use the list as needed


def export_to_txt(data, file_path):
    try:
        with open(file_path, "w") as file:
            for line in data:
                file.write(line + "\n")
    except IOError as e:
        print("Error writing to the file:", e)
    

if __name__ == "__main__":
    l = generate()
    print(len(l))
    for e in l:
        print(e)
    export_to_txt(l, "./Hamming/message.txt")
    


