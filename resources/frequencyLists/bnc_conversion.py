import sys

filename = sys.argv[1]
print(filename)

file = open(filename, 'r')
newfile = open(filename+'_converted','w+')

for line in file:
    splitted = line.split(" ")
    newfile.write(splitted[1] + "\t" + splitted[2] + "\n")
