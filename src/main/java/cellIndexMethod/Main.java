package cellIndexMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        boolean generate = Boolean.parseBoolean(System.getProperty("generate", "false"));
        boolean periodic = Boolean.parseBoolean(System.getProperty("periodic", "false"));
        boolean bf = Boolean.parseBoolean(System.getProperty("bf", "false"));
        int L = Integer.parseInt(System.getProperty("L", "100"));
        int MAX_R = Integer.parseInt(System.getProperty("R", "2"));
        int N = Integer.parseInt(System.getProperty("N", "100"));
        double RC = Double.parseDouble(System.getProperty("RC", "5"));
        String generationOutput = System.getProperty("generation", "output.txt");
        String output = System.getProperty("output", "neighbours-output.txt");

        List<Particle> particles;

        if (generate) {
            particles = ParticleGenerator.generate(generationOutput,N,L,MAX_R);
        } else {
            //TODO read files
            particles = ParticleGenerator.generate(generationOutput,N,L,MAX_R);
        }

        List<ParticleWithNeighbours> particlesN = bf ? BruteForceMethod.call(particles, RC, L, !periodic) : CellIndexMethod.call(particles, RC, L);

        if (particlesN == null) return;

        FileWriter out;
        try {
            out = new FileWriter(output);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        particlesN.forEach(p -> {
            StringBuilder line = new StringBuilder(p.getId() + " ");
            p.neighbours.sort(Comparator.comparing(ParticleWithNeighbours::getId));
            for (ParticleWithNeighbours n : p.neighbours) {
                line.append(n.getId()).append(" ");
            }
            try {
                out.write(String.format("%s\n",line.toString()));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
