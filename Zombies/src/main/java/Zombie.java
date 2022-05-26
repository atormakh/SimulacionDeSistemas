public class Zombie extends Entity{

    public Zombie(Vec2 position, Vec2 velocity, Vec2 acceleration) {
        super(position, velocity, acceleration);
    }

    @Override
    void changeDirection(Vec2 velocity, Vec2 acceleration) {
        System.out.println("CHANGE DIRECTION ZOMBIE");
    }
}
