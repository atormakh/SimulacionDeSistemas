import math
import numpy as np

MULTIPLIER = 1000


def generate_wall(width=0.24, height=0.09, hole_size=0.03):
    filename = "wall.xyz"

    wall_diameter = 0.0015*2/5

    height_n = int(height/wall_diameter)
    height_diameter = height/height_n
    width_n = int(width/wall_diameter)
    width_diameter = width/width_n

    hole_n = int((height/2-hole_size/2)/wall_diameter)
    hole_diameter = (height/2-hole_size/2)/hole_n

    with open(filename, "w") as f:
        f.write("{}\ncomment\n".format(2*width_n + 2*height_n + 2*hole_n))
        i = width/2
        for j in range(0, hole_n):
            idx = j
            j = idx*wall_diameter + height/2 + hole_size/2 + hole_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, hole_diameter*MULTIPLIER/2, 255, 0, 0))
            j = -idx*wall_diameter + height/2 - hole_size/2 - hole_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, hole_diameter*MULTIPLIER/2, 255, 0, 0))

        for i in range(width_n):
            i = i*width_diameter+width_diameter/2
            j = -width_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, width_diameter*MULTIPLIER/2, 255, 0, 0))
            j = height+width_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, width_diameter*MULTIPLIER/2, 255, 0, 0))

        for j in range(height_n):
            i = -height_diameter/2
            j = j*height_diameter + height_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, height_diameter*MULTIPLIER/2, 255, 0, 0))
            i = width+height_diameter/2
            f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER *
                    j, height_diameter*MULTIPLIER/2, 255, 0, 0))


def save(filename, data, radius=0.0015):
    with open(filename, "w") as dump:
        for time, a, b, m, t, frame in data:
            dump.write("{}\ncomment\n".format(len(frame)))
            for particle in frame:
                x, y, vx, vy = particle
                dump.write("{} {} {} {} {}\n".format(
                    MULTIPLIER*x, MULTIPLIER*y, vx*MULTIPLIER, vy*MULTIPLIER, radius*MULTIPLIER))
    dump.close()
