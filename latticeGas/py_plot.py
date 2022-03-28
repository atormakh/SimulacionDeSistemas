from math import sqrt
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np
import sys

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

def plot(filename, update, grid_size, iterations, interval=1):
    plt.xlim(0, grid_size)
    plt.ylim(0, grid_size)
    plt.axvline(x=grid_size/2, ymin=0, ymax=0.375, color='black')
    plt.axvline(x=grid_size/2, ymin=0.625, ymax=1, color='k')

    def animate(data):
        print(data[0])
        return update(data)

    anim = animation.FuncAnimation(
        plt.gcf(), animate, iterations, interval=interval, blit=True, save_count=sys.maxsize)
  
    #plt.pause(0.001)
    #plt.show()



    writervideo = animation.FFMpegWriter(fps=15)
    anim.save(filename + '.mp4', writer=writervideo, dpi=100)

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
        time, flow, a, b, _data = data
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
        time, flow, a, b, _data = data
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
        time, flow, a, b, _data = data
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


def plotGraphs(data):
    count = []
    flow = []
    for time, f, a,b, _data in data:
        flow.append(f)
        count.append((a,b))


    plt.figure("Lattice Gas", figsize=(10, 10))
    plt.plot(count)
    
    flow_avg = []
    for i in range(100, len(flow)):
        flow_avg.append(sum(flow[i-100:i])/100)

    m = max(flow_avg)
    flow_avg = [ x/(2*m) + 0.5 for x in flow_avg]
    #plt.plot(flow_avg)
    #plt.show()
    plt.savefig("particles.png")
