
def save(filename, data):
    with open(filename, "w") as dump:
        for time, flow, a, b, frame in data:
            dump.write("{}\ncomment\n".format(len(frame[-1])))
            for particle in frame[-1]:
                dump.write("{} {} {}\n".format(particle[0], particle[1], 0))
    dump.close()