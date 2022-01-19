package my.gdx.game.desktop.Actor;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import my.gdx.game.desktop.Screens.GameScreen;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public class Ships extends MyActor{
    private boolean touched;
    private int id;
    private int hp;
    private int attack;
    private int defensepercent;
    private float criticalpercent;
    private boolean clash;
    private float delay;
    private Ships AttacktoAny;
    private boolean animateBoom;
    private boolean status = false;
    private String textureName;
    private int type;
    private int level;
    public Ships(){}
    public Ships(String textureName, Point Position, Size size,int hp, int attack, int defensepercent, float criticalpercent, boolean clash, int id, float delay,int type,int level) {
        super("Ships/"+textureName+level+".png", Position, size);
        this.hp = hp;
        this.attack = attack;
        this.defensepercent = defensepercent;
        this.criticalpercent = criticalpercent;
        this.clash = clash;
        this.setRotation(90);
        if (!clash) {
            this.setScale(-1, -1);
        }
        this.id = id;
        this.delay = delay;
        this.type = type;
        this.textureName = textureName;
        this.level = level;
    }
    public Ships(String textureName, Point Position,int hp, int attack, int defensepercent, float criticalpercent, boolean clash, int id, float delay,int type,int level) {
        super("Ships/"+textureName+level+".png", Position);
        this.hp = hp;
        this.attack = attack;
        this.defensepercent = defensepercent;
        this.criticalpercent = criticalpercent;
        this.clash = clash;
        this.setRotation(90);
        if (!clash) {
            this.setScale(-1, -1);
        }
        this.id = id;
        this.delay = delay;
        this.type = type;
        this.textureName = textureName;
        this.level = level;
    }
    public boolean getTouched() {
        return touched;
    }
    public void setTouched(boolean touched) {
        this.touched = touched;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public float getDelay() {
        return delay;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public int getAttack() {
        return attack;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public int getDefensepercent() {
        return defensepercent;
    }
    public void setDefensepercent(int defensepercent) {
        this.defensepercent = defensepercent;
    }
    public float getCriticalpercent() {
        return criticalpercent;
    }
    public void setCriticalpercent(float criticalpercent) {
        this.criticalpercent = criticalpercent;
    }
    public boolean getAnimateBoom() {
        return animateBoom;
    }
    public void setAnimateBoom(boolean animateBoom) {
        this.animateBoom = animateBoom;
    }
    
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public void upLevel() {
        this.level += 1;
        this.setTexture(new Texture(Gdx.files.internal("Ships/"+textureName+this.level+".png")));
        this.setHp(hp+(int)((level - 1)*1.25*hp));
        this.setAttack(attack+(int)((level - 1)*1.1*attack));
        this.setDefensepercent(defensepercent+level-1);
    }
    public void beAttack(int damage) {
        if (this.hp > 0) {
            if (clash)
                GameScreen.defend += damage;
            else 
                GameScreen.attack += damage;
            if (RandomMethod(this.criticalpercent)) {
                damage *= 1.75;
            }
            this.hp -= damage * (100 - this.defensepercent) / 100f;
            this.hp = this.hp < 0 ? 0 : this.hp;
            System.out.println("hp "+this.hp);
            this.dead();
            animateBoom = true;
        }
    }
    public void Attack(Point position,float delay, final Group background) {
        if (this.getHp() > 0) {
            if (AttacktoAny == null || AttacktoAny.getHp() <= 0) {
                AttacktoAny = GameScreen.getShipsAttackTo(clash);
                if (AttacktoAny == null) 
                    return; 
            }
            final float CRITICALPERCENT = this.criticalpercent;
            final int ATTACK = this.attack;
            final boolean CLASH = this.clash;
            final Ships ATTACKTO = AttacktoAny;
            final Point THISPOINT = new Point(position.getX(), position.getY());
            System.out.println(THISPOINT.getX() +" "+ THISPOINT.getY());
            final MyActor BULLET = new MyActor("bullet.png", position,new Size(10,10));
            final float DELAY = delay;
            rotateOrtientedOther(ATTACKTO,CLASH);
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    System.out.println("lalal");
                    if (RandomMethod(CRITICALPERCENT))
                        ATTACKTO.beAttack(ATTACK * 2);
                    else 
                        ATTACKTO.beAttack(ATTACK);
                }
            });
            final RunnableAction run1 = new RunnableAction();
            run1.setRunnable(new Runnable() {
                @Override
                public void run() {
                    if (ATTACKTO != null)
                        Attack(THISPOINT,DELAY,background);
                    else {
                        System.out.println("nef");
                    }
                }
            }); 
            SequenceAction shoot = Actions.sequence(
            Actions.moveTo(ATTACKTO.getX() + ATTACKTO.getWidth()/2, ATTACKTO.getY(),delay),run,Actions.removeActor());
            DelayAction shoot1 = Actions.delay(delay+0.1f,run1);
            background.addActor(BULLET);
            BULLET.addAction(shoot);
            background.addAction(shoot1);
        }
    }
    public void dead() {
        if (this.hp == 0) {
            if (this.clash) {
                GameScreen.MyDead(this);
            }
            else {
                GameScreen.EnemyDead(this);
            }
            //this.remove();
        }
    }
    private static boolean RandomMethod(float percent) {
        float randomVar = (new Random(100)).nextFloat();
        if ((randomVar - (float)(int)randomVar)*100.0 < percent)
            return true;
        return false;
    }
}
