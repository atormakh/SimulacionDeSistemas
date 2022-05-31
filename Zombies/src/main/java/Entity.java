import java.util.List;

public abstract class Entity {
    Vec2 position, velocity, acceleration;
    double desiredVelocity = 0;
    double r;
    Environment environment = Environment.getInstance();

    Vec2 desiredPos;


    protected Entity(double x, double y) {
        this.position = new Vec2(x, y);
        this.velocity = new Vec2(0, 0);
        this.acceleration = new Vec2(0, 0);
        this.desiredPos = environment.randomPosition();
        r = environment.rmin;

    }


    Vec2 calculateNc(Vec2 obstacle) {
        double distance = obstacle.dist(position);
        //cos(theta) = a.b/(||a||*||b||)
        Vec2 obstacleVec = obstacle.sub(position).normalize();
        Vec2 desiredVec = desiredPos.sub(position).normalize();
        double cos = obstacleVec.dot(desiredVec);
        double mod = environment.Ap * Math.exp(-distance / environment.Bp) * cos;

        return obstacleVec.mul(-mod);
    }

    abstract void update(double dt);

}
