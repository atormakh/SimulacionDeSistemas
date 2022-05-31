import java.net.URI;

public class Vec2 {
    double x,y;


    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double dist(Vec2 v) {
        return Math.sqrt((this.x - v.x)*(this.x - v.x) + (this.y - v.y)*(this.y - v.y));
    }

    public Vec2 sub(Vec2 position) {
        return new Vec2(x-position.x, y- position.y);
    }

    public double norm(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }

    public Vec2 normalize(){
        double norm = this.norm();
        if(norm == 0) return new Vec2(0,0);
        return new Vec2(x/norm, y/norm);
    }

    public double dot(Vec2 v) {
        return this.x*v.x + this.y*v.y;
    }

    public Vec2 mul(double v) {
        return new Vec2(this.x*v, this.y*v);
    }

    public Vec2 add(Vec2 calculateNc) {
        return new Vec2(this.x+calculateNc.x, this.y+calculateNc.y);
    }
}
