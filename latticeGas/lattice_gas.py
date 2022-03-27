from matplotlib import pyplot
import data_import
import py_plot, ovito
import numpy as np


#particles,grid_size,data,flow,count= data_import.import_data("latticeGas.txt")





AVG_GRID = 20
AVG_T = 10

data = data_import.Data("latticeGas.txt", AVG_T)

py_plot.plotGraphs(data)

#py_plot.plotDensity(AVG_GRID,data)

#py_plot.plotParticles(data)

#py_plot.plotFlow(AVG_GRID,data)

#ovito.save("lattice_gas.xyz",data)




