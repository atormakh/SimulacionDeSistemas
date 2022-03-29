import math
def save(filename, data):
    with open(filename, "w") as dump:
        for time, flow, a, b, frame in data:
            dump.write("{}\ncomment\n".format(len(frame[-1])))
            for particle in frame[-1]:
                x,y,dir = particle
                dump.write("{} {} {}\n".format( x-0.5 if y%2 else x, y * math.sqrt(3)/2, 0))
    dump.close()