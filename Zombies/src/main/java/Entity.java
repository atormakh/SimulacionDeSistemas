public abstract class Entity {
    Vec2 position, velocity, acceleration;
    double radius;
    double maxVel;

    Vec2 desiredDirection;


    protected Entity(Vec2 position,Vec2 velocity,Vec2 acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    abstract void changeDirection(Vec2 velocity, Vec2 acceleration);
}
