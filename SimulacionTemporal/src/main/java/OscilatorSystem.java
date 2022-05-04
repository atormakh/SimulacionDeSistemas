import java.util.HashMap;
import java.util.Map;

public class OscilatorSystem {

    public static void main(String[] args) {

        String methodName = System.getProperty("method", "Gear");
        Double dt = Double.parseDouble(System.getProperty("dt", "0.001"));

        Map<String, IntegrationMethod> methods = new HashMap<>();


        double k = Math.pow(10, 4);
        int mass = 70;
        int gamma = 100;
        int tf = 5;
        double A = 1;

        ForceFunction force = new OscilatorForce(k, gamma);

        double initialPos = 1;
        double initialVel = -A * gamma / (2 * mass);
        double dt2 = 50 * 1e-3;

        methods.put("Verlet", new Verlet(force, initialPos, initialVel, mass, dt));
        methods.put("Beeman", new Beeman(force, initialPos, initialVel, mass, dt));
        methods.put("Gear", new GearPredictor(force, initialPos, initialVel, mass, dt));



        IntegrationMethod method = methods.get(methodName);
        Result result;
        for (double t = 0; t < tf; t += dt) {
            result = method.get();

            if ((int) (t / dt) % (int) (dt2 / dt) == 0) {
                System.out.println(result.getPosition());
            }
            //System.out.println(result.getPosition() + " " + result.getVelocity());
        }
    }
}
