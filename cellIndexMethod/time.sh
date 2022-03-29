#!/bin/bash
L=20
rc=1
r=0.25
M=( 16 10 8 4 1)
N=(10 100 1000 10000)

for n in ${N[@]};
 do
    echo "running brute force N=$n"
    java -cp target/cellIndexMethod-1.0-SNAPSHOT.jar -Dgenerate=true -DN=$n -Dbf=true cellIndexMethod.Main

    for m in ${M[@]}
    do
     echo "running Cell index method N=$n and M=$m"
    java -cp target/cellIndexMethod-1.0-SNAPSHOT.jar -DN=$n -DL=$L -DR=$r -DRC=${rc} -DM=${m} cellIndexMethod.Main
    done
done