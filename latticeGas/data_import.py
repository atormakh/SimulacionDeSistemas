



def parse(line):
    return line.rstrip("\n").split(" ")


def import_data(filename):

    f = open(filename, 'r')
    num_partices, grid_size = map(int, parse(f.readline()))
    data = []
    time = f.readline()
    while time:
        data.append([])
        for j in range(num_partices):
            x, y, dir = map(float, parse(f.readline()))
            data[-1].append((x, y, dir))
        time = f.readline()
    f.close()

    return num_partices, grid_size, data