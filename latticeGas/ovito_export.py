import math

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