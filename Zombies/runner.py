import argparse
import os
import data_import
import ovito_export
import ovito_render


def run(dt=1e-2, output_dt=1e-1, tf=60*3, vz=3, nh=10, np=0):

    path = os.path.abspath(os.curdir)
    command = f"java -cp {path}/target/Zombies-1.0.jar \
                -Ddt={dt} -DoutputDt={output_dt} -Dtf={tf} \
                -Dvz={vz} -Dnh={nh} \
                -Dnp={np} \
                Main"


    dir = "output/np_{}/dt_{}/out_{}/tf_{}/vz_{}/nh_{}/".format(
        np, dt, output_dt, tf, vz, nh)

    os.makedirs(dir, exist_ok=True)
    os.chdir(dir)

    proc = os.popen(command)
    proc.readlines()
    print("FINISH JAVA")
    data = data_import.Data("zombies.txt")

    print("generating walls")
    ovito_export.generate_wall()

    print("saving particles for ovito")
    data = data_import.Data("zombies.txt")
    ovito_export.save("zombies.xyz", data)

    print("rendering ovito particles")
    ovito_render.animate()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Run zombie simulation")
    parser.add_argument("-dt", "--timeStep", type=float,
                        default=1e-2, help="Time step for simulation")
    parser.add_argument("-outputDt", "--saveStep", type=float,
                        default=2e-1, help="Time step for saving")
    parser.add_argument("-tf", "--maxTime", type=float, default=3*60,
                        help="Max time for simulation")
    parser.add_argument("-vz", "--zombieVelocity", type=float,
                        default=3, help="velocity for zombie")
    parser.add_argument("-nh", "--numberHumans", type=int,
                        default=10, help="Human quantity")
    parser.add_argument("-np", "--neutralizeProb", type=float,
                        default=0, help="neutralize probability")

    args = parser.parse_args()

    run(dt=args.timeStep, output_dt=args.saveStep, tf=args.maxTime,
        vz=args.zombieVelocity, nh=args.numberHumans, np=args.neutralizeProb)
