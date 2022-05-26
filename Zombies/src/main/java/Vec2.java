public class Vec2 {
    double x,y;


    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double dist(Vec2 v) {
        return Math.sqrt((this.x - v.x)*(this.x - v.x) + (this.y - v.y)*(this.y - v.y));
    }
}
