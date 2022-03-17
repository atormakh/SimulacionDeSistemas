import argparse
import matplotlib.pyplot as plt

class Particle:
    def __init__(self):
        self.x = 0
        self.y = 0
        self.r = 0
        self.id = 0
        self.color = "b"
        self.neighbours = []

    def __str__(self):
        return "id: {}, x:{}, y:{}, r:{}".format(self.id,self.x,self.y,self.r)

    def draw(self):
        self.circle = plt.Circle((self.x,self.y),self.r, facecolor=self.color)
        plt.gca().add_patch(self.circle)


parser = argparse.ArgumentParser("Render cell index method outputs")
parser.add_argument("--static", dest="static", default="static-output.txt", help="static file input")
parser.add_argument("--dynamic", dest="dynamic", default="dynamic-output.txt", help="dynamic file input")
parser.add_argument("--neighbours", dest="neighbours", default="neighbours-output.txt", help="neighbours file input")
parser.add_argument("--id", dest="id", default="1", type=int, help="id of particle")



args = parser.parse_args()

sf = open(args.static,"r")
df = open(args.dynamic,"r")
nf = open(args.neighbours,"r")


N = int(sf.readline())
L = int(sf.readline())
T = float(df.readline())

def parse(line):
    return  line.rstrip("\n").split(" ")

static = [ parse(line) for line in sf.readlines()] #[['r1' 'id1'], ['r2' 'id2'], ['r3' 'id3']]
dynamic = [parse(line) for line in df.readlines()] #[['x1' 'y1'], ['x2' 'y2'], ['x3' 'y3']]
neighbours = [parse(line) for line in nf.readlines()]#[['id1' 'n11', 'n12', 'n13'], ['id2' 'n21', 'n22', 'n23'], ['id3' 'n31', 'n32', 'n33']]


particles = [Particle() for i in range(0,N)]

particlesDict = dict()

for idx,data in enumerate(static):
    id = int(data[1])
    p = particlesDict[id] = particles[idx]
    p.r = float(data[0])


for idx, d in enumerate(dynamic):
    p = particles[idx]
    p.x = float(d[0])
    p.y = float(d[1])


for ns in neighbours:
    id = int(ns[0])
    p = particlesDict[id]

    ns = ns[1:-1]
    for n in ns:
        if n.isnumeric():
            p.neighbours.append(particlesDict[int(n)]) 


plt.figure(figsize=(10,10))
plt.ion()
plt.show()
id = args.id
first = True
idx=0
while(True):
    particle = particlesDict[id] if first else particles[idx]

    for p in particles:
        p.color = "b"

    particle.color = "r"

    for p in particle.neighbours:
        p.color = "g"

    for p in particles:
        p.draw()


    border = 2*max(map(lambda p: p.r,particles))
    plt.gca().set_ylim(-border,L+border)
    plt.gca().set_xlim(-border,L+ border)
    plt.draw()
    if first:input("press enter to continue...")
    plt.pause(0.5)
    plt.cla()
    idx = (idx+1)%N
    first = False

