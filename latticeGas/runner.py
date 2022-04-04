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
    data = data_import.Data("latticeGas.txt", 1)
    ovito_export.save("lattice_gas.xyz", data)

def save_flow(avg_grid, avg_t):
    print("saving flow for ovito")
    data = data_import.Data("latticeGas.txt", 1)
    ovito_export.flow("flow.xyz", data, avg_grid, avg_t)

def save_density(avg_grid, avg_t):
    print("saving density for ovito")
    data = data_import.Data("latticeGas.txt", 1)
    ovito_export.density("density.xyz", data, avg_grid, avg_t)

def run(N = 2000, hole_size=50, seed = 0, avg_grid = 10, avg_t=10, threshold=0.05, max_iterations=5000, render=True):
    if not seed: seed = random.randint(1, 2**16)

    dir = "output/{}_{}_{}_{}_{}_{}".format(N,hole_size,seed,avg_grid,avg_t,threshold)
    os.makedirs(dir, exist_ok=True)
    os.chdir(dir)

    proc = os.popen(f"java -cp ../../target/latticeGas-1.0-SNAPSHOT.jar -DnumParticles={N} -DholeSize={hole_size} -Dseed={seed} -Dthreshold={threshold} -DmaxIterations={max_iterations} latticeGas.Main")
    print(proc.readline())
    
    data = data_import.Data("latticeGas.txt", 1)

    py_plot.plotGraphs(data,N, hole_size, avg_t, True)

    if not render:
        return

    print("generating walls")
    ovito_export.generate_wall(data.grid_size, data.hole_size, False)
    ovito_export.generate_wall(data.grid_size, data.hole_size, True)

    save_particles_p = Process(target=save_particles)
    save_particles_p.start()

    save_flow_p = Process(target=save_flow, args=(avg_grid, avg_t))
    save_flow_p.start()

    save_density_p = Process(target=save_density, args=(avg_grid, avg_t))
    save_density_p.start()

    save_density_p.join()
    save_flow_p.join()
    save_particles_p.join()
    
    print("rendering ovito particles")    
    #animate_particles = Process(target=ovito_render.animate_particles)
    ovito_render.animate_particles()

    print("\nrendering ovito density")
    #animate_density= Process(target=ovito_render.animate_density, args=(avg_grid,))
    ovito_render.animate_density(avg_grid)
    
    print("\nrendering ovito flow")
    #animate_flow = Process(target=ovito_render.animate_flow)
    ovito_render.animate_flow()

    #animate_particles.start()
    #animate_flow.start()
    #animate_density.start()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run lattice gas simulation")
    parser.add_argument("-n", "--numParticles", type=int, default=2000, help="Number of particles")
    parser.add_argument("-s", "--seed", type=int, default=0, help="Seed for random number generator")
    parser.add_argument("-g", "--avgGrid", type=int, default=10, help="Average grid size")
    parser.add_argument("-t", "--avgT", type=int, default=10, help="Average time")
    parser.add_argument("-hs", "--holeSize", type=int, default=50, help="Hole size")
    parser.add_argument("-th", "--threshold", type=float, default=0.05, help="threshold")
    parser.add_argument("-m", "--maxIterations", type=int, default=5000, help="max iterations")
    parser.add_argument("-r","--render", type=bool, default=False, help="Render ovito")

    args = parser.parse_args()

    run(args.numParticles, args.holeSize, args.seed, args.avgGrid, args.avgT, args.threshold, args.maxIterations, args.render)
