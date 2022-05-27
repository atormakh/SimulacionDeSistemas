import java.util.List;

public class Zombie extends Entity {

    boolean eating = false;
    double eatingTime = 7;
    boolean converting = false;
    double eatingTimeLeft = eatingTime;
    Zombie food = null;
    double zombieVision = 4;

    public Zombie(double x, double y) {
        super(x, y);
    }

    @Override
    void update(double dt) {
        List<Entity> entities = environment.getAgents();
        Vec2 nc = new Vec2(0, 0);

        //check if converting
        if (converting) return;

        //check if eating
        if (eating) {
            eatingTimeLeft -= dt;
            if (eatingTimeLeft <= 0) {
                eating = false;
                eatingTimeLeft = eatingTime;
                food.converting = false;
            }
            return;
        }

        //get closest human;
        Human h = null;
        double dh = Double.MAX_VALUE;
        for (Entity entity : entities) {
            if (entity instanceof Human) {
                double d = position.dist(entity.position);
                if (d < dh) {
                    dh = d;
                    h = (Human) entity;
                }
            }
        }

        //set desired position
        if (h != null && dh < zombieVision) {
            if (dh < h.radius + radius) {
                eating = true;
                environment.removeHuman(h);
                Zombie z = new Zombie(h.position.x, h.position.y);
                z.converting = true;
                environment.addZombie(z);
                food = z;
                return;
            }
            desiredPos = h.position;
        } else {
            if (position.dist(desiredPos) < radius) {
                desiredPos = environment.randomPosition();
            }
        }


        //Avoid other zombies
        for (
                Entity entity : entities) {
            if (entity instanceof Zombie) {
                nc = nc.add(calculateNc(entity.position));
            }
        }

        velocity = desiredPos.sub(position).normalize().mul(environment.vz);
    }

}
