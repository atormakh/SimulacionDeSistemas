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
        Human h = getClosestHuman();
        double dh = h==null? 0:h.position.dist(position);

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

        Zombie z = getClosestZombie();
        if(z!=this && z!=null && z.position.dist(position) <= r + z.r){
            //desiredPos = environment.randomPosition();
            //z.desiredPos = environment.randomPosition();
            desiredPos = position.sub(z.position).normalize().add(position);
            z.desiredPos = z.position.sub(position).normalize().add(z.position);
            if(desiredPos.norm() >= environment.radius) desiredPos = environment.randomPosition();
            if(z.desiredPos.norm() >= environment.radius) z.desiredPos = environment.randomPosition();
        }

        move(dt);


    }

}
