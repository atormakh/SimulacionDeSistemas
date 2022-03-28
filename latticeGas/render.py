import lattice_gas
from multiprocessing import Process

actions = ["graphs", "density", "particles", "flow", "ovito"]

processes = [Process(target=lattice_gas.main, args=(action,)) for action in actions]

for process in processes:
    process.start()

for process in processes:
    process.join()

    

