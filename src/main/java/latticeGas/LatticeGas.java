package latticeGas;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileWriter;
import java.io.IOException;

public class LatticeGas {

    int gridSize;
    int numParticles;
    final int holeSize = 50;
    int directions = 6;
    Node[][] lattice;
    //   2  1
    //  3  c  0
    //   4  5
    //

    static int[][] evenDirectionsArray = {{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {0, -1}, {1, -1}};
    static int[][] oddDirectionsArray = {{1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};


    public LatticeGas(int gridSize, int numParticles) {
        this.gridSize = gridSize;
        this.numParticles = numParticles;
    }

    public void propagate(){
        int [][] directionsArray;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                for (int k = 0; k < directions; k++) {
                    if(lattice[i][j].getOutDirection(k)){
                        directionsArray = j%2==0?evenDirectionsArray:oddDirectionsArray;
                        int x = i + directionsArray[k][0];
                        int y = j + directionsArray[k][1];
                        if(x>=0 && x<gridSize && y>=0 && y<gridSize) {
                            //lattice[i][j].setInDirection(k, false);
                            lattice[x][y].setInDirection(k, true);
                        }
                    }
                }
            }
        }
    }

    public void run(long iterations, FileWriter out) throws IOException {

        lattice = new Node[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                lattice[i][j] = new Node();
                if(i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1) {
                    lattice[i][j].setBoundary(true);
                }
            }
        }

        //set vertical wall in the middle of the lattice with a hole
        for (int i = 1; i < gridSize; i++) {
            if(i<(gridSize/2 - holeSize/2) || i> (gridSize/2 + holeSize/2)) lattice[gridSize/2][i].setBoundary(true);
        }


        for(int i=0; i<numParticles;i++){
            int x,y,direction;
            do{
                x= (int) (Math.random() * (gridSize/2-2)) + 1;
                y = (int) (Math.random() * (gridSize-2)) + 1;
                direction = (int) (Math.random() * directions);
            }while (lattice[x][y].getInDirection(direction));
            lattice[x][y].setInDirection(direction, true);
        }

/*        numParticles=directions;
        for(int i=0; i<directions; i++) {
            lattice[gridSize / 2][gridSize / 2].setInDirection(i, true);
        }*/
        out.write(numParticles + " " + gridSize + " " + iterations + "\n");
        for (int iter = 0; iter < iterations; iter++) {
            propagate();
            out.write((iter + 1) + "\n");
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    for (int k = 0; k < directions; k++) {
                        if(lattice[i][j].getInDirection(k)){
                            out.write(i + " " + j + "\n");
                        }
                    }
                    lattice[i][j].recalculate();
                }
            }
        }




    }
}
