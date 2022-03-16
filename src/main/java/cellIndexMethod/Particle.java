package cellIndexMethod;

public class Particle {
    double x,y,r;
    long id;

    public Particle(double x, double y, double r, long id) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public long getId() {
        return id;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                '}';
    }
}
