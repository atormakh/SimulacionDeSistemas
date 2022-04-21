package gasDiffusion;

import java.util.Collections;
import java.util.List;

public class ObstacleEvent extends Event {
    private Particle particle;

    public ObstacleEvent(double time, Particle particle) {
        super(time);
    }

    @Override
    public void updateParticles() {
        System.out.println("WAAAAAAAAAAAAAAZZZZZZAAAAAAA");
    }

    @Override
    public List<Particle> getParticles() {
        return Collections.singletonList(particle);
    }
}
