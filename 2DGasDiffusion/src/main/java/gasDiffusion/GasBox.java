package gasDiffusion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
public class GasBox{

    int holeSize,numParticles;
    float threshold;
    Double BOX_HEIGHT=0.09, BOX_WIDTH=0.24,PARTICLE_MASS=1.0,INITIAL_VELOCITY=0.1,PARTICLE_RADIUS=0.0015,THRESHOLD=7.0;
    Random random;
    List<Particle> particles;

    public GasBox(int holeSize,int numParticles,int seed,float threshold){
        this.holeSize=holeSize;
        this.numParticles=numParticles;
        this.random= new Random(seed);
        this.threshold=threshold;
        particles=new ArrayList<>();
    }

    public void run(int maxIterations,FileWriter out){
        //1) Se definen las posiciones y velocidades iniciales, los radios y tamaño de la caja.
        for(int i=0;i<numParticles;i++){
            Double x = random.nextDouble()*(BOX_WIDTH/2);
            Double y= random.nextDouble()*BOX_HEIGHT;
            Double vx = random.nextDouble();
            Double vy = Math.sqrt(-Math.pow(vx,2) + Math.pow(INITIAL_VELOCITY,2));
            particles.add(new Particle(x,y,vx,vy,PARTICLE_MASS,PARTICLE_RADIUS));
        }
        try {
            out.write(numParticles + " " + BOX_HEIGHT+ " "+BOX_WIDTH + " " + holeSize + "\n");
            for (Particle particle:particles){
                out.write(particle.x + " "+ particle.y+ "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(calculateBalance(particles)>=(50-THRESHOLD)){
            //2) Se calcula el tiempo hasta el primer choque (evento!) (tc).
            //Double timeToCollision = getTimeToCollision();
            //3) Se evolucionan todas las partículas según sus ecuaciones de movimiento hasta tc .
            //4) Se guarda el estado del sistema (posiciones y velocidades) en t = tc
            //5) Con el “operador de colisión” se determinan las nuevas velocidades después del choque, solo para las partículas que chocaron.
            //6) ir a 2).
        }

    }

    //Calcula el porcentaje de particulas en el primer rectangulo
    private Double calculateBalance(List<Particle> particles){
        int amount=0;
        for(Particle particle:particles){
            if (isInLeftRectangle(particle)) amount++;
        }
        return amount*1.0/numParticles;
    }

    public boolean isInLeftRectangle(Particle particle){
        if(particle.x < BOX_WIDTH/2) return true;
        return false;
    }
}