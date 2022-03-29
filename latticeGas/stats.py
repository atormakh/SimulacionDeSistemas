import os
import random
import sys
import matplotlib.pyplot as plt
import matplotlib
import math
import data_import
import statistics

import pickle


os.popen("mvn package").read()

seed = 2022
random.seed(seed)

N = 10
particles = [2000, 3000, 5000]
hole_sizes = [40, 50, 60]
seeds = [random.randint(1, 2**16) for _ in range(0, N)]

try:
    pickle_file = open("data.pickle","rb")
    data = pickle.load(pickle_file)
    pickle_file.close()
except:
    data = {}


path = "stats_output/"

os.makedirs(path, exist_ok=True)

for hole_size in hole_sizes:
    for numParticles in particles:
        if(data.get((numParticles, hole_size))):
            continue
        else:
            data[(numParticles, hole_size)] = []

        for seed in seeds:
            command = f"java -cp target/latticeGas-1.0-SNAPSHOT.jar -DnumParticles={numParticles} -DholeSize={hole_size} -Dseed={seed} latticeGas.Main "
            print(command)
            proc = os.popen(command)
            data[(numParticles,hole_size)].append(proc.readline())

            # data = data_import.Data("latticeGas.txt", 0)
            # count = [(a, b) for time, a, b, frame in data]
            # plt.figure("{} particles, hole size {}, seed {}".format(
            #     numParticles, hole_size, seed))
            # plt.plot(count)
            # plt.savefig(f"{path}/{numParticles}_{hole_size}_{seed}.png")
            # plt.close


with open("data.pickle", "wb") as f:
    pickle.dump(data, f)
    f.close()

HOLE_SIZE = 50

time = []
errors = []
for numParticles in particles:
    times = data[(numParticles, HOLE_SIZE)]
    time.append(statistics.mean(map(float,times)))
    errors.append(statistics.stdev(map(float,times))/math.sqrt(len(times)))


plt.figure("Time vs N particles")
plt.xlabel("N particles")
plt.ylabel("Time")
plt.errorbar(particles, time, yerr=errors)
plt.savefig("time_vs_N_particles.png")


time = []
errors = []
NUM_PARTICLES = 3000
for hole_size in hole_sizes:
    times = data[(NUM_PARTICLES, hole_size)]
    time.append(statistics.mean(map(float,times)))
    errors.append(statistics.stdev(map(float,times))/math.sqrt(len(times)))


plt.figure("Time vs Hole size")
plt.xlabel("Hole size")
plt.ylabel("Time")
plt.errorbar(hole_sizes, time, yerr=errors)
plt.savefig("time_vs_holes_sizes.png")