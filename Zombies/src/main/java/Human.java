import java.util.List;

public class Human extends Entity {
    public Human(double x, double y) {
        super(x, y);
    }


    @Override
    void update(double dt) {
        List<Entity> entities = environment.getAgents();
        Vec2 nc = new Vec2(0, 0);

        //set desired position

        //Avoid Humans
        for (Entity entity : entities) {
            if (entity instanceof Human) {
                nc = nc.add(calculateNc(entity.position));
            }
        }

        //Wall avoidance


    }
}

