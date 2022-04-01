from matplotlib import pyplot
import data_import
import py_plot, ovito_export
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
#py_plot.plotGraphs(data)

#py_plot.plotDensity(AVG_GRID,data)

#py_plot.plotParticles(data)

#py_plot.plotFlow(AVG_GRID,data)


#ovito_export.generate_lattice(data.grid_size)
#ovito_export.generate_wall(data.grid_size, 50)
ovito_export.save("lattice_gas.xyz",data)

#print("Time:", time.time() - start)

