import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import data_import


plt.figure("render", figsize=(8, 8))

data = data_import.Data("zombies.txt")

radius = 11

def update(frame):
    time,z,h, data = frame
    print(time)

    plt.cla()

    X = []
    Y = []
    c = []
    r = []

    for x, y,ra, type in data:
        X.append(x)
        Y.append(y)
        r.append(ra)
        color = 'g' if type == 1 else 'b'
        c.append(color)

        circ = plt.Circle((x, y), radius=ra, color=color, fill=True)
        plt.gca().add_patch(circ)
    

    circle = plt.Circle((0, 0), radius , color='r', fill=False)

    plt.gca().add_patch(circle)

    #plt.scatter(X, Y, c=c, s=100)

    plt.ylim(-radius-1, radius+1)
    plt.xlim(-radius-1, radius+1)
    plt.gca().set_axis_off()



anim = FuncAnimation(plt.gcf(), update, data, interval = 1, save_count=1e5)


#anim.save("zombies.mp4", writer="ffmpeg", fps=15)

plt.show()





