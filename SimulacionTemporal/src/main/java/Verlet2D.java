

public class Verlet2D {

    Vector2 current_pos, old_pos, current_vel;
    double mass;
    double dt;
    Vector2 force;
    Force2DFunction forceFunction;

    public Verlet2D(Force2DFunction forceFunction, Vector2 pos, Vector2 vel, double mass, double dt) {
        this.forceFunction = forceFunction;
        this.current_pos = pos;
        this.old_pos = current_pos.substract(vel.scalarMultiply(dt));
        this.current_vel = vel;
        this.mass = mass;
        this.dt = dt;
    }


    public Result2D get() {

        Vector2 f = forceFunction.getForce(current_pos, current_vel);
        Vector2 new_pos = current_pos.scalarMultiply(2).substract(old_pos).add(f.scalarMultiply(dt*dt / mass));
        Vector2 new_v = (new_pos.substract(old_pos)).scalarMultiply(1 / (2 * dt));
        old_pos = current_pos;
        current_pos = new_pos;
        current_vel = new_v;
        return new Result2D(new_pos, new_v, f.scalarMultiply(1/mass));
    }
}
