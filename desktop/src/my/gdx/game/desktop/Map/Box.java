package my.gdx.game.desktop.Map;
import my.gdx.game.desktop.Utils.Point;

public class Box {
    private boolean clash;//true is my clash, false is enemy clash
    private int index;
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private boolean contained;
    private Point position;
    public Box(){}
    public Box(boolean clash, float xMin, float xMax, float yMin, float yMax, int index) {
        this.clash = clash;
        this.index = index;
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.position = new Point(xMin,yMin);
        this.contained = false;
    }
    public boolean getClash() {
        return this.clash;
    }
    public int getIndex() {
        return this.index;
    }
    public Point getPoint() {
        return this.position;
    }
    public float getWidth() {
        return xMax - xMin;
    }
    public float getHeight() {
        return yMax - yMin;
    }
    public float getxMax() {
        return xMax;
    }
    public float getxMin() {
        return xMin;
    }
    public float getyMax() {
        return yMax;
    }
    public float getyMin() {
        return yMin;
    }
    public void setContained(boolean contained) {
        this.contained = contained;
    }
    public boolean getContained() {
        return this.contained;
    }
    public boolean contains(float x, float y,boolean clash) {
        if (this.getClash() != clash) return false;
        if (x >= xMin && x <= xMax) {
            if (y >= yMin && y <= yMax) {
                this.contained = true;
                return true;
            }
        }
        return false;
    }
    public boolean contains(Point another,boolean clash) {
        if (this.getClash() != clash) return false;
        if (another.getX()  >= xMin && another.getX() <= xMax) {
            if (another.getY() >= yMin && another.getY() <= yMax) {
                this.contained = true;
                return true;
            }
        }
        return false;
    }
}
