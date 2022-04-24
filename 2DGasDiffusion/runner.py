import argparse
import random
import os
import data_import
import ovito_export
import ovito_render
import py_plot
from multiprocessing import Process


def save_particles():
    print("saving particles for ovito")
    data = data_import.Data("2DGasDiffusion.txt")
    ovito_export.save("gas.xyz", data)


BOX_HEIGHT = 0.09
BOX_WIDTH = 0.24


def run(N=2000, hole_size=0.02, seed=0, threshold=0.05, max_iterations=100000, velocity=0.01, time_multiplier=1, render=True):
    if not seed:
        seed = random.randint(1, 2**16)

    dir = "output/{}_{}_{}_{}_{}_{}".format(N, hole_size, seed, threshold, velocity, time_multiplier)
    os.makedirs(dir, exist_ok=True)
    os.chdir(dir)

    proc = os.popen(
        f"java -cp ../../target/gasDiffusion-1.0-SNAPSHOT.jar -DnumParticles={N} -DholeSize={hole_size} -Dseed={seed} -Dthreshold={threshold} -DmaxIterations={max_iterations} -DinitialVelocity={velocity} -DtimeMultiplier={time_multiplier} gasDiffusion.Main")
    print(proc.readlines())
    # proc.readlines()
    print("FINISH JAVA")
    data = data_import.Data("2DGasDiffusion.txt")

    py_plot.plotGraphs(data, N, hole_size, True)

    if not render:
        return

    print("generating walls")
    ovito_export.generate_wall(0.24, 0.09, hole_size)

    save_particles_p = Process(target=save_particles)
    save_particles_p.start()
    save_particles_p.join()

    print("rendering ovito particles")
    ovito_render.animate_particles()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run lattice gas simulation")
    parser.add_argument("-n", "--numParticles", type=int,
                        default=10, help="Number of particles")
    parser.add_argument("-s", "--seed", type=int, default=1,
                        help="Seed for random number generator")
    parser.add_argument("-hs", "--holeSize", type=float,
                        default=0.02, help="Hole size")
    parser.add_argument("-th", "--threshold", type=float,
                        default=0.05, help="threshold")
    parser.add_argument("-m", "--maxIterations", type=int,
                        default=100000, help="max iterations")
    parser.add_argument("-v", "--velocity", type=float,
                        default=0.01, help="velocity")
    parser.add_argument("-t", "--timeMultiplier", type=float,
                        default=1, help="time multiplier")
    parser.add_argument("-r", "--render", type=bool,
                        default=False, help="Render ovito")

    args = parser.parse_args()

    run(args.numParticles, args.holeSize, args.seed,
        args.threshold, args.maxIterations, args.velocity, args.timeMultiplier, True)
