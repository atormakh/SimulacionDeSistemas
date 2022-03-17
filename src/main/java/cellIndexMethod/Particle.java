package cellIndexMethod;

public class Particle {
    final double x;
    final double y;
    final double r;
    final long id;

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
