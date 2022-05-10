import numpy as np
import matplotlib.pyplot as plt

K = 1e-10


class Particle:
    def __init__(self, x, y, q):
        self.x = x
        self.y = y
        self.q = q

    def distance(self):
        return 1


class Grid:

    def __init__(self, N, D, Q):
        self.N = N
        self.particles = np.zeros((N, N), dtype=Particle)
        for i in range(N):
            for j in range(N):
                q = Q if (i+j) % 2 == 0 else -Q
                self.particles[i][j] = Particle((i+1)*D, j*D, q)

    def plot(self):
        for particle in self.particles.flatten():
            c = 'r' if particle.q > 0 else 'b'
            plt.scatter(particle.x, particle.y, c=c, s=100)

    def calculate_energy(self, x, y, vx, vy, m, q):
        EK = vx**2 + vy**2
        EK *= 0.5*m

        EU = 0
        for particle in self.particles.flatten():
            dx = particle.x - x
            dy = particle.y - y
            d = np.sqrt(dx**2 + dy**2)
            EU += particle.q/d
        EU *= K*q

        return EK + EU


