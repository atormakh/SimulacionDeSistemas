
def parse(line):
    return line.rstrip("\n").split(" ")


class DataIterator:
    def __init__(self, filepath):
        self.filepath = filepath
        self.file = open(filepath, "r")

    def __next__(self):
        line = self.file.readline()
        if line == "":
            raise StopIteration
        
        t, r, v, a = map(float, parse(line))
        return t, r, v, a



class Data:

    def __init__(self, filename):
        self.filename = filename

    def __iter__(self):
        return DataIterator(self.filename)

    
