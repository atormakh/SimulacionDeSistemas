from collections import deque

def parse(line):
    return line.rstrip("\n").split(" ")


class Data:
    def __init__(self, filename, avg_t):
        self.avg_t = avg_t
        self.file = open(filename, 'r')
        self.num_particles, self.grid_size = map(int, parse(self.file.readline()))
        self.data = deque()
    
    
    def __iter__(self):
        return self
    
    def __next__(self):
        header = self.file.readline()
        if not header:
            raise StopIteration
        
        time, a, b = map(float, parse(header))


        data = []

        for j in range(self.num_particles):
            x, y, dir = map(float, parse(self.file.readline()))
            data.append((x, y, dir))
        
        if(len(self.data) > self.avg_t):
            self.data.popleft()
        
        self.data.append(data)

        return time, a, b, self.data

    def close(self):
        self.file.close()
        
