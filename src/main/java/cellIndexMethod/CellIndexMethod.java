package cellIndexMethod;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CellIndexMethod {

    private static class Cell {
        final ArrayList<ParticleWithNeighbours> particles = new ArrayList<>();

        private void add(ParticleWithNeighbours particle) {
            particles.add(particle);
        }

        private void checkNeighbours(ArrayList<Cell> neighbourCells, double rc) {
            particles.forEach(p1 -> {
                neighbourCells.forEach(c -> c.particles.forEach(p2 -> {

                    double distance = Point2D.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY()) - p1.r - p2.r;
                    if (distance < rc) {
                        p1.addNeighbour(p2);
                        p2.addNeighbour(p1);
                    }
                }));
                addSameCell(rc, p1);

            });
        }

        private void checkNeighboursRound(ArrayList<Cell> neighbourCells, double rc, double L) {
            particles.forEach(p1 -> {
                neighbourCells.forEach(c -> c.particles.forEach(p2 -> {

                    double deltaX = Math.abs(p1.getX() - p2.getX());
                    double deltaY = Math.abs(p1.getY() - p2.getY());

                    deltaX = deltaX - (deltaX > 1.0f * L / 2 ? L : 0);
                    deltaY = deltaY - (deltaY > 1.0f * L / 2 ? L : 0);


                    double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) - p1.r - p2.r;
                    if (distance < rc) {
                        p1.addNeighbour(p2);
                        p2.addNeighbour(p1);
                    }
                }));
                addSameCell(rc, p1);

            });
        }

        private void addSameCell(double rc, ParticleWithNeighbours p1) {
            particles.forEach(p2 -> {
                if (p1 != p2) {
                    double distance = Point2D.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY()) - p1.r - p2.r;
                    if(distance < rc)
                        p1.addNeighbour(p2);
                }
            });
        }

    }

    private static void neighbours(Cell[][] cellMatrix, int M, double rc) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {

                ArrayList<Cell> neighbourCells = new ArrayList<>();

                if (j > 0) {
                    neighbourCells.add(cellMatrix[j - 1][i]);
                }

                if (i < M - 1) {
                    if (j > 0) {
                        neighbourCells.add(cellMatrix[j - 1][i + 1]);
                    }
                    neighbourCells.add(cellMatrix[j][i + 1]);
                    if (j < M - 1) {
                        neighbourCells.add(cellMatrix[j + 1][i + 1]);
                    }
                }

                cellMatrix[j][i].checkNeighbours(neighbourCells, rc);

            }
        }

    }

    private static void neighboursRound(Cell[][] cellMatrix, int M, double rc, double L) {
        int jr;
        int ir;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {

                ArrayList<Cell> neighbourCells = new ArrayList<>();

                jr = j == 0 ? M - 1 : j - 1;
                neighbourCells.add(cellMatrix[jr][i]);


                ir = i == M - 1 ? 0 : i + 1;

                jr = j == 0 ? M - 1 : j - 1;
                neighbourCells.add(cellMatrix[jr][ir]);
                neighbourCells.add(cellMatrix[j][ir]);

                jr = j == M - 1 ? 0 : j + 1;
                neighbourCells.add(cellMatrix[jr][ir]);


                cellMatrix[j][i].checkNeighboursRound(neighbourCells, rc, L);

            }
        }
    }

    public static List<ParticleWithNeighbours> call(List<Particle> _particles, double rc, double L, int _M, boolean periodic) {
        Optional<Particle> max_r = _particles.stream().max(Comparator.comparing(Particle::getR));
        if (!max_r.isPresent()) return null;
        _M = _M >0? _M:(int) (L / (rc + max_r.get().getR()));
        int M = _M==0? 1: _M;
        double W = 1.0f * L / M;
        Cell[][] cellMatrix = new Cell[M][M];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                cellMatrix[j][i] = new Cell();
            }
        }
        List<ParticleWithNeighbours> particles = _particles.stream().map(ParticleWithNeighbours::new).collect(Collectors.toList());
        particles.forEach(p -> {
            int x = (int) (p.getX() / W);
            int y = (int) (p.getY() / W);
            if(x == M) x = M-1; // if L=100 and px = 100 then x will be out of range
            if(y == M) y = M-1;
            cellMatrix[y][x].add(p);
        });

        if (periodic) {
            neighboursRound(cellMatrix, M, rc, L);
        } else {
            neighbours(cellMatrix, M, rc);
        }

        return particles;

    }

}
