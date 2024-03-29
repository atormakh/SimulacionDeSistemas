package gasDiffusion;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        double holeSize = Float.parseFloat(System.getProperty("holeSize", "0.02"));
        int numParticles = Integer.parseInt(System.getProperty("numParticles", "50"));
        int maxIterations = Integer.parseInt(System.getProperty("maxIterations", "1500000"));
        int seed = Integer.parseInt(System.getProperty("seed", "0"));
        double initialVelocity = Float.parseFloat(System.getProperty("initialVelocity", "0.01"));
        double timeMultiplier = Float.parseFloat(System.getProperty("timeMultiplier", "1"));
        float threshold = Float.parseFloat(System.getProperty("threshold", "0.05"));
        
        if (seed == 0) seed = (int) (Math.random() * 1000000);

        GasBox gasBox = new GasBox(holeSize,numParticles,seed,threshold, initialVelocity, timeMultiplier);
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