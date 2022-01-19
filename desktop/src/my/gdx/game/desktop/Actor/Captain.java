package my.gdx.game.desktop.Actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public abstract class Captain extends MyActor {
    private String id;
    private String textureName;
    private int price;
    private String attribute;
    private double bonus;
    private boolean status;
    public Captain(String textureName,Point Position,Size size,String id,int price,String attribute,double bonus) {
        super(textureName+"_0.png",Position, size);
        this.setId(id);
        this.setTextureName(textureName);
        this.price=price;
        this.setAttribute(attribute);
        this.bonus=bonus;
    }

    public Captain(String textureName,String id,int price,String attribute,double bonus) {
        super(textureName+"_0.png");
        this.setId(id);
        this.setTextureName(textureName);
        this.price=price;
        this.setAttribute(attribute);
        this.bonus=bonus;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    public String getTextureName() {
        return textureName;
    }
    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public double getBonus() {
        return bonus;
    }
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    public abstract Animation<AtlasRegion> getAnimate();
}
