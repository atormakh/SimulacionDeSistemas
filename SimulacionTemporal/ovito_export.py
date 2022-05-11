MULTIPLIER = 1000

N = 16
D = 1e-8


def generate_grid():

    with open("grid.xyz", "w") as dump:
        dump.write("{}\ncomment\n".format(N*N))
        for i in range(N):
            for j in range(N):
                c = (255, 0, 0) if (i+j) % 2 == 0 else (0, 0, 255)
                dump.write("{} {} {} {} {}\n".format(
                    (i+1)*D, j*D, c[0], c[1], c[2]))
        dump.close()


def save(filename, data):
    with open(filename, "w") as dump:
        for t, x, y, vx, vy in data:
            dump.write("1\ncomment\n")
            dump.write("{} {} {}\n".format(x, y, 255))
        dump.close()
