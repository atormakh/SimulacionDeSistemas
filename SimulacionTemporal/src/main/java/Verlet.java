import java.util.ArrayList;
import java.util.List;

public class Verlet extends IntegrationMethod{

    List<Double> positions;
    List<Double> velocities;

    double dt;



    public Verlet(ForceFunction forceFunction, double pos, double vel, double dt) {
        super(forceFunction);
        this.positions = new ArrayList<>();
        this.velocities = new ArrayList<>();
        this.positions.add(pos);
        this.velocities.add(vel);
        this.dt = dt;
    }

    @Override
    public Result get() {
        double pos = positions.get(positions.size() - 1);

        return new Result(0d,0d, 0d);
    }
}
