import java.util.List;

public class Zombie extends Entity {

    boolean eating = false;
    boolean converting = false;
    double eatingTimeLeft;
    Zombie food = null;


    public Zombie(double x, double y) {
        super(x, y);
        eatingTimeLeft = environment.eatingTime;
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
                eatingTimeLeft = environment.eatingTime;
                food.converting = false;
                food.r = environment.rmin;
                r = environment.rmin;
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

        //set desired position to closest human
        if (h != null && dh < environment.zombieVision) {
            if (dh < h.r + r) { //if human is close enough to eat
                eating = true;
                environment.removeHuman(h);
                Zombie z = new Zombie(h.position.x, h.position.y);
                z.converting = true;
                environment.addZombie(z);
                food = z;
                return;
            }
            desiredVelocity = environment.vz;
            desiredPos = h.position;
        } else {
            desiredVelocity = environment.inactiveZombieVelocity;
            if (position.dist(desiredPos) < r) {
                desiredPos = environment.randomPosition();
            }
        }


        //Avoid other zombies
        for (Entity entity : entities) {
            if (entity instanceof Zombie && entity != this) {
                nc = nc.add(calculateNc(entity.position));
            }
        }

        //Wall avoidance
        Vec2 wall = environment.getClosestWall(position);
        if(wall != null) {
            nc = nc.add(calculateNc(wall));
        }


        //update
        Vec2 eit = desiredPos.sub(position).normalize();
        Vec2 eia = nc.add(eit).normalize();

        double mod = desiredVelocity * Math.pow((r - environment.rmin) / (environment.rmax - environment.rmin), environment.beta);
        if (r < environment.rmax) r = r + environment.rmax * dt / environment.tau;
        velocity = eia.mul(mod);
        position = position.add(velocity.mul(dt));


    }

}
