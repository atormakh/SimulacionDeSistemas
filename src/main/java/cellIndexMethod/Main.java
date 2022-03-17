package cellIndexMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        boolean generate = Boolean.parseBoolean(System.getProperty("generate", "false"));
        boolean periodic = Boolean.parseBoolean(System.getProperty("periodic", "false"));
        boolean bf = Boolean.parseBoolean(System.getProperty("bf", "false"));
        double L = Integer.parseInt(System.getProperty("L", "100"));
        int MAX_R = Integer.parseInt(System.getProperty("R", "2"));
        int N = Integer.parseInt(System.getProperty("N", "100"));
        double RC = Double.parseDouble(System.getProperty("RC", "40"));
        String generationOutput = System.getProperty("generation", "output.txt");
        String output = System.getProperty("output", "neighbours-output.txt");
        String staticFilePath = System.getProperty("static", "static-output.txt");
        String dynamicFilePath = System.getProperty("dynamic", "dynamic-output.txt");

        List<Particle> particles = new ArrayList<>();
        System.out.printf("running with RC=%.2f%n",RC);
        if (generate) {
            particles = ParticleGenerator.generate(generationOutput,N,L,MAX_R);
        } else {
            try {
                File file = new File(staticFilePath);
                Scanner scanner = new Scanner(file);
                ArrayList<Particle> tmp = new ArrayList<>();
                N = scanner.nextInt();
                L = scanner.nextDouble();
                while(scanner.hasNextLine()){
                    if(scanner.hasNextDouble())
                        tmp.add(new Particle(0,0,scanner.nextDouble(),scanner.nextInt()));
                    else scanner.nextLine();
                }

                file = new File(dynamicFilePath);
                scanner = new Scanner(file);

                scanner.nextDouble();

                int i=0;
                while(scanner.hasNextLine()){

                    if(scanner.hasNextDouble()) {
                        Particle p = tmp.get(i);
                        particles.add(new Particle(scanner.nextDouble(), scanner.nextDouble(), p.getR(), p.id));
                    }
                    else scanner.nextLine();
                    i++;
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        long start = System.nanoTime();
        List<ParticleWithNeighbours> particlesN = bf ? BruteForceMethod.call(particles, RC, L, periodic) : CellIndexMethod.call(particles, RC, L, periodic);
        long end = System.nanoTime();
        System.out.println((bf?"Bruteforce: ":"Cell Index Method: ") + (end-start)/1000000 + "ms");
        if (particlesN == null) return;

        FileWriter out;
        try {
            out = new FileWriter(output);
            out.write(String.format("%.2f\n",RC));
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
                out.write(String.format("%s\n",line));
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
