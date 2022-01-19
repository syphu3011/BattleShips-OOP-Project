package my.gdx.game.desktop.Utils;

public class Size {
    private float width;
    private float height;
    public Size() {}
    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public float getWidth() {
        return this.width;
    }
    public float getHeight() { 
        return this.height;
    }
}
