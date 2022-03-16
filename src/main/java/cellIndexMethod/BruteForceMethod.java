package cellIndexMethod;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForceMethod{


    public static List<ParticleWithNeighbours> call(List<Particle> _particles, double rc, int L, Boolean isBorder){
        List<ParticleWithNeighbours>  particles = _particles.stream().map(ParticleWithNeighbours::new).collect(Collectors.toList());
        for (ParticleWithNeighbours particleA : particles){
            for (ParticleWithNeighbours particleB : particles){
                if(!particleA.isNeighbour(particleB) && particleA.getId()!=particleB.getId()) {
                    Double dist = getDistance(particleA, particleB, isBorder) - particleA.r - particleB.r;
                    if (dist < rc) {
                        particleA.addNeighbour(particleB);
                        particleB.addNeighbour(particleA);
                    }
                }
            }
        }
        return particles;
    }

    public static Double getDistance(Particle p,Particle p2, Boolean isBorder){
            if(isBorder){
               return Point2D.distance(p.getX(),p.getY(),p2.getX(),p2.getY());
            }
            return Point2D.distance(p.getX(),p.getY(),p2.getX(),p2.getY());
    }
}
