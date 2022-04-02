import math
import numpy as np

def generate_lattice(size=200):
    with open("static.xyz","w") as f:
        f.write("{}\ncomment\n".format(size*size))
        for i in range(0,size):
            for j in range(0,size):
                f.write("{} {} {}\n".format(i if j%2 else i-0.5,j*math.sqrt(3)/2,0))

def generate_wall(size=200, hole_size=50):
    with open("wall.xyz","w") as f:
        f.write("{}\ncomment\n".format(4*(size-1)+size-hole_size))
        i = size/2
        for j in range(0,size):
            if(j <= size/2 - hole_size/2 or j > size/2 + hole_size/2):
                f.write("{} {} {} {}\n".format(i if j%2 else i-0.5,j*math.sqrt(3)/2,0, 255,0,0))
        
        for i in range(0,size):
            for j in range(0,size):
                if(i == 0 or i == size-1 or j == 0 or j == size-1):
                    f.write("{} {} {} {}\n".format(i if j%2 else i-0.5,j*math.sqrt(3)/2,0, 255,0,0))


def save(filename, data):
    with open(filename, "w") as dump:
        for time, a, b, frame in data:
            dump.write("{}\ncomment\n".format(len(frame[-1])))
            for particle in frame[-1]:
                x,y,dir = particle
                dump.write("{} {} {}\n".format( x if y%2 else x-0.5, y * math.sqrt(3)/2, 0))
    dump.close()


directions_component = ((1,0),(1,1),(-1,1),(-1,0),(-1,-1),(1,-1))

def compute_flow(frame, grid_size, avg_grid):
    flows_x = np.zeros((int(grid_size/avg_grid), int(grid_size/avg_grid)),dtype=float)
    flows_y = np.zeros((int(grid_size/avg_grid), int(grid_size/avg_grid)),dtype=float)
    for particle in frame:
        x,y,dir = particle
        x = int(x/avg_grid)
        y = int(y/avg_grid)
        flows_x[x][y] += directions_component[int(dir)][0]
        flows_y[x][y] += directions_component[int(dir)][1]
    return (flows_x, flows_y)

def flow(filename,data, avg_grid=10, avg_t=10):
    size = int(data.grid_size/avg_grid)
    flows_x = np.zeros((size, size),dtype=float)
    flows_y = np.zeros((size, size),dtype=float)
    max_flow = len(directions_component)/2*avg_grid*avg_grid # for example, 3 particles per cell going left
    with open(filename, "w") as dump:
        for time, a, b, frame in data:
            dump.write("{}\ncomment\n".format(size*size))
            
            frame_flows_x, frame_flows_y= compute_flow(frame[-1], data.grid_size, avg_grid)
            for i in range(0,size):
                for j in range(0,size):
                    flows_x[i][j] = (flows_x[i][j]*(avg_t-1) + frame_flows_x[i][j])/avg_t
                    flows_y[i][j] = (flows_y[i][j]*(avg_t-1) + frame_flows_y[i][j])/avg_t
                    dump.write("{} {} {} {}\n".format(i*avg_grid,j*avg_grid*math.sqrt(3)/2,flows_x[i][j],flows_y[i][j]*math.sqrt(3)/2))
            