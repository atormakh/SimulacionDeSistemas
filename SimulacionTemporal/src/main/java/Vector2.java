import java.util.List;

public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 scalarMultiply(double v) {
        return new Vector2(x * v, y * v);
    }

    public double distance(Vector2 position) {
        return Math.sqrt(Math.pow(x - position.x, 2) + Math.pow(y - position.y, 2));
    }

    public Vector2 substract(Vector2 v) {
        return new Vector2(x-v.x,y-v.y);
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }
}
