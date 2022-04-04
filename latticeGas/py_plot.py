from math import sqrt
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np
import sys
import os
import multiprocessing as mp


directions_component = ((1,0),(0.5,sqrt(3)/2),(-0.5,sqrt(3)/2),(-1,0),(-0.5,-sqrt(3)/2),(0.5,-sqrt(3)/2))


def fillGrid( data, grid_size, AVG_GRID):
    grid = np.zeros((int(grid_size/AVG_GRID), int(grid_size/AVG_GRID)),dtype=object)
    
    for x in range(int(grid_size/AVG_GRID)):
        for y in range(int(grid_size/AVG_GRID)):
            grid[x][y] = [0,0,0]
    
    for frame in data:
        for particle in frame:
            x, y, dir = particle
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][0]  += 1 / (len(data)*AVG_GRID*AVG_GRID)
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][1] += directions_component[int(dir)][0] / (len(data)*AVG_GRID*AVG_GRID)
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][2] += directions_component[int(dir)][1] / (len(data)*AVG_GRID*AVG_GRID)

    return grid

def save(animate, iterations,filename):
    def asyncSave(filename, frame):
        animate(frame)
        plt.savefig(filename)
    
    
    MAX_PROCESSES = mp.cpu_count()

    processes = []
    os.makedirs("frames/", exist_ok=True)

    for frame in iterations:
        
        while len(processes) >= MAX_PROCESSES:
            processes[0].join()
            processes.pop(0)

        frame_num = "_{:08d}".format(int(frame[0]))
        file = "frames/"+filename +  frame_num + ".png"
        save = mp.Process(target=asyncSave, args=(file, frame))
        processes.append(save)
        save.start()
    
    for process in processes:
        process.join()

    os.system("ffmpeg -y -pattern_type glob -i 'frames/"+filename + "*.png' "+filename+".mp4")
    os.system("rm -r frames/"+filename+"*.png")

def plot(filename, update, grid_size, iterations, interval=1):
    plt.xlim(0, grid_size)
    plt.ylim(0, grid_size)
    plt.axvline(x=grid_size/2, ymin=0, ymax=0.375, color='black')
    plt.axvline(x=grid_size/2, ymin=0.625, ymax=1, color='k')

    def animate(data):
        print(data[0])
        return update(data)

    #save(animate, iterations, filename)
    #return
    
    anim = animation.FuncAnimation(
        plt.gcf(), animate, iterations, interval=interval, blit=True, save_count=sys.maxsize)
  
    plt.pause(0.001)
    plt.show()

    writervideo = animation.FFMpegWriter(fps=15)
    anim.save(filename + '.mp4', writer=writervideo)

def plotDensity(avg_grid, data):
    plt.figure("Lattice Gas", figsize=(10, 10))

    grid_size = data.grid_size

    squares = np.zeros(
        (int(grid_size/avg_grid), int(grid_size/avg_grid)), dtype=object)

    for x in range(int(grid_size/avg_grid)):
        for y in range(int(grid_size/avg_grid)):
            squares[x][y] = plt.gca().add_patch(plt.Rectangle(
                (x*avg_grid, y*avg_grid), avg_grid, avg_grid, color='red', alpha=0))

        
    def update(data):
        time, a, b, _data = data
        grid = fillGrid(_data, grid_size, avg_grid)

        for x in range(int(grid_size/avg_grid)):
            for y in range(int(grid_size/avg_grid)):
                squares[x][y].set_alpha(grid[x][y][0])
        return squares.flatten()

    plot("density",update, grid_size, data, 1)

        


def plotParticles(data):
    plt.figure("Lattice Gas", figsize=(10, 10))

    num_particles = data.num_particles
    particles = [plt.gca().add_patch(plt.Circle((0, 0), 1, fc='r'))
                 for i in range(num_particles)]

    def update(data):
        time, a, b, _data = data
        for i in range(num_particles):
            x, y, dir = _data[-1][i]
            particles[i].center = (x, y)
        return particles

    plot("particles",update, data.grid_size, data, 1)


def plotFlow(avg_grid, data):
    plt.figure("Lattice Gas", figsize=(10, 10))

    grid_size = data.grid_size

    arrows = np.zeros(
        (int(grid_size/avg_grid), int(grid_size/avg_grid)), dtype=object)

    for x in range(int(grid_size/avg_grid)):
        for y in range(int(grid_size/avg_grid)):
            arrows[x][y] = plt.gca().add_line(plt.Line2D([x*avg_grid,x*avg_grid],[y*avg_grid,y*avg_grid], color='red'))

    dot = plt.gca().add_patch(plt.Circle((0, 0), 0.01, fc='green'))
    def update(data):
        time, a, b, _data = data
        plt.cla()
        plt.xlim(0, grid_size)
        plt.ylim(0, grid_size)
        plt.axvline(x=grid_size/2, ymin=0, ymax=0.375, color='black')
        plt.axvline(x=grid_size/2, ymin=0.625, ymax=1, color='k')
        grid = fillGrid(_data, grid_size, avg_grid)
        for x in range(int(grid_size/avg_grid)):
            for y in range(int(grid_size/avg_grid)):
                dx = grid[x][y][1]
                dy = grid[x][y][2]
                if dx == 0 and dy == 0:
                    arrows[x][y] = dot
                    #pass
                else:
                    len = sqrt(dx**2 + dy**2)
                    dx = avg_grid/4 * dx/len
                    dy = avg_grid/4 * dy/len
                
                    #arrows[x][y].set(xdata=[x*avg_grid,x*avg_grid+dx], ydata=[y*avg_grid,y*avg_grid+dy])
                    arrows[x][y] = plt.gca().add_patch(plt.arrow(x*avg_grid,y*avg_grid, dx,dy,width=0.5, color='green'))
        return arrows.flatten()

    plot("flow",update, grid_size, data, 1)


def plotGraphs(data,N=2000, hole_size = 50, avg_t = 10, f=False):
    count = []
    flow = []
    flow_actual = 0
    for time, a,b, _data in data:
        if f:
            old = count[-1][0] if count else a
            flow_actual = (flow_actual*(avg_t -1)+ old-a)/avg_t
            flow.append(flow_actual)
        count.append((a,b))
        
    plt.figure("Lattice Gas Particles", figsize=(10, 10))
    plt.plot(count)
    plt.title("N={} and Hole size={}".format(N, hole_size))
    plt.xlabel("time")
    plt.ylabel("particles proportion")
    plt.yticks(np.arange(0,1.05,0.05))
    plt.grid()
        
    plt.savefig("equilibrium.png")   
    #plt.show()
 
    
    if not flow:
        return

    max_flow = max(flow)

    flow = [x/max_flow for x in flow]

    plt.figure("Flow")
    plt.title("N={} and Hole size={}".format(N, hole_size))
    plt.xlabel("time")
    plt.ylabel("flow")
    plt.grid()
    plt.plot(flow)
    plt.savefig("flow.png")
   # plt.show()

   


