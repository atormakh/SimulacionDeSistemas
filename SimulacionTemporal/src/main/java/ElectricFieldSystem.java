import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElectricFieldSystem {

    public static void main(String[] args) throws IOException {

        double dt = Double.parseDouble(System.getProperty("dt", "0.1"));
        double dt2 = Double.parseDouble(System.getProperty("dt2", "0.1"));
        double tf = Double.parseDouble(System.getProperty("tf", "4"));
        double v = Double.parseDouble(System.getProperty("v", "5000"));
        double r = Double.parseDouble(System.getProperty("r", "0")); // y should be between -1 and 1

        dt *= 1e-12;
        dt2 *= 1e-12;
        tf *= 1e-12;

        int N = 16;
        double D = 1e-8;

        ElectricFieldForce ElectricFieldForce = new ElectricFieldForce(N, D);

        Vector2 initialVel = new Vector2(v, 0);
        Vector2 initialPos = new Vector2(0, r * D + N * D / 2 - D/2);

        double mass = 1e-27;

        Verlet2D verlet2D = new Verlet2D(ElectricFieldForce, initialPos, initialVel, mass, dt);

        FileWriter out = new FileWriter("ElectricField.txt");

        Result2D result;
        Vector2 pos, vel;
        out.write(0 + " " + initialPos.x + " " + initialPos.y + " " + initialVel.x + " " + initialVel.y + "\n");
        for (double t = dt; t < tf; t += dt) {
            result = verlet2D.get();
            pos = result.getPosition();
            vel = result.getVelocity();


            if ((int) (t / dt) % (int) (dt2 / dt) == 0) {
                out.write(t + " " + pos.x + " " + pos.y + " " + vel.x + " " + vel.y + "\n");
            }

            if (pos.x > (N + 1) * D || pos.x < -D || pos.y > N * D || pos.y < 0) {
                System.out.println("Particle escaped!");
                break;
            }

            if (ElectricFieldForce.checkCollision(pos, 0.001 * D)) {
                System.out.println("Particle collided!");
                break;
            }


        }

        out.close();
    }
}
