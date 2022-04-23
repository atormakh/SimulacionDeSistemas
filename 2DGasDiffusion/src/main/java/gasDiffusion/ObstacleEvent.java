package gasDiffusion;

import java.util.Collections;
import java.util.List;

public class ObstacleEvent extends Event {
    private final Particle particle;
    private final double alpha;

    public ObstacleEvent(double time, Particle particle, double alpha) {
        super(time);
        this.particle = particle;
        this.alpha = alpha;
    }


    @Override
    public void updateParticles() {
        double vx = particle.vx;
        double vy = particle.vy;
        double cos = Math.cos(alpha);
        double sin = Math.sin(alpha);
        particle.vx = (sin * sin - cos * cos) * vx - 2 * sin * cos * vy;
        particle.vy = -2 * sin * cos * vx + (cos * cos - sin * sin) * vy;


    }

    @Override
    public List<Particle> getParticles() {
        return Collections.singletonList(particle);
    }
}
