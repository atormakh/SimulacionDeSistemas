import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Environment {
    List<Entity> entities = new LinkedList<>();
    List<Zombie> zombies = new LinkedList<>();
    List<Human> humans = new LinkedList<>();
    double inactiveZombieVelocity = 0.3;
    double vz;
    double vh = 4;
    double initialZombieDistance = 1;

    double t = 0;
    double dt;
    double radius;
    double entityAp = 2000;
    double entityBp = 0.2;
    double wallAp = 500;
    double wallBp = 0.4;
    double zombieAp = 3000;
    double zombieBp = 0.2;
    double rmin = 0.2;
    double rmax = 0.3;
    double beta = 0.9;
    double tau = 0.5;

    double eatingTime = 7;
    double zombieVision = 4;
    double np;
    static Environment instance = null;

    public Environment(double dt, double radius, double vz, double np) {
        this.dt = dt;
        this.radius = radius;
        this.vz = vz;
        this.np = np;
    }

    public static Environment init(double dt, double radius, double vz, double np) {
        instance = new Environment(dt, radius, vz, np);
        return instance;
    }

    public static Environment getInstance() {
        return instance;
    }

    public void printToFile(FileWriter out) throws IOException {
        out.write(t + " " + zombies.size() + " " + humans.size() + "\n");
        for (Entity entity : entities) {
            out.write(entity.position.x + " " + entity.position.y
                    + " " + entity.desiredPos.x + " " + entity.desiredPos.y
                    + " " + entity.r + " " +  entity.type + "\n");
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
        humans.add(human);


    }

    void addHuman(Human human) {
        entities.add(human);
        humans.add(human);
    }

    public boolean areAllSame(){
        return zombies.size() == 0 || humans.size()==0;
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
        humans.remove(h);
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

    public void killZombie(Zombie zombie) {
        zombies.remove(zombie);
    }

    public void removeZombie(Zombie zombie) {
        entities.remove(zombie);
        zombies.remove(zombie);
    }
}
