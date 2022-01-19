package my.gdx.game.desktop.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;
/**
 * MyActor
 */
public class MyActor extends Actor{
    private Point position;
    private Texture texture;
    private float timeKeyFrame = 0;
    public MyActor(){}
    // public MyActor (String textureName) {
    //     texture = new Texture(Gdx.files.internal(textureName));
    //     setBounds(getX()-texture.getWidth()/2, getY()-texture.getHeight()/2, texture.getWidth(), texture.getHeight());
    //     this.setOrigin(Align.center);
    // }
    // public MyActor(String textureName, Point Position){
    //     texture = new Texture(Gdx.files.internal(textureName));
    //     setBounds(Position.getX()-texture.getWidth()/2, Position.getY()-texture.getHeight()/2, texture.getWidth(), texture.getHeight());
    //     this.position = Position;
    //     this.setOrigin(Align.center);
    // }
    // public MyActor(String textureName, Point Position, Size size) {
    //     texture = new Texture(Gdx.files.internal(textureName));
    //     setBounds(Position.getX()-size.getWidth()/2, Position.getY()-size.getWidth()/2, size.getWidth(), size.getHeight());
    //     this.position = Position;
    //     this.setOrigin(Align.center);
    // }
    public void setPosition(Point position, Size size) {
        // TODO Auto-generated method stub
        super.setPosition(position.getX()-size.getWidth()/2, position.getY()-size.getHeight()/2);
        this.position = new Point(position.getX()-size.getWidth()/2, position.getY()-size.getHeight()/2);
    }
    public void setPosition(Point position) {
        // TODO Auto-generated method stub
        super.setPosition(position.getX(), position.getY());
        this.position = position;
    }
    public MyActor (String textureName) {
        texture = new Texture(Gdx.files.internal(textureName));
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
        this.setOrigin(Align.center);
    }
    public MyActor(String textureName, Point Position){
        texture = new Texture(Gdx.files.internal(textureName));
        setBounds(Position.getX()-texture.getWidth()/2, Position.getY()-texture.getHeight()/2, texture.getWidth(), texture.getHeight());
        this.position = Position;
        this.setOrigin(Align.center);
    }
    public MyActor(String textureName, Point Position, Size size) {
        texture = new Texture(Gdx.files.internal(textureName));
        setBounds(Position.getX()-size.getWidth()/2, Position.getY()-size.getHeight()/2, size.getWidth(), size.getHeight());
        this.position = Position;
        this.setOrigin(Align.center);
        
    }
    public Point getPoint() {
        return this.position;
    }
    
    public Texture getTexture() {
        return texture;
    }

    public float getTimeKeyFrame() {
        return timeKeyFrame;
    }
    public void setTimeKeyFrame(float timeKeyFrame) {
        this.timeKeyFrame = timeKeyFrame;
    }
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    public boolean Contains(float x,float y) {
        float xMin = this.getX();
        float xMax = this.getX() + this.getWidth();
        float yMin = this.getY();
        float yMax = this.getY() + this.getHeight();
        if (this.getRotation() == 90) {
            xMin = this.getX() + this.getWidth()/2  - this.getHeight()/2;
            xMax = this.getX() + this.getWidth()/2  + this.getHeight()/2;
            yMin = this.getY() + this.getHeight()/2  - this.getWidth()/2;
            yMax = this.getY() + this.getHeight()/2  + this.getWidth()/2;
        }
        if (xMin <= x && xMax >= x) {
            if (yMin <= y && yMax >= y) {
                return true;
            }
        }
        return false;
    }
    public boolean normalContain(float x,float y) {
        float xMin = this.getX();
        float xMax = this.getX() + this.getWidth();
        float yMin = this.getY();
        float yMax = this.getY() + this.getHeight();
        if (xMin <= x && xMax >= x) {
            if (yMin <= y && yMax >= y) {
                return true;
            }
        }
        return false;
    }
    public void moveToOnTime(Point Position,float time) {
        MoveToAction moveToAction = new MoveToAction();
		moveToAction.setPosition(Position.getX(),Position.getY());
		moveToAction.setDuration(time);
		this.addAction(moveToAction);
        System.out.println(texture.getWidth());
    }
    public void moveToOnSpeed(Point Position,float speed) {
        Point thisPoint = new Point(this.getX(),this.getY());
        float Distance = Position.getDistance(thisPoint);
		this.addAction(Actions.moveTo(Position.getX(), Position.getY(), Distance/speed));
        System.out.println(texture.getWidth());
    }
    public void moveToOnTimeRemove(Point Position,float time) {
        this.addAction(Actions.sequence(
        Actions.moveTo(Position.getX(), Position.getY(),time),
        Actions.removeActor()));
        System.out.println(texture.getWidth());
    }
    public void moveToOnSpeedRemove(Point Position,float speed) {
        Point thisPoint = new Point(this.getX(),this.getY());
        float Distance = Position.getDistance(thisPoint);
		this.addAction(Actions.sequence(
        Actions.moveTo(Position.getX(), Position.getY(), Distance/speed),
        Actions.removeActor()));
    }
    public void rotateOrtientedOther(MyActor other, boolean clash) {
        Point p = new Point(other.getX(),other.getY());
        float degree = this.position.calcDegree(p);
        if (clash) 
            this.addAction(Actions.rotateTo(degree));
        else 
            this.addAction(Actions.rotateTo(degree));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //batch.draw(texture, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY());
        batch.draw(texture, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        
        // this.position.setX(this.getX());
        // this.position.setY(this.getY());
    }
}