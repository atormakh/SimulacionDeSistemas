package cellIndexMethod;

import java.util.ArrayList;
import java.util.List;
public class ParticleWithNeighbours extends Particle {
    public final List<ParticleWithNeighbours> neighbours;

    public ParticleWithNeighbours(Particle particle) {
        super(particle.x, particle.y, particle.r, particle.id);
        neighbours = new ArrayList<>();
    }

    public void addNeighbour(ParticleWithNeighbours particle) {
        neighbours.add(particle);
    }

    public Boolean isNeighbour(Particle p) {
        return neighbours.contains(p);
    }
}
