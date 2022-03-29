package cellIndexMethod;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

public class BruteForceMethod{


    public static List<ParticleWithNeighbours> call(List<Particle> _particles, double rc, double L, Boolean isBorder){
        List<ParticleWithNeighbours>  particles = _particles.stream().map(ParticleWithNeighbours::new).collect(Collectors.toList());
        for (ParticleWithNeighbours particleA : particles){
            for (ParticleWithNeighbours particleB : particles){
                if( particleA!=particleB) {
                    double dist = getDistance(particleA, particleB, isBorder,L) - particleA.r - particleB.r;
                    if (dist < rc) {
                        particleA.addNeighbour(particleB);
//                        particleB.addNeighbour(particleA);
                    }
                }
            }
        }
        return particles;
    }

    public static Double getDistance(Particle p1,Particle p2, Boolean isBorder,double L){

        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());

        if (isBorder) {
            deltaX = deltaX - (deltaX > 1.0f * L / 2 ? L : 0);
            deltaY = deltaY - (deltaY > 1.0f * L / 2 ? L : 0);
        }



        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) - p1.r - p2.r;
    }
}
