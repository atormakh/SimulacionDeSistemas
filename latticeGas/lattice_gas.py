import data_import
import py_plot,ovito
import numpy as np


particles,grid_size,data = data_import.import_data("latticeGas.txt")

iterations = len(data)

AVG_GRID = 10
AVG_T = 20



#py_plot.plotDensity(AVG_GRID,AVG_T,grid_size,data)

#py_plot.plotParticles(grid_size,data)

py_plot.plotFlow(AVG_GRID,AVG_T,grid_size,data)

#ovito.save("lattice_gas.xyz",data)




