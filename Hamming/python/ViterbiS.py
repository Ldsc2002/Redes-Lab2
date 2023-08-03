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


entrada = input("Introduce la cadena de bits: ")
entrada = list(entrada)
entrada = [int(i) for i in entrada]
print(entrada)
print(convolucion(entrada))