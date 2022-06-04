
def parse(line):
    return line.rstrip("\n").split(" ")


class Data:
    def __init__(self, filename):
        self.file = open(filename, 'r')
        self.N = int(self.file.readline())

    def __iter__(self):
        return self

    def __next__(self):
        header = self.file.readline()
        if not header:
            raise StopIteration

        time, z, h = map(float, parse(header))
        #time = float(header)
        data = []

        for j in range(self.N):
            x, y, dx, dy, r, type = map(float, parse(self.file.readline()))
            data.append((x, y, dx, dy, r, type))

        return time, z, h, data

    def close(self):
        self.file.close()
