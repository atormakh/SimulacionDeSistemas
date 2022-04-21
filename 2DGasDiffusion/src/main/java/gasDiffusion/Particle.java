package gasDiffusion;

public class Particle {
    Double x,y,vx,vy,mass,radius;

    public Particle(Double x, Double y, Double vx, Double vy,Double mass,Double radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.mass=mass;
        this.radius=radius;
    }


    public void update(double time) {
        x += vx * time;
        y += vy * time;
    }
}
