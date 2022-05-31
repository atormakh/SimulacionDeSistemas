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

        Zombie z = getClosestZombie();
        double dz = z == null ? 0 : z.position.dist(position);

        if (z != null && dz <= environment.zombieVision) {
            desiredPos = position.sub(z.position).normalize().add(position);
            if(desiredPos.norm() > environment.radius){
                Vec2 radial = position.normalize();
                Vec2 tang = radial.rotate(-Math.PI/2);
                desiredPos = tang.mul(desiredPos.dot(tang)).add(position);
                //desiredPos = desiredPos.rotate(Math.PI/2);
            }
        }
        else
            r = environment.rmin;



        move(dt);

    }
}

