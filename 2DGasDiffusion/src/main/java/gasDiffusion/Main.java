package gasDiffusion;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        double holeSize = Float.parseFloat(System.getProperty("holeSize", "0.03"));
        int numParticles = Integer.parseInt(System.getProperty("numParticles", "100"));
        int maxIterations = Integer.parseInt(System.getProperty("maxIterations", "15000"));
        int seed = Integer.parseInt(System.getProperty("seed", "1"));
        float threshold = Float.parseFloat(System.getProperty("threshold", "0.05"));

        if (seed == 0) seed = (int) (Math.random() * 1000000);

        GasBox gasBox = new GasBox(holeSize,numParticles,seed,threshold);
        FileWriter out = null;
        try {
            out = new FileWriter("2DGasDiffusion.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            gasBox.run(maxIterations, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}