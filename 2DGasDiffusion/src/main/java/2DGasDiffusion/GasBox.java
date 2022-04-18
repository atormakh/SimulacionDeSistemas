package 2DGasDiffusion;
import java.util.Random;
import java.io.FileWriter;
public class GasBox{

    int holeSize,numParticles;
    float threshold;
    Random random;

    public GasBox(int holeSize,int numParticles,int seed,float threshold){
        this.holeSize=holeSize;
        this.numParticles=numParticles;
        this.random= Random(seed);
        this.threshold=threshold;
    }

    public void run(int maxIterations,FileWriter out){
        //1) Se definen las posiciones y velocidades iniciales, los radios y tamaño de la caja.
        //2) Se calcula el tiempo hasta el primer choque (evento!) (tc).
        //3) Se evolucionan todas las partículas según sus ecuaciones de movimiento hasta tc .
        //4) Se guarda el estado del sistema (posiciones y velocidades) en t = tc
        //5) Con el “operador de colisión” se determinan las nuevas velocidades después del choque, solo para las partículas que chocaron.
        //6) ir a 2).
    }
}