import java.util.Arrays;
import java.util.Vector;

public class ElectricFieldForce implements Force2DFunction {



    public boolean checkCollision(Vector2 pos, double epsilon) {
        for (Particle p : particles) {
            if (pos.distance(p.position) <= epsilon) {
                return true;
            }
        }
        return false;
    }

    private static class Particle {
        Vector2 position;
        double q;

        public Particle(double x, double y, double q) {
            position = new Vector2(x, y);
            this.q = q;
        }
    }

    double k = 1e10;
    double Q = 1e-19;

    private Particle[] particles;


    public ElectricFieldForce(int N, double D) {
        particles = new Particle[N * N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double q = (i + j) % 2 == 0 ? Q : -Q;
                particles[i * N + j] = new Particle((i + 1) * D, j * D, q);
            }
        }

    }


    public Vector2 getForce(Vector2 r, Vector2 r1) {
        Vector2 F = new Vector2(0,0);
        double f,fx,fy;
        double distance;
        for (Particle p : particles) {
            distance = r.distance(p.position);
            f = p.q / (distance * distance);
            fx = f * (r.x - p.position.x) / distance;
            fy = f * (r.y - p.position.y) / distance;
            F.x += fx;
            F.y += fy;

        }

        F = F.scalarMultiply(k*Q);


        return F;
    }


}
