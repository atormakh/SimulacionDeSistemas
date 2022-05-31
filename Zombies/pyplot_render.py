import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
import data_import


plt.figure("render", figsize=(8, 8))

data = data_import.Data("zombies.txt")

radius = 11

def update(frame):
    time, data = frame
    print(time)


    X = []
    Y = []
    c = []

    for x, y, vx, vy, type in data:
        X.append(x)
        Y.append(y)
        c.append(type+1)
    
    plt.cla()
    circle = plt.Circle((0, 0), radius , color='r', fill=False)

    plt.gca().add_patch(circle)

    plt.scatter(X, Y, c=c, s=10)

    plt.ylim(-radius-1, radius+1)
    plt.xlim(-radius-1, radius+1)
    plt.gca().set_axis_off()



anim = FuncAnimation(plt.gcf(), update, data, interval = 1)

#anim.save("zombies.mp4", writer="imagemagick")

plt.show()





