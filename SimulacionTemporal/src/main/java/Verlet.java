

public class Verlet extends IntegrationMethod {

    double current_pos, old_pos, current_vel, mass;
    double dt;


    public Verlet(ForceFunction forceFunction, double pos, double vel, double mass, double dt) {
        super(forceFunction);

        this.current_pos = pos;
        this.old_pos = current_pos;
        this.current_vel = vel;
        this.mass = mass;
        this.dt = dt;
    }

    @Override
    public Result get() {

        double f = forceFunction.getForce(current_pos, current_vel);
        double new_pos = 2 * current_pos - old_pos + dt*dt * f / mass;
        double new_v = (new_pos - old_pos) / (2 * dt);
        old_pos = current_pos;
        current_pos = new_pos;
        current_vel = new_v;
        return new Result(new_pos, new_v, f / mass);
    }
}
