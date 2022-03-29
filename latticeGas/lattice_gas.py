from matplotlib import pyplot
import data_import
import py_plot, ovito
import numpy as np
import time, math
AVG_GRID = 10
AVG_T = 100

def main(action):
    data = data_import.Data("latticeGas.txt", AVG_T)

    actions ={"graphs": lambda: py_plot.plotGraphs(data),
              "density": lambda: py_plot.plotDensity(AVG_GRID, data),
              "particles": lambda: py_plot.plotParticles(data),
              "flow": lambda: py_plot.plotFlow(AVG_GRID, data),
              "ovito": lambda: ovito.save("lattice_gas.xyz", data)}

    actions[action]()


data = data_import.Data("latticeGas.txt", AVG_T)
#start = time.time()
py_plot.plotGraphs(data)

#py_plot.plotDensity(AVG_GRID,data)

#py_plot.plotParticles(data)

#py_plot.plotFlow(AVG_GRID,data)

#ovito.save("lattice_gas.xyz",data)

#print("Time:", time.time() - start)

# with open("static.xyz","w") as f:
#     f.write("{}\ncomment\n".format(200*200))
#     for i in range(0,200):
#         for j in range(0,200):
#             f.write("{} {} {}\n".format(i-0.5 if j%2 else i,j*math.sqrt(3)/2,0))
