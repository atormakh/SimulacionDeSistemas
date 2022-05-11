import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElectricFieldSystem {

    public static void main(String[] args) throws IOException {

        String methodName = System.getProperty("method", "Verlet");
        double dt = Double.parseDouble(System.getProperty("dt", "0"));
        double dt2 = Double.parseDouble(System.getProperty("dt2", "0"));
        double tf = Double.parseDouble(System.getProperty("tf", "0"));
        double v = Double.parseDouble(System.getProperty("v", "5000"));
        double r = Double.parseDouble(System.getProperty("r", "0")); // y should be between -1 and 1
/*
        dt *= 1e-12;
        dt2 *= 1e-12;
        tf *= 1e-12;*/

        dt = dt == 0 ? 1e-12 : dt;
        dt2 = dt2 == 0 ? 1e-12 : dt2;
        tf = tf == 0 ? 1 : tf;

        int N = 16;
        double D = 1e-8;
        int margin = 3;

        ElectricFieldForce ElectricFieldForce = new ElectricFieldForce(N, D);

        Vector2 initialVel = new Vector2(v, 0);
        Vector2 initialPos = new Vector2(0, r * D + N * D / 2 - D / 2);

        double mass = 1e-27;

        Map<String, Integration2DMethod> methods = new HashMap<>();
        methods.put("Gear",new GearPredictor2D(ElectricFieldForce, initialPos, initialVel, mass, dt));
        methods.put("Verlet", new Verlet2D(ElectricFieldForce, initialPos, initialVel, mass, dt));

        Integration2DMethod method = methods.get(methodName);


        FileWriter out = new FileWriter("ElectricField.txt");

        Result2D result;
        Vector2 pos, vel;
        out.write(0 + " " + initialPos.x + " " + initialPos.y + " " + initialVel.x + " " + initialVel.y + "\n");
        for (double t = dt; t < tf; t += dt) {
            result = method.get();
            pos = result.getPosition();
            vel = result.getVelocity();


            if ((int) (t / dt) % (int) (dt2 / dt) == 0) {
                out.write(t + " " + pos.x + " " + pos.y + " " + vel.x + " " + vel.y + "\n");
            }

            if (pos.y > (N + margin) * D) {
                System.out.println("1");
                break;
            }

            if (pos.x > (N + margin) * D) {
                System.out.println("2");
                break;
            }

            if ( pos.y < -margin * D) {
                System.out.println("3");
                break;
            }

            if(pos.x < -D * margin){
                System.out.println("4");
            }



            if (ElectricFieldForce.checkCollision(pos, 0.01 * D)) {
                System.out.println("0");
                break;
            }


        }

        out.close();
    }
}
