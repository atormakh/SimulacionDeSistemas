package gasDiffusion;

import java.util.Collections;
import java.util.List;

public class ObstacleEvent extends Event {
    private Particle particle;

    public ObstacleEvent(double time, Particle particle) {
        super(time);
        this.particle = particle;
    }

    @Override
    public void updateParticles() {
        particle.vx = 0d;
        particle.vy = 0d;

    }

    @Override
    public List<Particle> getParticles() {
        return Collections.singletonList(particle);
    }
}
