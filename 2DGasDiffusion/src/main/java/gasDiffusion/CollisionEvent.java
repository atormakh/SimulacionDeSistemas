package gasDiffusion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollisionEvent extends Event {

    Particle a;
    Particle b;

    public CollisionEvent(double time, Particle a, Particle b) {
        super(time);
        this.a= a;
        this.b = b;
    }

    @Override
    public void updateParticles() {
        double dvx = b.vx - a.vx;
        double dvy = b.vy - a.vy;

        double dx = b.x - a.x;
        double dy = b.y - a.y;


        double dvdr = dvx * dx + dvy * dy;

        double sigma = a.radius + b.radius;

        double j = (2*a.mass*b.mass*dvdr)/(sigma*(a.mass+b.mass));
        double jx = j * dx / sigma;
        double jy = j * dy / sigma;


        a.vx = a.vx + jx / a.mass;
        a.vy = a.vy + jy / a.mass;

        b.vx = b.vx - jx / b.mass;
        b.vy = b.vy - jy / b.mass;

    }

    @Override
    public List<Particle> getParticles() {
        return Arrays.asList(a,b);
    }
}
