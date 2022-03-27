package latticeGas;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {;
        LatticeGas latticeGas = new LatticeGas(200,2000);
        FileWriter out = null;
        try {
            out = new FileWriter("latticeGas/latticeGas.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            latticeGas.run(1000, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
