package latticeGas;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LatticeGas {

    int gridSize;
    int numParticles;
    final int holeSize;
    final int directions = 6;
    Random random;
    Node[][] lattice;
    //   2  1
    //  3  c  0
    //   4  5
    //

    static int[][] oddDirectionsArray = {{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {0, -1}, {1, -1}};
    static int[][] evenDirectionsArray = {{1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};


    public LatticeGas(int gridSize, int numParticles, int holeSize, int seed) {
        this.gridSize = gridSize;
        this.numParticles = numParticles;
        this.holeSize = holeSize;
        this.random = new Random(seed);
    }

    public void propagate() {
        int[][] directionsArray;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                for (int k = 0; k < directions; k++) {
                    if (lattice[i][j].getOutDirection(k)) {
                        directionsArray = j % 2 == 0 ? evenDirectionsArray : oddDirectionsArray;
                        int x = i + directionsArray[k][0];
                        int y = j + directionsArray[k][1];
                        if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
                            lattice[x][y].setInDirection(k, true);
                        }
                    }
                }
            }
        }
    }
    public void run(long maxIterations, float threshold,  FileWriter out) throws IOException {

        lattice = new Node[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                lattice[i][j] = new Node(random.nextDouble());
                if(i==0 || i==gridSize-1) {
                    lattice[i][j].setVerticalWall(true);
                    lattice[i][j].setBoundary(true);
                }
                if(j==0 || j==gridSize-1) {
                    lattice[i][j].setHorizontalWall(true);
                    lattice[i][j].setBoundary(true);
                }

            }
        }

        //set vertical wall in the middle of the lattice with a hole
        for (int i = 0; i < gridSize; i++) {
            if (i < (gridSize / 2 - holeSize / 2) || i > (gridSize / 2 + holeSize / 2)) {
                lattice[gridSize / 2][i].setVerticalWall(true);
                lattice[gridSize / 2][i].setBoundary(true);
            }
        }


        for (int i = 0; i < numParticles; i++) {
            int x, y, direction;
            do {
                x = random.nextInt(gridSize/2-2) + 1;
                y = random.nextInt(gridSize-2) + 1;
                direction = random.nextInt(directions);
            } while (lattice[x][y].getInDirection(direction));
            lattice[x][y].setInDirection(direction, true);
        }




        out.write(numParticles + " " + gridSize+ " " + holeSize + "\n");
        int iter;
        float a = 1;
        float b = 0;
        float c = 1;
        for (iter = 0; iter < maxIterations && c > threshold; iter++) {
            propagate();
            out.write((iter + 1)  + " " + a + " " + b +  "\n");
            a = b = c = 0;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    for (int k = 0; k < directions; k++) {
                        if (lattice[i][j].getInDirection(k)) {
                            c = i < gridSize/2 ? a++ : b++;
                            out.write(i + " " + j + " " + k + "\n");
                        }
                    }
                    lattice[i][j].recalculate();
                }
            }
            a /= numParticles;
            b /= numParticles;
            c = Math.abs(a-b)/2;
        }
        System.out.println(iter);


    }
}
