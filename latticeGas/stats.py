import os
import random
import sys
import matplotlib.pyplot as plt
import matplotlib
import math
import py_plot 


os.popen("mvn package").read()

seed = 2022
random.seed(seed)
N=5
particles = [2000, 3000, 5000]
hole_sizes = [40,50,60]
seeds = [random.randint(1, 2**16) for _ in range(0,N)]
particles_fps=[]
hole_sizes_fps=[]


for hole_size in hole_sizes:
    for numParticles in particles:
        for seed in seeds:
            command = f"java -cp target/latticeGas-1.0-SNAPSHOT.jar -DnumParticles={numParticles} -DholeSize={hole_size} -Dseed={seed} latticeGas.Main "
            print(command)
            proc = os.popen(command)
            print(proc.readline())

    

    