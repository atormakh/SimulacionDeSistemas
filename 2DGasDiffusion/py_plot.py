import matplotlib.pyplot as plt
import numpy as np


def plotGraphs(data, N=2000, hole_size=0.03, f=False):
    count = []
    flow = []
    flow_actual = 0
    for time, a, b, _data in data:
        count.append((a, b))

    plt.figure("Lattice Gas Particles", figsize=(10, 10))
    plt.plot(count)
    plt.title("N={} and Hole size={}".format(N, hole_size))
    plt.xlabel("time")
    plt.ylabel("particles proportion")
    plt.yticks(np.arange(0, 1.05, 0.05))
    plt.grid()

    plt.savefig("equilibrium.png")
    # plt.show()
