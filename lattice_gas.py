

import math
from matplotlib import animation, pyplot as plt


num_partices = 0
grid_size = 0



def parse(line):
    return  line.rstrip("\n").split(" ")

f = open('latticeGas.txt', 'r')

num_partices,grid_size, iterations = map(int,parse(f.readline()))



plt.figure("Lattice Gas",figsize=(10,10))
particles = [plt.gca().add_patch(plt.Circle((0,0),1, fc='r')) for i in range(num_partices)]

height_multiplier = math.sqrt(3)/2

plt.xlim(0,grid_size)
plt.ylim(0,grid_size)

lines = [ parse(line) for line in f.readlines() ]
line = 0
def update(frame_number):
    global line
    line+=1
    print(frame_number)
    for i in range(num_partices):
        x,y = map(float,lines[line])
        line+=1
        #if(i%2 == 0): x -= 0.5
        # y*= height_multiplier
        particles[i].center = (x,y)
    
    return particles


anim = animation.FuncAnimation(plt.gcf(), update, iterations-1, interval=1, blit=True)

plt.axvline(x=grid_size/2,ymin=0,ymax=0.375, color='black')
plt.axvline(x=grid_size/2,ymin=0.625,ymax=1, color='k')

writervideo = animation.FFMpegWriter(fps=60) 
#plt.show()
anim.save('lattice_gas.avi', writer=writervideo)

