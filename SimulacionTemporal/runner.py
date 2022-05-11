import argparse
import random
import os
import data2D as data_import
import ovito_export
import ovito_render
from multiprocessing import Process


def save_particles():
    print("saving particles for ovito")
    data = data_import.Data("ElectricField.txt")
    ovito_export.save("dynamic.xyz", data)


def run(v=1e3, r=0, dt=1e-15, dt2=1e-12, tf=1, method="Gear"):


    dir = "output/{}_{}_{}_{}_{}_{}".format(v, r, dt, dt2, tf, method)
    os.makedirs(dir, exist_ok=True)
    os.chdir(dir)

    command = f"java -cp ../../target/SimulacionTemporal-1.0-SNAPSHOT.jar \
            -Ddt={dt} -Ddt2={dt2} -Dtf={tf} \
            -Dv={v} -Dr={r} \
            -Dmethod={method} \
            ElectricFieldSystem"

    proc = os.popen(command)
    proc.readlines()
    print("FINISH JAVA")
    data = data_import.Data("ElectricField.txt")


    print("generating walls")
    ovito_export.generate_grid()

    save_particles_p = Process(target=save_particles)
    save_particles_p.start()
    save_particles_p.join()

    print("rendering ovito particles")
    ovito_render.animate()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run lattice gas simulation")
    parser.add_argument("-m", "--method", type=str,
                        default="Gear", help="Integration Method")
    parser.add_argument("-dt", "--timeStep", type=float,
                        default=1e-15, help="Time step for integration")
    parser.add_argument("-dt2", "--SaveStep", type=float,
                        default=1e-13, help="Time step for saving")
    parser.add_argument("-r", "--yPos", type=float, default=0,
                        help="Y position of the particle")
    parser.add_argument("-v", "--velocity", type=float,
                        default=5e3, help="velocity")
    parser.add_argument("-t", "--timeMultiplier", type=float,
                        default=1, help="time multiplier")

    args = parser.parse_args()

    run(method=args.method,
        dt=args.timeStep,
        dt2=args.SaveStep,
        r=args.yPos,
        v=args.velocity)
