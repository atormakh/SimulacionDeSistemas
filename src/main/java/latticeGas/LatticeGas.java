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

    double[][] directionsComponent = {
            {1, 0},
            {1, 1},
            {-1, 1},
            {-1, 0},
            {-1, -1},
            {1, -1}
    };

    static int[][] evenDirectionsArray = {{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {0, -1}, {1, -1}};
    static int[][] oddDirectionsArray = {{1, 0}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};


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
        return x/(directions*holeSize);
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
        System.out.println(N);
        return false;
        //return Math.abs(N)<=numParticles*0.1;
    }

    public float countParticles(int xi, int yi, int xf, int yf) {
        int N=0;
        for (int i = xi; i < xf; i++) {
            for (int j = yi; j < yf; j++) {
                for (int k = 0; k < directions; k++) {
                    if (lattice[i][j].getOutDirection(k)) {
                        N ++;
                    }
                }
            }
        }

        return N*1.0f/numParticles;
    }

    public void run(long maxIterations, FileWriter out) throws IOException {

        lattice = new Node[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                lattice[i][j] = new Node(random.nextDouble());
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
                x = random.nextInt(gridSize/2-2) + 1;
                y = random.nextInt(gridSize-2) + 1;
                direction = random.nextInt(directions);
            } while (lattice[x][y].getInDirection(direction));
            lattice[x][y].setInDirection(direction, true);
        }

        int time_avg = 1000;

        List<Double> flows = new ArrayList<>();

        double flow_avg= 10;

        out.write(numParticles + " " + gridSize + "\n");
        int iter = 0;
        //for (iter = 0; iter < maxIterations && (!isBalanced() || iter < 1); iter++) {
        //for (iter = 0; iter < maxIterations && flow_avg > 0; iter++) {
        for (iter = 0; iter < maxIterations; iter++) {
/*            Float percentageInA = getPercentageOfParticlesInSideA()*100;
            Float percentageInB = 100 - percentageInA;
            flows.add(flow());
            if (iter > time_avg) {
                flows.remove(0);
                flow_avg = flows.stream().mapToDouble(d -> d).average().getAsDouble();
            }*/
            propagate();

            float a = countParticles(0,0,gridSize/2-1,gridSize);
            float b = countParticles(gridSize/2+1,0,gridSize,gridSize);

            out.write((iter + 1) + " " + flow() + " " + a + " " + b +  "\n");
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
