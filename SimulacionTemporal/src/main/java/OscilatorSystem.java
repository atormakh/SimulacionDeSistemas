import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OscilatorSystem {

    public static void main(String[] args) throws IOException {

        String methodName = System.getProperty("method", "Verlet");
        double dt = Double.parseDouble(System.getProperty("dt", "0.001"));
        double dt2 = Double.parseDouble(System.getProperty("dt2", "0.05"));
        double tf = Double.parseDouble(System.getProperty("tf", "5"));
        Map<String, IntegrationMethod> methods = new HashMap<>();

/*        dt *= 1e-3;
        dt2 *= 1e-3;*/

        double k = Math.pow(10, 4);
        int mass = 70;
        int gamma = 100;
        double A = 1;

        ForceFunction force = new OscilatorForce(k, gamma);

        double initialPos = 1;
        double initialVel = -A * gamma / (2 * mass);

        methods.put("Verlet", new Verlet(force, initialPos, initialVel, mass, dt));
        methods.put("Beeman", new Beeman(force, initialPos, initialVel, mass, dt));
        methods.put("Gear", new GearPredictor(force, initialPos, initialVel, mass, dt));

        FileWriter out = new FileWriter("oscilator.txt");

        IntegrationMethod method = methods.get(methodName);
        Result result = new Result(initialPos,initialVel,0);
        out.write(0 + " " + result.getPosition() + " " + result.getVelocity() + " " + result.getAcceleration() + "\n");
        for (double t = dt; t < tf; t += dt) {
            result = method.get();

            if ((int) (t / dt) % (int) (dt2 / dt) == 0) {
                out.write(t + " " + result.getPosition() + " " + result.getVelocity() + " " + result.getAcceleration() + "\n");
            }

        }

        out.close();
    }
}
