

import math,random
import numpy as np
from matplotlib import animation, pyplot as plt



def parse(line):
    return  line.rstrip("\n").split(" ")

f = open('latticeGas.txt', 'r')
num_partices, grid_size = map(int,parse(f.readline()))
data = []
time = f.readline()
while time:
    data.append([])
    for j in range(num_partices):
        x,y,dir = map(float,parse(f.readline()))
        data[-1].append((x,y,dir))
    time = f.readline()
f.close()

iterations = len(data)

AVG_GRID = 5
AVG_T = 15

plt.figure("Lattice Gas",figsize=(10,10))

squares = np.zeros((int(grid_size/AVG_GRID),int(grid_size/AVG_GRID)),dtype=object)

for x in range(int(grid_size/AVG_GRID)):
    for y in range(int(grid_size/AVG_GRID)):
        squares[x][y]= plt.gca().add_patch(plt.Rectangle((x*AVG_GRID,y*AVG_GRID),AVG_GRID,AVG_GRID,color='red', alpha=0))

def fillGrid(frame):
    grid = np.zeros((int(grid_size/AVG_GRID),int(grid_size/AVG_GRID)))
    start = frame - AVG_T
    if start < 0: start = 0
    for f in range(start,frame):
        for particle in data[f]:
            x,y,dir = particle
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)] += 1/(AVG_T*AVG_GRID*AVG_GRID)
    return grid



particles = [plt.gca().add_patch(plt.Circle((0,0),1, fc='r')) for i in range(num_partices)]

plt.xlim(0,grid_size)
plt.ylim(0,grid_size)

def update(frame_number):
    print(frame_number)
    for i in range(num_partices):
        x,y,dir = data[frame_number][i]
        particles[i].center = (x,y)    
    return random.choices(particles,k=100)

    # grid = fillGrid(frame_number)
    
    # for x in range(int(grid_size/AVG_GRID)):
    #     for y in range(int(grid_size/AVG_GRID)):
    #         squares[x][y].set_alpha(grid[x][y])
    # return squares.flatten()


with open("dump.xyz","w") as dump:
    for i in range(iterations):
        dump.write("{}\ncomment\n".format(num_partices))
        for j in range(num_partices):
            dump.write("{} {} {}\n".format(data[i][j][0],data[i][j][1],0))
    dump.close()

anim = animation.FuncAnimation(plt.gcf(), update, len(data), interval=1, blit=True)

plt.axvline(x=grid_size/2,ymin=0,ymax=0.375, color='black')
plt.axvline(x=grid_size/2,ymin=0.625,ymax=1, color='k')

plt.show()

writervideo = animation.FFMpegWriter(fps=15) 
anim.save('lattice_gas.avi', writer=writervideo)

