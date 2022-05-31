import java.util.List;

public class Human extends Entity {
    public Human(double x, double y) {
        super(x, y);
        desiredVelocity = environment.vh;
    }


    @Override
    void update(double dt) {
        List<Entity> entities = environment.getAgents();
        Vec2 nc = new Vec2(0, 0);

        //set desired position avoid zombies
        Zombie z = null;
        double dz = Double.MAX_VALUE;

        for (Zombie zombie : environment.zombies) {

                double d = position.dist(zombie.position);
                if (d < dz) {
                    dz = d;
                    z = zombie;
                }
        }

        if(z!=null)
            desiredPos = position.sub(z.position).normalize().add(position);

        //Avoid Humans
        for (Entity entity : entities) {
            if (entity instanceof Human && entity != this) {
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

