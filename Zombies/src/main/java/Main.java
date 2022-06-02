import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        FileWriter out = new FileWriter("zombies.txt");


        double dt = Double.parseDouble(System.getProperty("dt", "1e-3"));
        double outputDt = Double.parseDouble(System.getProperty("outputDt", "100e-3"));
        double tf = Double.parseDouble(System.getProperty("tf", "300"));
        double vz = Double.parseDouble(System.getProperty("vz", "3"));
        int Nh = Integer.parseInt(System.getProperty("nh", "10"));
        int frec = (int) (outputDt / dt);
        int recipientRadius = 11;

        Environment environment = Environment.init(dt, recipientRadius, vz);


        environment.addZombie();

        // add humans
        for (int i = 0; i < Nh; i++) {
            environment.addHuman();
        }

        out.write(Nh + 1 + "\n");
        for (int i = 1; i * dt < tf && !environment.areAllZombies(); i++) {
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
