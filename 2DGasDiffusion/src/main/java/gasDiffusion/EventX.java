package gasDiffusion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventX extends Event {
    Particle particle;
    public EventX(double tc, Particle particle) {
        super(tc);
        this.particle = particle;
    }

    public void updateParticles(){
        particle.vx=-particle.vx;
    }

    @Override
    public List<Particle> getParticles() {
        return Collections.singletonList(particle);
    }

    @Override
    public double getMomentum() {
        return 2*particle.vx*particle.mass;
    }
}
