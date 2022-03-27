
def save(filename, data):
    with open(filename, "w") as dump:
        for frame in data:
            dump.write("{}\ncomment\n".format(len(frame)))
            for particle in frame:
                dump.write("{} {} {}\n".format(particle[0], particle[1], 0))
    dump.close()