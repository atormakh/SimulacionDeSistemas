import math
import numpy as np

MULTIPLIER = 1000

def generate_wall(width=0.24, height=0.09, hole_size=0.03): 
    filename = "wall.xyz"

    wall_diameter = 0.0015*2

    height_n = int(height/wall_diameter)
    width_n = int(width/wall_diameter)
    hole_n = int(hole_size/wall_diameter)


    with open(filename, "w") as f:
        f.write("{}\ncomment\n".format(2*width_n + 3*height_n -hole_n - 5))
        i = width/2
        for j in range(0, height_n):
            j = j*wall_diameter;
            if(j <= height/2 - hole_size/2 - wall_diameter/2 or j > height/2 + hole_size/2 + wall_diameter/2):        
                f.write("{} {} {} {}\n".format(MULTIPLIER*i, MULTIPLIER*j, wall_diameter*MULTIPLIER/2, 255, 0, 0))

        for i in range(0, width_n):
            for j in range(0, height_n):
                if(i == 0 or i == width_n-1 or j == 0 or j == height_n-1):                
                    f.write("{} {} {} {}\n".format(MULTIPLIER*i*wall_diameter, MULTIPLIER*j*wall_diameter, wall_diameter*MULTIPLIER/2, 255, 0, 0))


def save(filename, data,radius=0.0015):
    with open(filename, "w") as dump:
        for time, a, b, frame in data:
            dump.write("{}\ncomment\n".format(len(frame)))
            for particle in frame:
                x, y, vx, vy = particle
                dump.write("{} {} {} {} {}\n".format(MULTIPLIER*x, MULTIPLIER*y, vx*MULTIPLIER, vy*MULTIPLIER,radius*MULTIPLIER))
    dump.close()
