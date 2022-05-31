import java.util.List;

public abstract class Entity {
    Vec2 position, velocity, acceleration;
    double desiredVelocity = 0;
    double r;
    Environment environment = Environment.getInstance();

    Vec2 desiredPos;


    protected Zombie getClosestZombie() {
        Zombie z = null;
        double dz = Double.MAX_VALUE;

        for (Zombie zombie : environment.zombies) {
            if(zombie == this) continue;
            double d = position.dist(zombie.position);
            if (d < dz) {
                dz = d;
                z = zombie;
            }
        }
        return z;
    }

    protected Human getClosestHuman() {
        Human h = null;
        double dh = Double.MAX_VALUE;
        for (Entity entity : environment.entities) {
            if (entity instanceof Human && entity != this) {
                double d = position.dist(entity.position);
                if (d < dh) {
                    dh = d;
                    h = (Human) entity;
                }
            }
        }
        return h;
    }

    protected Vec2 handleCollisions() {
        Vec2 escapeVel = null;
        Vec2 wall = environment.getClosestWall(position);
        //collisions
        if (wall != null && position.dist(wall) < r)
            escapeVel = wall.sub(position).normalize().mul(-environment.vh);
        if (escapeVel == null) {
            for (Entity entity : environment.entities) {
                if (this.getClass() == entity.getClass() && entity != this) {
                    if (position.dist(entity.position) < r + entity.r) {
                        escapeVel = position.sub(entity.position).normalize().mul(environment.vh);
                        break;
                    }
                }
            }
        }
        return escapeVel;
    }

    protected Vec2 handleAvoidance() {
        Vec2 obstacle = environment.getClosestWall(position);
        Entity e = this instanceof Human ? getClosestHuman() : getClosestZombie();

        obstacle = e!=null && e.position.dist(position) < obstacle.dist(position) ? e.position : obstacle;

        return calculateNc(obstacle);
    }

    protected void move(double dt) {
        Vec2 escapeVel = handleCollisions();
        if (escapeVel == null) {
            Vec2 nc = handleAvoidance();
            Vec2 eit = desiredPos.sub(position).normalize();
            Vec2 eia = nc.add(eit).normalize();
            double mod = desiredVelocity * Math.pow((r - environment.rmin) / (environment.rmax - environment.rmin), environment.beta);
            if (r < environment.rmax) r = r + environment.rmax * dt / environment.tau;
            velocity = eia.mul(mod);
        } else {
            velocity = escapeVel;
            r = environment.rmin;
        }
        position = position.add(velocity.mul(dt));
    }


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
