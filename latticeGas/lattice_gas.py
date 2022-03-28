from matplotlib import pyplot
import data_import
import py_plot, ovito
import numpy as np

AVG_GRID = 10
AVG_T = 1

def main(action):
    data = data_import.Data("latticeGas.txt", AVG_T)

    actions ={"graphs": lambda: py_plot.plotGraphs(data),
              "density": lambda: py_plot.plotDensity(AVG_GRID, data),
              "particles": lambda: py_plot.plotParticles(data),
              "flow": lambda: py_plot.plotFlow(AVG_GRID, data),
              "ovito": lambda: ovito.save("lattice_gas.xyz", data)}

    actions[action]()


data = data_import.Data("latticeGas.txt", AVG_T)

#py_plot.plotGraphs(data)

py_plot.plotDensity(AVG_GRID,data)

#py_plot.plotParticles(data)

#py_plot.plotFlow(AVG_GRID,data)

#ovito.save("lattice_gas.xyz",data)

