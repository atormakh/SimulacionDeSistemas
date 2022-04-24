from collections import deque
from turtle import update


def parse(line):
    return line.rstrip("\n").split(" ")


class Data:
    def __init__(self, filename, dt=0.02):
        self.file = open(filename, 'r')
        self.num_particles, self.hole_size = map(
            float, parse(self.file.readline()))
        self.data = deque()
        self.dt = dt
        self.t = 0
        self.event_t = 0
        self.is_wall_event = False
        self.event_momentum = 0
        self.l = 0.24*2 + 0.09*2 #+ 2*(0.09 - 0.03)
        self.update_header()

    def __iter__(self):
        return self

    def update_header(self):
        self.header = self.file.readline()
        if not self.header:
            raise StopIteration
        self.event_t, self.is_wall_event, self.event_momentum = map(
            float, parse(self.header))

    def update_data(self):
        data = []
        for j in range(int(self.num_particles)):
            x, y, vx, vy = map(float, parse(self.file.readline()))
            data.append((x, y, vx, vy))
        self.data = data

    def __next__(self):

        momentums_sum = 0
        
        while(self.t >= self.event_t):
            self.update_data()
            if self.is_wall_event: momentums_sum += abs(self.event_momentum)
            self.update_header()
        


        data = []

        a = 0
        b = 0

        temperature = 0

        dt = self.dt if self.event_t - self.t > self.dt else self.event_t - self.t
        for p in self.data:
            x, y, vx, vy = p
            if x < 0.24/2:
                a += 1
                temperature += vx**2 + vy**2
            data.append((x + vx*dt, y + vy*dt, vx, vy))

        a /= self.num_particles
        b = 1-a
        self.old_t = self.t
        self.t += self.dt
        old = self.data
        self.data = data
        pr = momentums_sum / (dt*self.l)
        return self.t, a, b, pr, temperature, old

    def close(self):
        self.file.close()
