public class OscilatorForce implements ForceFunction{

    private double k;
    private double gamma;

    public OscilatorForce(double k, double gamma){
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public double getForce(double r, double r1) {
        return -k*r-gamma*r1;
    }
}
