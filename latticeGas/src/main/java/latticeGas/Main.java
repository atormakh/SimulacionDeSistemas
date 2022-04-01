package latticeGas;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {


        int holeSize = Integer.parseInt(System.getProperty("holeSize","50"));
        int numParticles = Integer.parseInt(System.getProperty("numParticles","2000"));
        int seed = Integer.parseInt(System.getProperty("seed","0"));
        float threshold = Float.parseFloat(System.getProperty("threshold","0.05"));

        if (seed == 0) seed = (int) (Math.random() * 1000000);

        LatticeGas latticeGas = new LatticeGas(200,numParticles,holeSize,seed);
        FileWriter out = null;
        try {
            out = new FileWriter("latticeGas.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            latticeGas.run(10000, threshold, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
