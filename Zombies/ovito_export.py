import math
import numpy as np


def generate_wall():
    filename = "static.xyz"
    with open(filename, "w") as f:
        f.write("{}\ncomment\n".format(1))
        f.write("{} {} {}\n".format(0, 0, 11))


def save(filename, data):
    with open(filename, "w") as dump:
        for time, z, h, frame in data:
            dump.write("{}\ncomment\n".format(len(frame)))
            for particle in frame:
                x, y, dx, dy, ra, type = particle
                red = 0 if type == 1 else 1
                green = 1 if type == 1 else 0
                dump.write("{} {} {} {} {}\n".format(x, y, ra, red, green))
    dump.close()


if __name__ == "__main__":
   import data_import
   data = data_import.Data("zombies.txt")
   generate_wall()
   save("zombies.xyz", data)