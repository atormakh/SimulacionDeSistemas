from matplotlib import pyplot
import data_import
import py_plot, ovito_export, ovito_render
import numpy as np
import time, math

AVG_GRID = 10
AVG_T = 10

def main(action):
    data = data_import.Data("latticeGas.txt", AVG_T)

    actions ={"graphs": lambda: py_plot.plotGraphs(data),
              "density": lambda: py_plot.plotDensity(AVG_GRID, data),
              "particles": lambda: py_plot.plotParticles(data),
              "flow": lambda: py_plot.plotFlow(AVG_GRID, data),
              "ovito": lambda: ovito_export.save("lattice_gas.xyz", data)}

    actions[action]()


data = data_import.Data("latticeGas.txt", AVG_T)
#start = time.time()
#py_plot.plotGraphs(data)

#py_plot.plotDensity(AVG_GRID,data)

#py_plot.plotParticles(data)

#py_plot.plotFlow(AVG_GRID,data)


#ovito_export.generate_lattice(data.grid_size)


# print("saving for ovito")
# ovito_export.save("lattice_gas.xyz",data)

# data = data_import.Data("latticeGas.txt", AVG_T)
# print("saving flow for ovito")
# ovito_export.flow("flow.xyz",data)

data = data_import.Data("latticeGas.txt", AVG_T)
print("saving density for ovito")
ovito_export.density("density.xyz",data, 20, 5)

ovito_render.animate_density(20)

#ovito_export.generate_wall(data.grid_size,data.hole_size,False)

# print("generating walls")
#ovito_export.generate_wall(data.grid_size, data.hole_size)

#print("rendering ovito")
#ovito_render.animate_particles()
#print("Time:", time.time() - start)

