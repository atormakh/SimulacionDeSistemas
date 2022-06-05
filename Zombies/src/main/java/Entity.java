import java.util.List;

public abstract class Entity {
    Vec2 position, velocity, acceleration;
    double desiredVelocity = 0;
    double r;
    Environment environment = Environment.getInstance();

    Vec2 desiredPos;

    Human closestHuman;
    Zombie closestZombie;
    double humantimestamp = -1;
    double zombietimestamp = -1;

    int type;


    protected Zombie getClosestZombie() {

        if (zombietimestamp == environment.t)
            return closestZombie;

        Zombie z = null;
        double dz = Double.MAX_VALUE;

        for (Zombie zombie : environment.zombies) {
            if (zombie == this) continue;
            double d = position.dist(zombie.position);
            if (d < dz) {
                dz = d;
                z = zombie;
            }
        }
        closestZombie = z;
        zombietimestamp = environment.t;
        return z;
    }

    protected Human getClosestHuman() {
        if (humantimestamp == environment.t)
            return closestHuman;
        Human h = null;
        double dh = Double.MAX_VALUE;
        for (Human human : environment.humans) {
            if (human != this) {
                double d = position.dist(human.position);
                if (d < dh) {
                    dh = d;
                    h = human;
                }
            }
        }
        closestHuman = h;
        humantimestamp = environment.t;
        return h;
    }

    protected Vec2 handleCollisions() {
        Vec2 escapeVel = null;
        Vec2 wall = environment.getClosestWall(position);
        //collisions
        if (wall != null && position.dist(wall) < r)
            escapeVel = wall.sub(position).normalize().mul(-environment.vh);
        if (escapeVel == null) {
            Entity entity = this instanceof Human ? getClosestHuman() : getClosestZombie();
            if (entity != null && position.dist(entity.position) < r + entity.r) escapeVel = position.sub(entity.position).normalize().mul(environment.vh);
           /* for (Entity entity : environment.entities) {
                if (this.getClass() == entity.getClass() && entity != this) {
                    if (position.dist(entity.position) < r + entity.r) {
                        escapeVel = position.sub(entity.position).normalize().mul(environment.vh);
                        break;
                    }
                }
            }*/
        }
        return escapeVel;
    }

    protected Vec2 handleAvoidance() {

        Vec2 nc = new Vec2(0, 0);
        if (this instanceof Zombie) return new Vec2(0, 0);
        else{
            nc = nc.add(calculateNc(environment.getClosestWall(position), environment.wallAp, environment.wallBp));
            Zombie z = getClosestZombie();
            if (z != null) nc = nc.add(calculateNc(z.position, environment.zombieAp, environment.zombieBp));
            Human h = getClosestHuman();
            if (h != null) nc = nc.add(calculateNc(getClosestHuman().position, environment.entityAp, environment.entityBp));

        }
        /*nc = nc.add(calculateNc(environment.getClosestWall(position), environment.wallAp, environment.wallBp));
        List<? extends Entity> entities = this instanceof Human ? environment.humans : environment.zombies;
        for (Entity entity : entities) {
            nc = nc.add(calculateNc(entity.position, environment.entityAp, environment.entityBp));
        }

        if (this instanceof Human) {
            nc = nc.add(calculateNc(getClosestZombie().position, environment.zombieAp, environment.zombieBp));
        }*/
        return nc;

        /* Vec2 obstacle = environment.getClosestWall(position);
        Entity e = this instanceof Human ? getClosestHuman() : getClosestZombie();

        obstacle = e!=null && e.position.dist(position) < obstacle.dist(position) ? e.position : obstacle;

        return calculateNc(obstacle);*/
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


    Vec2 calculateNc(Vec2 obstacle, double Ap, double Bp) {
        double distance = obstacle.dist(position);
        //cos(theta) = a.b/(||a||*||b||)
        Vec2 obstacleVec = obstacle.sub(position).normalize();
        Vec2 desiredVec = desiredPos.sub(position).normalize();
        double cos = obstacleVec.dot(desiredVec);
        double mod = Math.abs(Ap * Math.exp(-distance / Bp) * cos);

        return obstacleVec.mul(-mod);
    }

    abstract void update(double dt);

}
