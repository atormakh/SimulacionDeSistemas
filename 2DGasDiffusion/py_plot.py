import matplotlib.pyplot as plt
import numpy as np


def plotGraphs(data, N=2000, hole_size=0.03, f=False):
    count = []
    flow = []
    times = []
    flow_actual = 0
    for time, a, b, m, t, _data in data:
        times.append(time)
        count.append((a, b))

    plt.figure("Difusión Gas", figsize=(10, 10))
    plt.plot(times,count)
    plt.title("N={} and apertura={}m".format(N, hole_size))
    plt.xlabel("Tiempo (s)")
    plt.ylabel("fp")
    plt.yticks(np.arange(0, 1.05, 0.05))
    plt.grid()

    plt.savefig("equilibrium.png")
    # plt.show()
