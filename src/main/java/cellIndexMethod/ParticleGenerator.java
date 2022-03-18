package cellIndexMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParticleGenerator implements Supplier<Particle> {
    final double L;
    final double MAX_R;
    long N=0;
    long id = 1;

    public ParticleGenerator(double L, double MAX_R){
        this.L = L;
        this.MAX_R = MAX_R;
    }

    @Override
    public Particle get() {
        N++;
        return new Particle(Math.random()*L,Math.random()*L, Math.random()*MAX_R,id++);
    }

    public static List<Particle> generate(String output, int N, double L, double MAX_R){
        ParticleGenerator generator = new ParticleGenerator(L,MAX_R);

        List<Particle> particles = Stream.generate(generator).limit(N).collect(Collectors.toList());

        try {
            FileWriter staticFile = new FileWriter("static-" + output);
            FileWriter dynamicFile = new FileWriter("dynamic-" + output);
            staticFile.write(String.format(Locale.US,"%d\n",N));
            staticFile.write(String.format(Locale.US,"%.2f\n",L));
            dynamicFile.write("0\n");

            for(Particle p : particles){
                staticFile.write(String.format(Locale.US,"%.2f %d\n",p.getR(),p.getId()));
                dynamicFile.write(String.format(Locale.US,"%.2f %.2f\n",p.getX(),p.getY()));
            }
            staticFile.close();
            dynamicFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return particles;
    }

    public static void main(String[] args) {
        String output = System.getProperty("output","output.txt");
        int L = Integer.parseInt(System.getProperty("L","100"));
        double MAX_R = Integer.parseInt(System.getProperty("R","2"));
        int N = Integer.parseInt(System.getProperty("N","100"));

        ParticleGenerator.generate(output,N,L,MAX_R);

    }
}
