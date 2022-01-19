package my.gdx.game.desktop.Utils;

public class Point {
    private float x1;
    private float y1;
    public Point() {
        this.x1 = 0;
        this.y1 = 0;
    }
    public Point(float x, float y) {
        this.x1 = x;
        this.y1 = y;
    }
    public void setX(float x) {this.x1 = x;}
    public void setY(float y) {this.y1 = y;}
    public float getX() {return this.x1;}
    public float getY() {return this.y1;}
    public float getDistance(Point another) {
        return (float)Math.sqrt(Math.pow(getX()-another.getX(),2)+(Math.pow(getY()-another.getY(), 2)));
    }
    public float getDistance(float x, float y) {
        return (float)Math.sqrt(Math.pow(getX()-x,2)+(Math.pow(getY()-y, 2)));
    }
    public float calcDegree(Point another) {
        float hypotenus = this.getDistance(another);
        float squareegde;
        if (this.getY() < another.getY()) 
            squareegde = this.getDistance(this.getX(),another.getY());
        else
            squareegde = another.getDistance(another.getX(),this.getY());
        if (hypotenus == 0) return 0;
        // if (this.getX() < another.getX())  {
        //     if (this.getY() < another.getY())
        //         squareegde *= -1;
        // }
        // else 
        //     if (this.getY() > another.getY())
        //         squareegde *= -1;
        if (this.getX() > another.getX()) {
            if (this.getY() < another.getY())
            return (float) ((float) (Math.asin(-squareegde/hypotenus) * 180 / Math.PI) + 180);
        }
        else {
            if (this.getY() > another.getY())
            return (float) ((float) (Math.asin(-squareegde/hypotenus) * 180 / Math.PI) + 180);
        }
        return (float) (Math.asin(squareegde/hypotenus) * 180 / Math.PI);
    }
}
