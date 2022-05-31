import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        FileWriter out = new FileWriter("zombies.txt");


        double dt = 1e-3;
        double outputDt = 100e-3;
        double tf = 60*5;
        int Nh = 10;
        int frec = (int) (outputDt / dt);
        int recipientRadius = 11;

        Environment environment = Environment.init(dt, recipientRadius);


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
            }

            environment.update();

        }

        out.close();


    }
}
