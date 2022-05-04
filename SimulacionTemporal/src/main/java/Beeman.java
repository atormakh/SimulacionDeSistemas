

public class Beeman extends IntegrationMethod {

    double current_pos, old_pos, current_v, old_a;

    double mass;

    double dt;


    public Beeman(ForceFunction forceFunction, double pos, double vel, double mass, double dt) {
        super(forceFunction);

        this.current_pos = pos;
        this.old_pos = current_pos;
        this.current_v = vel;
        this.mass = mass;
        this.old_a = forceFunction.getForce(current_pos, current_v) / mass;

        this.dt = dt;
    }

    @Override
    public Result get() {

        double current_a = forceFunction.getForce(current_pos, current_v) / mass;

        double new_pos = current_pos + current_v * dt
                + (2f / 3) * current_a * dt * dt
                - (1f / 6) * old_a * dt * dt;

        double new_a = forceFunction.getForce(new_pos, current_v) / mass;

        double new_v = current_v
                + (1f / 3) * new_a * dt
                + (5f / 6) * current_a * dt
                - (1f / 6) * old_a * dt;

        old_a = current_a;
        old_pos = current_pos;
        current_pos = new_pos;
        current_v = new_v;

        return new Result(new_pos, new_v, new_a);
    }
}
