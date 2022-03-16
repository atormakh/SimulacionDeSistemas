package cellIndexMethod;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CellIndexMethod{

    private static class Cell {
        ArrayList<ParticleWithNeighbours> particles = new ArrayList<>();

        private void add(ParticleWithNeighbours particle) {
            particles.add(particle);
        }

        private void checkNeighbours(ArrayList<Cell> neighbourCells, double rc) {
            particles.forEach(p1 -> {
                neighbourCells.forEach(c -> {
                    c.particles.forEach(p2 -> {
                        ArrayList<Particle> aux = new ArrayList<>();
                        double distance = Point2D.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY()) - p1.r - p2.r;
                        if (distance < rc) {
                            p1.addNeighbour(p2);
                            p2.addNeighbour(p1);
                        }
                    });
                });
                particles.forEach(p2 -> {
                    if(p1 != p2){
                        p1.addNeighbour(p2);
                    }
                });

            });
        }
    }

    public static List<ParticleWithNeighbours> call(List<Particle> _particles, double rc, int L){
        Optional<Particle> max_r = _particles.stream().max(Comparator.comparing(Particle::getR));
        if(!max_r.isPresent()) return null;
        int M = (int)(L/(rc+max_r.get().getR()));
        double W = 1.0f*L/M;
        Cell[][] cellMatrix = new Cell[M][M];



        for(int i=0; i<M;i++){
            for(int j=0; j<M;j++){
                cellMatrix[j][i] = new Cell();
            }
        }
        List<ParticleWithNeighbours> particles = _particles.stream().map(ParticleWithNeighbours::new).collect(Collectors.toList());
        particles.forEach(p-> {
            int x = (int)(p.getX()/W);
            int y = (int)(p.getY()/W);
            cellMatrix[y][x].add(p);
       });



       for (int i=0; i<M;i++) {
           for (int j=0; j<M;j++) {

               ArrayList<Cell> neighbourCells = new ArrayList<>();

               if(j>0) {
                   neighbourCells.add(cellMatrix[j-1][i]);
               }
               if (i<M-1) {
                   if(j>0) {
                       neighbourCells.add(cellMatrix[j - 1][i + 1]);
                   }
                   neighbourCells.add(cellMatrix[j][i+1]);
                   if (j<M-1) {
                       neighbourCells.add(cellMatrix[j+1][i+1]);
                   }
               }

               cellMatrix[j][i].checkNeighbours(neighbourCells,rc);

           }
       }

       return particles;

    }
}
