package cellIndexMethod;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        boolean generate = Boolean.parseBoolean(System.getProperty("generate","false"));
        int L = Integer.parseInt(System.getProperty("L","10"));
        int MAX_R = Integer.parseInt(System.getProperty("R","2"));
        int N = Integer.parseInt(System.getProperty("N","10"));
        double RC = Double.parseDouble(System.getProperty("RC","3"));

        List<Particle> particles;

        if(generate){
            ParticleGenerator generator = new ParticleGenerator(L,MAX_R);
            particles = Stream.generate(generator).limit(N).collect(Collectors.toList());
        }else{
            //TODO read files
            ParticleGenerator generator = new ParticleGenerator(L,MAX_R);
            particles = Stream.generate(generator).limit(N).collect(Collectors.toList());
        }

        List<ParticleWithNeighbours> particlesN =CellIndexMethod.call(particles,RC, L);

        if(particlesN == null) return;

        particlesN.forEach(p-> {
            StringBuilder line = new StringBuilder(p.getId() + " ");
            p.neighbours.sort(Comparator.comparing(ParticleWithNeighbours::getId));
            for(ParticleWithNeighbours n : p.neighbours){
                line.append(n.getId()).append(" ");
            }

            System.out.println(line);
        });


        List<ParticleWithNeighbours> particlesN2 =BruteForceMethod.call(particles,RC, L,true);
        System.out.println("\n\n\n");
        if(particlesN2 == null) return;

        particlesN2.forEach(p-> {
            StringBuilder line = new StringBuilder(p.getId() + " ");

            for(ParticleWithNeighbours n : p.neighbours){
                line.append(n.getId()).append(" ");
            }

            System.out.println(line);
        });

    }

}
