from collections import deque


def parse(line):
    return line.rstrip("\n").split(" ")


class Data:
    def __init__(self, filename):
        self.file = open(filename, 'r')
        self.num_particles, self.hole_size = map(float, parse(self.file.readline()))
        self.data = deque()

    def __iter__(self):
        return self

    def __next__(self):
        header = self.file.readline()
        if not header:
            raise StopIteration

        time, a, b = map(float, parse(header))

        data = []

        for j in range(int(self.num_particles)):
            try:
                x, y, vx, vy = map(float, parse(self.file.readline()))
            except:
                print("asd")
                break
            data.append((x, y, vx, vy))

        self.data = data

        return time, a, b, self.data

    def close(self):
        self.file.close()
