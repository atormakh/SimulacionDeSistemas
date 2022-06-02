import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CellIndexMethod {

    private static class Cell {
        final ArrayList<Entity> entities = new ArrayList<>();

        private void add(Entity Entity) {
            entities.add(Entity);
        }

        private void checkNeighbours(ArrayList<Cell> neighbourCells, double rc) {
            entities.forEach(p1 -> {
                neighbourCells.forEach(c -> c.entities.forEach(p2 -> {

                    double distance = p1.position.dist(p2.position) - p1.r - p2.r;
                    if (distance < rc) {
                        p1.addNeighbour(p2);
                        p2.addNeighbour(p1);
                    }
                }));
                addSameCell(rc, p1);

            });
        }

        private void checkNeighboursRound(ArrayList<Cell> neighbourCells, double rc, double L) {
            entities.forEach(p1 -> {
                neighbourCells.forEach(c -> c.entities.forEach(p2 -> {

                    if (p1.position.dist(p2.position) < rc) {
                        p1.addNeighbour(p2);
                        p2.addNeighbour(p1);
                    }
                }));
                addSameCell(rc, p1);

            });
        }

        private void addSameCell(double rc, Entity p1) {
            entities.forEach(p2 -> {
                if (p1 != p2) {
                    if (p1.position.dist(p2.position) < rc) 
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

    public static List<Entity> call(List<Entity> entities, double rc, double L, int _M, boolean periodic) {
        Optional<Entity> max_r = entities.stream().max(Comparator.comparing(Entity::getR));
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
        entities.forEach(p -> {
            int x = (int) ((p.position.x + L/2)/ W);
            int y = (int) ((p.position.y +L/2)/ W);
            if(x == M) x = M-1; // if L=100 and px = 100 then x will be out of range
            if(y == M) y = M-1;
            cellMatrix[y][x].add(p);
        });

        if (periodic) {
            neighboursRound(cellMatrix, M, rc, L);
        } else {
            neighbours(cellMatrix, M, rc);
        }

        return entities;

    }

}
