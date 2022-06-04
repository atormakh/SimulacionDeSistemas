
public class Human extends Entity {
    public Human(double x, double y) {
        super(x, y);
        desiredVelocity = environment.vh;
    }


    @Override
    void update(double dt) {
        //set desired position avoid zombies

        Zombie z = getClosestZombie();
        double dz = z == null ? 0 : z.position.dist(position);

        if (z != null && dz <= environment.zombieVision) {
            desiredPos = position.sub(z.position).normalize().add(position);
        }
        else {
            //Human h = getClosestHuman();
            //if (h!=null && h.position.dist(position) <= environment.zombieVision) {
            //    desiredPos = position.sub(h.position).normalize().mul(1).add(position);
            //}else
            //    desiredPos = position;

            r = environment.rmin;
            desiredPos = position;
        }



        move(dt);

    }
}

