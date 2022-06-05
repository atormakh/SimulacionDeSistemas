import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        FileWriter out = new FileWriter("zombies.txt");


        double dt = Double.parseDouble(System.getProperty("dt", "1e-2"));
        double outputDt = Double.parseDouble(System.getProperty("outputDt", "2e-1"));
        double tf = Double.parseDouble(System.getProperty("tf", "180"));
        double vz = Double.parseDouble(System.getProperty("vz", "3"));
        int Nh = Integer.parseInt(System.getProperty("nh", "200"));
        double np = Double.parseDouble(System.getProperty("np", "0"));
        int frec = (int) (outputDt / dt);
        int recipientRadius = 11;

        Environment environment = Environment.init(dt, recipientRadius, vz, np);

        environment.entityAp = Double.parseDouble(System.getProperty("entityAp", "400"));
        environment.entityBp = Double.parseDouble(System.getProperty("entityBp", "0.5"));
        environment.wallAp = Double.parseDouble(System.getProperty("wallAp", "2000"));
        environment.wallAp = environment.entityAp;
        environment.wallBp = Double.parseDouble(System.getProperty("wallBp", "0.5"));
        environment.zombieAp = Double.parseDouble(System.getProperty("zombieAp", "2000"));
        environment.zombieBp = Double.parseDouble(System.getProperty("zombieBp", "0.5"));
        environment.rmin = Double.parseDouble(System.getProperty("rmin", "0.2"));
        environment.rmax = Double.parseDouble(System.getProperty("rmax", "0.3"));
        environment.beta = Double.parseDouble(System.getProperty("beta", "0.9"));
        environment.tau = Double.parseDouble(System.getProperty("tau", "4"));


        environment.addZombie();
        // add humans
        for (int i = 0; i < Nh; i++) {
            environment.addHuman();
        }
        out.write(Nh + 1 + "\n");
        for (int i = 1; i * dt < tf && !environment.areAllSame(); i++) {
            if (i % frec == 0) {
                //print to file
                environment.printToFile(out);

                System.out.print(String.format("\r%.2f", i * dt / tf * 100) + "% ");
            }

            environment.update();

        }
        environment.printToFile(out);
        out.close();


    }
}
