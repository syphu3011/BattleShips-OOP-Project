package my.gdx.game.desktop.Utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import my.gdx.game.desktop.Actor.MyActor;

public class Button extends MyActor {
    private boolean touch;
    private Point position = new Point();
    private Size size;
    public Button(String textureName) {
        super(textureName);
    }
    public Button(String textureName, Point position, Size size) {
        super(textureName, position, size);
        this.position = position;
        this.size = size;
        this.btnSetScalePressed();
    }
    
    public boolean getTouch() {
        return touch;
    }
    public void setTouch(boolean touch) {
        this.touch = touch;
    }
    public Size getSize() {
        return size;
    }
    public Point getPosition() {
        return position;
    }
    public void btnSetScalePressed() {
        final Button BTN = this;
        this.addListener(new ClickListener(){
            final float W = BTN.getWidth();
            final float H = BTN.getHeight();
            final float WD = W * 3f/4;
            final float HD = H * 3f/4;
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                BTN.setWidth(W);
                BTN.setHeight(H);
                BTN.setPosition(BTN.getX() + WD/2 - H/2,BTN.getY() + HD/2 - H/2);
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BTN.setWidth(WD);
                BTN.setHeight(HD);
                BTN.setPosition(BTN.getX() - WD/2 + W/2,BTN.getY() - HD/2 + H/2);
                return true;
            }
        });
    }
    @Override
    public void setSize(float width, float height) {
        // TODO Auto-generated method stub
        super.setSize(width, height);
        this.size.setWidth(width);
        this.size.setHeight(height);
    }
    @Override
    public void setPosition(Point position) {
        // TODO Auto-generated method stub
        super.setPosition(position);
        this.position = position;
    }
    @Override
    public void setPosition(float x, float y) {
        // TODO Auto-generated method stub
        super.setPosition(x, y);
        this.position.setX(x);
        this.position.setY(y);
    }
    @Override
    public boolean Contains(float x, float y) {
        // TODO Auto-generated method stub
        return touch = super.Contains(x, y);
    }
}
