package gasDiffusion;

import java.util.Collections;
import java.util.List;

public class EventY extends Event {

    Particle particle;

    public EventY(double tc, Particle particle) {
        super(tc);
        this.particle = particle;
    }

    @Override
    public void updateParticles() {
        particle.vy=-particle.vy;
    }

    @Override
    public List<Particle> getParticles() {
        return Collections.singletonList(particle);
    }
}
