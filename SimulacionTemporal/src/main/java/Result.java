public class Result {
    double position;
    double velocity;
    double acceleration;

    public Result(double position, double velocity, double acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getAcceleration() {
        return acceleration;
    }
}
