import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import data_import


plt.figure("render", figsize=(8, 8))

data = data_import.Data("zombies.txt")

radius = 11
i = 0
def update(frame):
    global i
    time,z,h, data = frame
    print(i)
    i+=1

    plt.cla()

    X = []
    Y = []
    DX = []
    DY = []
    c = []
    r = []

    for x, y, dx,dy ,ra, type in data:
        X.append(x)
        Y.append(y)
        r.append(ra)
        DX.append(dx)
        DY.append(dy)
        color = 'g' if type == 1 else 'b'
        c.append(color)

        circ = plt.Circle((x, y), radius=ra, color=color, fill=True)
        plt.gca().add_patch(circ)
        #line = plt.Line2D((x, dx), (y, dy), color='k')
        #plt.gca().add_line(line)
    

    circle = plt.Circle((0, 0), radius , color='r', fill=False)

    plt.gca().add_patch(circle)

    #plt.scatter(DX,DY, marker="x")

    #plt.scatter(X, Y, c=c, s=100)

    plt.ylim(-radius-1, radius+1)
    plt.xlim(-radius-1, radius+1)
    plt.gca().set_axis_off()



anim = FuncAnimation(plt.gcf(), update, data, interval = 1, save_count=1e5)


anim.save("zombies.mp4", writer="ffmpeg", fps=15)

#plt.show()





