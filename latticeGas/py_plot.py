from math import sqrt
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np

directions_component = ((-1,0),(0.5,sqrt(3)/2),(-0.5,sqrt(3)/2),(-1,0),(-0.5,-sqrt(3)/2),(0.5,-sqrt(3)/2))

def fillGrid(frame, data, grid_size, AVG_GRID, AVG_T):
    grid = np.zeros((int(grid_size/AVG_GRID), int(grid_size/AVG_GRID)),dtype=object)
    grid.fill([0,0,0])
    start = frame - AVG_T
    if start < 0:
        start = 0
    for f in range(start, frame):
        for particle in data[f]:
            x, y, dir = particle
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][0] += 1 / (AVG_T*AVG_GRID*AVG_GRID)
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][1] += directions_component[int(dir)][0] / (AVG_T*AVG_GRID*AVG_GRID)
            grid[int(x/AVG_GRID)][int(y/AVG_GRID)][2] += directions_component[int(dir)][1] / (AVG_T*AVG_GRID*AVG_GRID)

    return grid

def plot(update, grid_size, iterations, interval=20):
    plt.xlim(0, grid_size)
    plt.ylim(0, grid_size)
    anim = animation.FuncAnimation(
        plt.gcf(), update, iterations, interval=1, blit=True)
    plt.axvline(x=grid_size/2, ymin=0, ymax=0.375, color='black')
    plt.axvline(x=grid_size/2, ymin=0.625, ymax=1, color='k')

    plt.show()
    writervideo = animation.FFMpegWriter(fps=15)
    anim.save('lattice_gas.avi', writer=writervideo)


def plotDensity(avg_grid, avg_t, grid_size, data):
    plt.figure("Lattice Gas", figsize=(10, 10))

    squares = np.zeros(
        (int(grid_size/avg_grid), int(grid_size/avg_grid)), dtype=object)

    for x in range(int(grid_size/avg_grid)):
        for y in range(int(grid_size/avg_grid)):
            squares[x][y] = plt.gca().add_patch(plt.Rectangle(
                (x*avg_grid, y*avg_grid), avg_grid, avg_grid, color='red', alpha=0))

        
    def update(frame_number):
        grid = fillGrid(frame_number, data, grid_size, avg_grid, avg_t)

        for x in range(int(grid_size/avg_grid)):
            for y in range(int(grid_size/avg_grid)):
                squares[x][y].set_alpha(grid[x][y][0])
        return squares.flatten()

    plot(update, grid_size, len(data), 1)

        


def plotParticles(grid_size, data):
    plt.figure("Lattice Gas", figsize=(10, 10))

    num_particles = len(data[0])
    particles = [plt.gca().add_patch(plt.Circle((0, 0), 1, fc='r'))
                 for i in range(num_particles)]

    def update(frame_number):
        print(frame_number)
        for i in range(num_particles):
            x, y, dir = data[frame_number][i]
            particles[i].center = (x, y)
        return particles

    plot(update, grid_size, len(data), 1)


# def plotFlow(avg_grid, avg_t, grid_size, data):
#     plt.figure("Lattice Gas", figsize=(10, 10))

#     arrows = np.zeros(
#         (int(grid_size/avg_grid), int(grid_size/avg_grid)), dtype=object)

#     for x in range(int(grid_size/avg_grid)):
#         for y in range(int(grid_size/avg_grid)):
#             arrows[x][y] = plt.gca().add_line(plt.Line2D([x*avg_grid,0],[y*avg_grid,0], color='red'))

        
#     def update(frame_number):
#         print(frame_number)
#         grid = fillGrid(frame_number, data, grid_size, avg_grid, avg_t)
#         plt.clf()
#         for x in range(int(grid_size/avg_grid)):
#             for y in range(int(grid_size/avg_grid)):
#                 arrows[x][y].set(xdata=[x*avg_grid,grid[x][y][1]], ydata=[y*avg_grid,grid[x][y][2]])
#         return arrows.flatten()

#     plot(update, grid_size, len(data), 1)