package latticeGas;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    double[][] directionsComponent = {
            {1, 0},
            {0.5, Math.sqrt(3) / 2},
            {-0.5, Math.sqrt(3) / 2},
            {-1, 0},
            {-0.5, -Math.sqrt(3) / 2},
            {0.5, -Math.sqrt(3) / 2}
    };

    static int[][] evenDirectionsArray = {{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {0, -1}, {1, -1}};
    static int[][] oddDirectionsArray = {{1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};


    public LatticeGas(int gridSize, int numParticles) {
        this.gridSize = gridSize;
        this.numParticles = numParticles;
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
                            //lattice[i][j].setInDirection(k, false);
                            lattice[x][y].setInDirection(k, true);
                        }
                    }
                }
            }
        }
    }

    double flow() {

        double x = 0;
        double y = 0;
        int i = gridSize / 2;
        for (int j = gridSize / 2 - holeSize / 2; j < gridSize / 2 + holeSize / 2; j++) {
            for (int k = 0; k < directions; k++) {
                if (lattice[i][j].getOutDirection(k)) {
                    x += directionsComponent[k][0];
                }
            }

        }
        return x;
    }


    boolean isBalanced(){
        int N=0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(i==0 || i==gridSize-1 || j==0 || j==gridSize-1 || i == gridSize/2) continue;
                for (int k = 0; k < directions; k++) {
                    if (lattice[i][j].getOutDirection(k)) {
                        N += i < gridSize / 2? 1 : -1;
                    }
                }
            }
        }
        //System.out.println(N);
        //return false;
        return Math.abs(N)<=numParticles*0.1;
    }

    public float getPercentageOfParticlesInSideA(){
        int N=0;
        for (int i = 0; i < gridSize/2; i++) {
            for (int j = 0; j < gridSize; j++) {
                if(i==0 || i==gridSize-1 || j==0 || j==gridSize-1 || i == gridSize/2) continue;
                for (int k = 0; k < directions; k++) {
                    if (lattice[i][j].getOutDirection(k)) {
                        N ++;
                    }
                }
            }
        }

        return N*1.0f/this.numParticles;
    }

    public void run(long maxIterations, FileWriter out) throws IOException {

        lattice = new Node[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                lattice[i][j] = new Node();
                if (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1) {
                    lattice[i][j].setBoundary(true);
                }
            }
        }

        //set vertical wall in the middle of the lattice with a hole
        for (int i = 0; i < gridSize; i++) {
            if (i < (gridSize / 2 - holeSize / 2) || i > (gridSize / 2 + holeSize / 2))
                lattice[gridSize / 2][i].setBoundary(true);
        }


        for (int i = 0; i < numParticles; i++) {
            int x, y, direction;
            do {
                x = (int) (Math.random() * (gridSize / 2 - 2)) + 1;
                y = (int) (Math.random() * (gridSize - 2)) + 1;
                direction = (int) (Math.random() * directions);
            } while (lattice[x][y].getInDirection(direction));
            lattice[x][y].setInDirection(direction, true);
        }

     /*   numParticles=directions;
        for(int i=0; i<directions; i++) {
            lattice[gridSize / 2][gridSize / 2].setInDirection(i, true);
        }*/

 /*       numParticles = 3;
        lattice[gridSize / 2-20][gridSize / 2].setInDirection(0, true);
        lattice[gridSize / 2+20][gridSize / 2].setInDirection(3, true);
        lattice[gridSize/2-20][gridSize / 2-20].setInDirection(1, true);

*/
        int time_avg = 100;

        List<Double> flows = new ArrayList<>();

        double flow_avg= 10;

        out.write(numParticles + " " + gridSize + "\n");
        int iter = 0;
        //for (iter = 0; iter < maxIterations && (!isBalanced() || iter < 1); iter++) {
        for (iter = 0; iter < maxIterations && flow_avg > 0; iter++) {
            Float percentageInA = getPercentageOfParticlesInSideA()*100;
            Float percentageInB = 100 - percentageInA;
            System.out.println("Percentage in AvsB="+ percentageInA.toString()+"% || " + percentageInB + "%");
            flows.add(flow());
            if (iter > time_avg) {
                flows.remove(0);
                flow_avg = flows.stream().mapToDouble(d -> d).average().getAsDouble();
                //System.out.println(flow_avg);
            }
            propagate();
            out.write((iter + 1) + "\n");
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    for (int k = 0; k < directions; k++) {
                        if (lattice[i][j].getInDirection(k)) {
                            out.write(i + " " + j + " " + k + "\n");
                        }
                    }
                    lattice[i][j].recalculate();
                }
            }
        }
        System.out.println(iter);


    }
}
