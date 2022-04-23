from collections import deque


def parse(line):
    return line.rstrip("\n").split(" ")


class Data:
    def __init__(self, filename, dt = 0.02):
        self.file = open(filename, 'r')
        self.num_particles, self.hole_size = map(
            float, parse(self.file.readline()))
        self.data = deque()
        self.dt = dt
        self.t = 0
        self.header = ""

    def __iter__(self):
        return self

    def __next__(self):
        while True:
            if not self.header:
                self.header = self.file.readline()
            if not self.header:
                raise StopIteration

                
            time, a, b = map(float, parse(self.header))
            
            data = []

            a = 0
            b = 0

            if time > self.t:
                dt = self.dt
                for p in self.data:
                    x, y, vx, vy = p
                    if x < 0.24/2: a+=1
                    data.append((x + vx*dt, y + vy*dt, vx, vy))
                
                a/=self.num_particles
                b = 1-a
                self.t += dt
                self.data = data
                return self.t, a, b, self.data
            else:
                self.header = ""
                for j in range(int(self.num_particles)):
                    x, y, vx, vy = map(float, parse(self.file.readline()))
                    data.append((x, y, vx, vy))
                self.data = data
            

     

    def close(self):
        self.file.close()
