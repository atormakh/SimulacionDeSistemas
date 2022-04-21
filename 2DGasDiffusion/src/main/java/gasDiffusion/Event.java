package gasDiffusion;


import java.util.List;

public abstract class Event implements Comparable<Event> {
    double time;
    boolean valid = true;


    public Event(double tc) {
        if(tc < 0) {
            System.out.println("Negative time");
        }
        this.time = tc;
    }

    public void invalidate() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public double getTime() {
        return time;
    }

    public abstract void updateParticles();

    public abstract List<Particle> getParticles();

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.time);
    }
}
