import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    Vec2 position, velocity, acceleration;
    double desiredVelocity = 0;
    double r;
    Environment environment = Environment.getInstance();

    Vec2 desiredPos;

    Human closestHuman;
    Zombie closestZombie;
    double timestamp;

    List<Entity> neighbours;

    public void addNeighbour(Entity entity) {
        neighbours.add(entity);
    }

    public void clearNeighbours() {
        neighbours.clear();
        closestHuman = null;
        closestZombie = null;
    }

    public double getR() {
        return r;
    }

    public void checkNeighbours(){
        double dz = Double.MAX_VALUE;
        double dh = Double.MAX_VALUE;
        closestHuman = null;
        closestZombie = null;
        for (Entity entity : neighbours) {
            if (entity instanceof Zombie && position.dist(entity.position) < dz) {
                dz = position.dist(entity.position);
                closestZombie = (Zombie) entity;
            }
            if (entity instanceof Human && position.dist(entity.position) < dh) {
                dh = position.dist(entity.position);
                closestHuman = (Human) entity;
            }
        }
        timestamp = environment.t;
    }

    protected Zombie getClosestZombie() {

        if (environment.t != timestamp)
            checkNeighbours();
        return closestZombie;

    /*    Zombie z = null;
        double dz = Double.MAX_VALUE;

        for (Zombie zombie : environment.zombies) {
            if (zombie == this) continue;
            double d = position.dist(zombie.position);
            if (d < dz) {
                dz = d;
                z = zombie;
            }
        }
        zombtimestamp = environment.t;
        closestZombie = z;
        return z;*/
    }

    protected Human getClosestHuman() {
        if (environment.t != timestamp)
            checkNeighbours();
        return closestHuman;
       /* if (environment.t == humantimestamp) {
            return closestHuman;
        }
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
        humantimestamp = environment.t;
        closestHuman = h;
        return h;*/
    }

    protected Vec2 handleCollisions() {
        Vec2 escapeVel = null;
        Vec2 wall = environment.getClosestWall(position);
        //collisions
        if (wall != null && position.dist(wall) < r)
            escapeVel = wall.sub(position).normalize().mul(-environment.vh);
        if (escapeVel == null) {
            Entity entity = this instanceof Human ? getClosestHuman() : getClosestZombie();
            if (entity !=null && position.dist(entity.position) < r + entity.r) {
                escapeVel = position.sub(entity.position).normalize().mul(environment.vh);
            }
        }
        return escapeVel;
    }

    protected Vec2 handleAvoidance() {
        Vec2 obstacle = environment.getClosestWall(position);
        Entity e = this instanceof Human ? getClosestHuman() : getClosestZombie();

        obstacle = e != null && e.position.dist(position) < obstacle.dist(position) ? e.position : obstacle;

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
        this.neighbours = new ArrayList<>();
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
