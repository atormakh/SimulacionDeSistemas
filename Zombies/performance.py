import os
import data_import
import time
import numpy as np
import matplotlib.pyplot as plt


def exec(dt=1e-3, output_dt=1e-2, tf=60*5, vz=2, nh=10):

    command = f"java -cp target/Zombies-1.0.jar \
                -Ddt={dt} -DoutputDt={output_dt} -Dtf={tf} \
                -Dvz={vz} -Dnh={nh} \
                Main"
    proc = os.popen(command)
    proc.readlines()
    proc.close()
    data = data_import.Data("zombies.txt")
    return data


NH = [50,100,200,400]
means = []
errors = []
for n in NH:
    execs = []
    for i in range(3):
        start = time.time()
        data = exec(nh=n, dt=1e-1, output_dt=1e-1)
        execs.append(time.time()-start)
    means.append(np.mean(execs))
    errors.append(np.std(execs)/np.sqrt(len(execs)))

plt.errorbar(NH, means, yerr=errors)
plt.ylabel("Execution time (s)")
plt.xlabel("Number of humans")
plt.show()
