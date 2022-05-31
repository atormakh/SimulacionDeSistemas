import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Environment {
    List<Entity> entities = new LinkedList<>();
    List<Zombie> zombies = new LinkedList<>();
    double inactiveZombieVelocity = 0.3;
    double vz = 5;
    double vh = 4;
    double initialZombieDistance = 1;

    double t = 0;
    double dt = 1e-3;
    double radius;
    double Ap = 1;
    double Bp = 1;
    double rmin = 0.15;
    double rmax = 0.32;
    double beta = 0.9;
    double tau = 0.5;

    double eatingTime = 7;
    double zombieVision = 4;
    static Environment instance = null;

    public Environment(double dt, double radius) {
        this.dt = dt;
        this.radius = radius;
    }

    public static Environment init(double dt, double radius) {
        instance = new Environment(dt, radius);
        return instance;
    }

    public static Environment getInstance() {
        return instance;
    }

    public void printToFile(FileWriter out) throws IOException {
        out.write(t + "\n");
        for (Entity entity : entities) {
            int zombie = entity instanceof Zombie ? 1 : 0;
            out.write(entity.position.x + " " + entity.position.y + " " + entity.r + " " + zombie + "\n");
        }
    }

    void addZombie() {
        Zombie zombie = new Zombie(0, 0);
        entities.add(zombie);
        zombies.add(zombie);
    }

    void addHuman() {
        Human human = new Human(0, 0);
        do {
            double mod = Math.random() * radius;
            double angle = Math.random() * 2 * Math.PI;
            human.position.x = mod * Math.cos(angle);
            human.position.y = mod * Math.sin(angle);

        } while (nearZombie(human) || isColliding(human));

        entities.add(human);


    }

    public boolean areAllZombies(){
        return entities.size() == zombies.size();
    }

    private boolean isColliding(Entity entity) {
        for (Entity e : entities) {
            if (e.position.dist(entity.position) < (e.r + entity.r)) return true;
        }
        return false;
    }

    private boolean nearZombie(Entity entity) {
        for (Zombie zombie : zombies) {
            if (zombie.position.dist(entity.position) < (initialZombieDistance + entity.r + zombie.r))
                return true;
        }
        return false;
    }

    public void update() {
        List<Entity> copy = new ArrayList<>(entities);
        copy.forEach((e) -> {
            e.update(dt);
            //bounceIfNeeded(e);
            //e.move(dt);
        });
        t += dt;

    }

    public List<Entity> getAgents() {
        return entities;
    }

    public void removeHuman(Human h) {
        entities.remove(h);
    }


    public void addZombie(Zombie z) {
        entities.add(z);
        zombies.add(z);
    }

    protected Vec2 randomPosition() {
        double mod = Math.random() * radius;
        double angle = Math.random() * 2 * Math.PI;
        return new Vec2(mod * Math.cos(angle), mod * Math.sin(angle));
    }

    public Vec2 getClosestWall(Vec2 position) {
        if(position.x == 0 && position.y == 0) return new Vec2(radius,0);
        return position.normalize().mul(radius);
    }
}
