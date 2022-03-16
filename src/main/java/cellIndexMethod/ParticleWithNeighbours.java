package cellIndexMethod;

import java.util.ArrayList;
import java.util.List;
public class ParticleWithNeighbours extends Particle {
    public List<ParticleWithNeighbours> neighbours;

    public ParticleWithNeighbours(double x, double y, double r, long id) {
        super(x, y, r, id);
        neighbours = new ArrayList<ParticleWithNeighbours>();
    }

    public ParticleWithNeighbours(Particle particle) {
        super(particle.x, particle.y, particle.r, particle.id);
        neighbours = new ArrayList<ParticleWithNeighbours>();
    }

    public void addNeighbour(ParticleWithNeighbours particle) {
        neighbours.add(particle);
    }

    public Boolean isNeighbour(Particle p) {
        return neighbours.contains(p);
    }
}
