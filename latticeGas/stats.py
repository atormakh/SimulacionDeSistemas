from cProfile import label
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

SEED = 2023
random.seed(SEED)

N = 10
particles = [2000, 2500, 3000, 3500, 4000, 4500, 5000]
hole_sizes = [40, 45, 50, 55, 60]
seeds = [random.randint(1, 2**16) for _ in range(0, N)]

try:
    pickle_file = open("cache.pickle","rb")
    cache = pickle.load(pickle_file)
    pickle_file.close()
except:
    cache = {}


path = "stats_output/"

os.makedirs(path, exist_ok=True)

data = {}

for hole_size in hole_sizes:
    for numParticles in particles:
        
        data[(numParticles, hole_size)] = []

        for seed in seeds:
            if(cache.get((numParticles, hole_size, seed))):
                data[(numParticles, hole_size)].append(cache[(numParticles, hole_size, seed)])
            else:
                data[(numParticles, hole_size,seed)] = []

                command = f"java -cp target/latticeGas-1.0-SNAPSHOT.jar -DnumParticles={numParticles} -DholeSize={hole_size} -Dseed={seed} latticeGas.Main "
                print(command)
                proc = os.popen(command)
                time = proc.readline()
                data[(numParticles,hole_size)].append(time)
                cache[(numParticles, hole_size, seed)] = time

with open("cache.pickle", "wb") as f:
    pickle.dump(cache, f)
    f.close()




plt.figure("Time vs N particles")
plt.xlabel("N particles")
plt.ylabel("Time")
for hole_size in hole_sizes:
    time = []
    errors = []
    for numParticles in particles:
        times = data[(numParticles, hole_size)]
        time.append(statistics.mean(map(float,times)))
        errors.append(statistics.stdev(map(float,times))/math.sqrt(len(times)))
    plt.errorbar(particles, time, yerr=errors, fmt="o", label=f"Hole size {hole_size}", markersize=5)


plt.legend()


plt.savefig("time_vs_N_particles.png")


plt.figure("Time vs Hole size")
plt.xlabel("Hole size")
plt.ylabel("Time")
for numParticles in particles:
    time = []
    errors = []
    for hole_size in hole_sizes:
        times = data[(numParticles, hole_size)]
        time.append(statistics.mean(map(float,times)))
        errors.append(statistics.stdev(map(float,times))/math.sqrt(len(times)))
    plt.errorbar(hole_sizes, time, yerr=errors, fmt="o", label=f"N particles {numParticles}", markersize=5)

plt.legend()
plt.savefig("time_vs_holes_sizes.png")