import sys

filename = sys.argv[1]
print(filename)

file = open(filename, 'rb')
newfile = open(filename+'_converted','wb+')

validChars = set(range(48,57))
validChars.add(45)
validChars.add(9)
validChars.update(range(97,122))

for line in file:
    bullshit = False
    for char in line[:-1]:
        if not (char in validChars): 
            bullshit = True
            break
    if not bullshit:
        if not (line[0] == 49 and line[1] == 9):
            newfile.write(line)
