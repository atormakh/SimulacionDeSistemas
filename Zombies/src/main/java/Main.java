import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.println("Estan las notas");
        FileWriter out = new FileWriter("ElectricField.txt");

        int humansQuantity, zombiesQuantity;

        double dt = 1e-3;
        double outputDt = 50e-3;
        double tf = 15;
        int Nh = 10;
        int frec = (int) (outputDt / dt);
        int recipientRadius = 11;
        Environment environment = new Environment(dt, recipientRadius);


        environment.addZombie();

        // add humans
        for (int i = 0; i < Nh; i++) {
            environment.addHuman();
        }


        for (int i = 1; i * dt < tf; i++) {
            if (i % frec == 0) {
                //print to file
                environment.printToFile(out);
            }

            environment.update();

        }


    }
}
