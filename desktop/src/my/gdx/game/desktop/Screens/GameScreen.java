package my.gdx.game.desktop.Screens;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Actor.BlueShips;
import my.gdx.game.desktop.Actor.GreenShips;
import my.gdx.game.desktop.Actor.MyActor;
import my.gdx.game.desktop.Actor.RedShips;
import my.gdx.game.desktop.Actor.Ships;
import my.gdx.game.desktop.Actor.ShipsManagement;
import my.gdx.game.desktop.Map.Box;
import my.gdx.game.desktop.Map.MapBattle;
import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public class GameScreen extends ScreenAdapter implements  InputProcessor,ScreenInterface {
    private static Game game;
    private Stage stage;
	private Group background = new Group();
    private boolean touched = false;
    private Ships ship;
    private boolean fighting = false;
	private final Box[][] mapBattle = MapBattle.mapbattle;
    private Point[][] mapPoint;
    private Point savePosWait = new Point();
    private Point savePos = new Point();
    private Point oldPos = new Point();
    private MyActor another = new MyActor();
    private SpriteBatch batch;
    private MyActor Back = new MyActor("back.png");
    private Button buttonReRoll = new Button("Reroll.png", new Point(100, 100), new Size(100,100));
    private Point btnReRollin;
    private Point btnReRollout;
    private Label lbltimepick;
    private Label lblHp;
    private Label lblAttack;
    private Label lblDefend;
    private Label lblCritical;
    private int timePick = 0;
    private boolean setUp = false;
    private ArrayList<Ships> shipsWaiting = new ArrayList<Ships>();
    private Ships[] shipsPick = new Ships[2];
    private Button btnHome;
    private boolean statusReroll = false;
    private boolean waitPick = false;
    private static final int TIMETOPICK = 25;
    private static final float ratio = 129/324f;
    private static ShipsManagement MyShips;
    private static ShipsManagement EnemyShips;
    public static int attack;
    public static int defend;
    public GameScreen(Game g) {
        attack = 0;
        defend = 0;
        stage = new Stage(new ScreenViewport());
        MyShips = new ShipsManagement();
        EnemyShips = new ShipsManagement();
        mapPoint = new Point[mapBattle.length][mapBattle[0].length];
        for (int i = 0; i < MapBattle.NUMROW;i++) {// lấy vị trí
            float yy = MapBattle.sizeHeight*i;
            float yyy = MapBattle.sizeHeight*(i+1);
            for (int j = 0; j < MapBattle.NUMCOL;j++) {
                float xx = MapBattle.sizeWidth*j;
                float xxx = MapBattle.sizeWidth*(j+1);
                mapPoint[i][j] = new Point((xx+xxx)/2,(yy+yyy)/2);
            }
        }
        game = g;
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(this);
        setBackground();
        setItem();
        BackIn();
        addSoil();
    }
    private void addLblAttributePickAndWait(Ships point) {
        lblHp = new Label("HP: "+point.getHp(),new LabelStyle(Asset.font, Color.RED));
        lblAttack = new Label("ATTACK: "+point.getAttack(),new LabelStyle(Asset.font, Color.RED));
        lblDefend = new Label("Defend Percent: " + point.getDefensepercent(),new LabelStyle(Asset.font, Color.RED));
        lblCritical = new Label("Critical Percent: " + point.getCriticalpercent(),new LabelStyle(Asset.font, Color.RED));
        lblHp.setPosition(stage.getWidth() - 400,stage.getHeight() - 100);
        lblAttack.setPosition(stage.getWidth() - 400,stage.getHeight() - 130);
        lblDefend.setPosition(stage.getWidth() - 400,stage.getHeight() - 160);
        lblCritical.setPosition(stage.getWidth() - 400,stage.getHeight() - 190);
        background.addActor(lblHp);
        background.addActor(lblAttack);
        background.addActor(lblDefend);
        background.addActor(lblCritical);
    }
    private void addLblAttributeFight(Ships point) {
        lblHp = new Label("HP: "+point.getHp(),new LabelStyle(Asset.font, Color.YELLOW));
        lblAttack = new Label("ATTACK: "+point.getAttack(),new LabelStyle(Asset.font, Color.YELLOW));
        lblDefend = new Label("Defend Percent: " + point.getDefensepercent(),new LabelStyle(Asset.font, Color.YELLOW));
        lblCritical = new Label("Critical Percent: " + point.getCriticalpercent(),new LabelStyle(Asset.font, Color.YELLOW));
        lblHp.setPosition(mapPoint[3][0].getX(),mapPoint[3][0].getY() + 20);
        lblAttack.setPosition(mapPoint[3][0].getX(),mapPoint[3][0].getY() - 20);
        lblDefend.setPosition(mapPoint[3][0].getX() + 200,mapPoint[3][0].getY() + 20);
        lblCritical.setPosition(mapPoint[3][0].getX() + 200,mapPoint[3][0].getY() - 20);
        background.addActor(lblHp);
        background.addActor(lblAttack);
        background.addActor(lblDefend);
        background.addActor(lblCritical);
    }
    private void removeLblAttribute() {
        if (lblHp != null)
            lblHp.remove();
        if (lblAttack != null)
            lblAttack.remove();
        if (lblCritical != null)
            lblCritical.remove();
        if (lblDefend != null)
            lblDefend.remove();
    }
    private void addSoil() {
        Size soilSize = new Size(stage.getWidth(),stage.getHeight()/MapBattle.NUMROW);
        MyActor soil = new MyActor("soil.png", mapPoint[3][(int)MapBattle.NUMCOL/2], soilSize);
        background.addActor(soil);
    }
    private void addGreenShips(int x, int y, int level,boolean clash, int id) {
        Box box =  mapBattle[x][y];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = mapPoint[x][y];
        GreenShips greenShip = new GreenShips(point,size, 1, clash, id);
        for (int i = 1; i < level;i++) {
            greenShip.upLevel();
        }
        if (clash) MyShips.addShip(greenShip); 
        else EnemyShips.addShip(greenShip);
        background.addActor(greenShip);
    }
    private void addRedShips(int x, int y, int level,boolean clash, int id) {
        Box box =  mapBattle[x][y];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = mapPoint[x][y];
        RedShips redShip = new RedShips(point,size, 1, clash, id);
        for (int i = 1; i < level;i++) {
            redShip.upLevel();
        }
        if (clash) MyShips.addShip(redShip); 
        else EnemyShips.addShip(redShip);
        background.addActor(redShip);
    }
    private void addBlueShips(int x, int y, int level,boolean clash, int id) {
        Box box =  mapBattle[x][y];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = mapPoint[x][y];
        BlueShips blueShip = new BlueShips(point,size, 1, clash, id);
        for (int i = 1; i < level;i++) {
            blueShip.upLevel();
        }
        if (clash) MyShips.addShip(blueShip); 
        else EnemyShips.addShip(blueShip);
        background.addActor(blueShip);
    }
    private void addGreenShipsPicking(int id) {
        Box box =  mapBattle[3][id];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = new Point(Back.getX() + (Back.getWidth() / 4) * (2 * id + 1),Back.getY() + size.getHeight()/2);
        GreenShips greenShip = new GreenShips(point,size, 1, true, id);
        shipsPick[id] = greenShip;
        background.addActor(greenShip);
    }
    private void addRedShipsPicking(int id) {
        Box box =  mapBattle[3][id];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = new Point(Back.getX() + (Back.getWidth() / 4) * (2 * id + 1),Back.getY() + size.getHeight()/2);
        RedShips redShip = new RedShips(point,size, 1, true, id);
        shipsPick[id] = redShip;
        background.addActor(redShip);
    }
    private void addBlueShipsPicking(int id) {
        Box box =  mapBattle[3][id];
        Size size = new Size(box.getWidth()*0.8f, box.getWidth() * ratio * 0.8f);
        Point point = new Point(Back.getX() + (Back.getWidth() / 4) * (2 * id + 1),Back.getY() + size.getHeight()/2);
        BlueShips blueShip = new BlueShips(point,size, 1, true, id);
        shipsPick[id] = blueShip;
        background.addActor(blueShip);
    }
    private void battle() {
        enemySetUp();
        fighting = true;
        for (Ships j: MyShips.arrShips) {
            RunnableAction run = new RunnableAction();
            final Ships i = j;
            //thuyền xuất hiện
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    i.Attack(new Point(i.getX() + i.getWidth()/2,i.getY() + i.getHeight()/2), i.getDelay(), background);
                }
            });
            j.addAction(Actions.sequence(Actions.scaleTo(0, 0),Actions.scaleTo(1, 1,0.25f),run));
        }
        for (Ships j: EnemyShips.arrShips) {
            RunnableAction run = new RunnableAction();
            final Ships i = j;
            //thuyền xuất hiện
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    i.Attack(new Point(i.getX() + i.getWidth()/2,i.getY() + i.getHeight()/2), i.getDelay(), background);
                }
            });
            j.addAction(Actions.sequence(Actions.scaleTo(0, 0),Actions.scaleTo(-1, -1,0.25f),run));
        }
    }
    public static void MyDead(Ships ship) {
        MyShips.removeShip(ship);
        if (MyShips.arrShips.length == 0) 
            game.setScreen(new LoseScreen(game));
    }
    public static void EnemyDead(Ships ship) {
        EnemyShips.removeShip(ship);
        if (EnemyShips.arrShips.length == 0) 
            game.setScreen(new WinScreen(game));
    }
    public static Ships getShipsAttackTo (boolean clash) {
        Random rd = new Random();
        if (clash) {
            if (EnemyShips.arrShips.length == 0)
                return null;
            return EnemyShips.arrShips[rd.nextInt(EnemyShips.arrShips.length)];
        }
        else {
            if (MyShips.arrShips.length == 0)
                return null;
            return MyShips.arrShips[rd.nextInt(MyShips.arrShips.length)];
        }
    }
    private void BackIn() {
        statusReroll = false;
        if (shipsPick[0] != null)
            shipsPick[0].remove();
        if (shipsPick[1] != null)
            shipsPick[1].remove();
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                final int RD1 = new Random().nextInt(3);
                final int RD2 = new Random().nextInt(3);
                if (RD1 == 0) {
                    addGreenShipsPicking(0);
                }
                else {
                    if (RD1 == 1) {
                        addRedShipsPicking(0);
                    }
                    else {
                        addBlueShipsPicking(0);
                    }
                }
                if (RD2 == 0) {
                    addGreenShipsPicking(1);
                }
                else {
                    if (RD2 == 1) {
                        addRedShipsPicking(1);
                    }
                    else {
                        addBlueShipsPicking(1);
                    }
                }
                shipsPick[0].addAction(Actions.moveTo(shipsPick[0].getX() + btnReRollin.getX() - btnReRollout.getX(), btnReRollin.getY() + shipsPick[0].getHeight() / 1.25f,0.3f));
                shipsPick[1].addAction(Actions.moveTo(shipsPick[1].getX() + btnReRollin.getX() - btnReRollout.getX(), btnReRollin.getY() + shipsPick[0].getHeight() / 1.25f,0.3f));
            }
        });
        RunnableAction run1 = new RunnableAction();
        run1.setRunnable(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                statusReroll = true;
            }
            
        });
        Back.addAction(Actions.sequence(Actions.moveTo(btnReRollout.getX(), btnReRollout.getY(), 0.3f),run,Actions.moveTo(btnReRollin.getX(),btnReRollin.getY() , 0.3f),run1));
    }
    private void BackOut() {
        Back.moveToOnTimeRemove(new Point(btnReRollout.getX(), Back.getY()), 0.3f);
    }
    private void pickingShip(float x , float y) throws SQLException {
        int num = waitingShips();
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                waitPick = false;
            }
        });
        if (!waitPick) {
            if (shipsPick[0] != null && shipsPick[0].Contains(x, y) && num != -1 ) {
                shipsPick[0].addAction(Actions.sequence(Actions.moveTo(mapPoint[3][num].getX() - shipsPick[0].getWidth()/2,mapPoint[3][num].getY() - shipsPick[0].getHeight()/2, 0.2f),run));
                timePick ++;
                if (upLevel(shipsPick[0],0) == false) shipsWaiting.add(shipsPick[0]);
                else shipsPick[0].remove();
                shipsPick[0] = null;
                changeLabel(lbltimepick, (TIMETOPICK - timePick)+""); 
                if (shipsWaiting.size() == 7)
                    setUpFight();
            }
            else {
                if (shipsPick[1] != null && shipsPick[1].Contains(x, y) && num != -1) {
                    shipsPick[1].addAction(Actions.sequence(Actions.moveTo(mapPoint[3][num].getX() - shipsPick[1].getWidth()/2,mapPoint[3][num].getY() - shipsPick[1].getHeight()/2, 0.2f),run));
                    timePick ++;
                    if (upLevel(shipsPick[1],0) == false) 
                        shipsWaiting.add(shipsPick[1]);
                    else
                        shipsPick[1].remove();
                    shipsPick[1] = null;
                    changeLabel(lbltimepick, (TIMETOPICK - timePick)+""); 
                    if (shipsWaiting.size() == 7)
                        setUpFight();
                }
            }
        }
        if (timePick == TIMETOPICK) {
            setUpFight();
        }
    }
    private void setUpFight() {
        BackOut();
        lbltimepick.remove();
        setUp = true;
        if (shipsPick[0] != null)
            shipsPick[0].remove();
        if (shipsPick[1] != null)
            shipsPick[1].remove();
        buttonReRoll.remove();
    }
    private boolean upLevel(Ships ship, int count) {
        if (shipsWaiting.size() > 0) {
            for (Ships i : shipsWaiting) {
                if (i.getType() == ship.getType() && i.getLevel() == ship.getLevel())
                    count++;
            }
            if (count == 2) {
                Point pos = null;
                Ships save = null;
                for (Ships i : shipsWaiting) {
                    if (i.getType() == ship.getType() && i.getLevel() == ship.getLevel()) {
                        i.upLevel();
                        save = i;
                        break;
                    }
                }
                for (int i = 0; i < shipsWaiting.size(); i++) {
                    if (shipsWaiting.get(i).getType() == ship.getType() && shipsWaiting.get(i).getLevel() == ship.getLevel()) {
                        System.out.println(shipsWaiting.get(i).getLevel());
                        if (pos != null)
                            shipsWaiting.get(i).moveToOnSpeed(pos,0);
                        pos = new Point(shipsWaiting.get(i).getX(),shipsWaiting.get(i).getY());
                        shipsWaiting.get(i).remove();
                        shipsWaiting.remove(i);
                        i--;
                    }
                    else {
                        Point temp = new Point(shipsWaiting.get(i).getX(),shipsWaiting.get(i).getY());
                        if (pos != null) {
                            shipsWaiting.get(i).moveToOnTime(pos, 0.3f);
                            pos = temp;
                        }
                    }
                }
                if (save != null)
                    upLevel(save, -1);
                return true;
            }
        }
        sortShip();
        return false;
    }
    private void sortShip() {
        for (int i = 0; i < shipsWaiting.size(); i++) {
                if (!shipsWaiting.get(i).Contains(mapPoint[3][i].getX(),mapPoint[3][i].getY())) {
                    Point p = new Point(mapPoint[3][i].getX() - shipsWaiting.get(i).getWidth()/2,shipsWaiting.get(i).getY());
                    shipsWaiting.get(i).moveToOnTime(p, 0.3f);
                }
        }
    }
    private int waitingShips() {
        if (shipsWaiting.size() > 7)
            return -1;
        return shipsWaiting.size();
    }
    private void enemySetUp() {
        ArrayList<Point> pointA = new ArrayList<Point>();
        for (Ships i : MyShips.arrShips) {
            boolean check = false;
            int rd = new Random().nextInt(3);
            int rdX = new Random().nextInt(3) + 4;
            int rdY = new Random().nextInt(7);
            while (!check) {
                check = true;
                for (int j = 0; j < pointA.size(); j++)  {
                    if (pointA.get(j).getX() == rdX && pointA.get(j).getY() == rdY || (rdX == 6 && rdY == 6)) {
                        check = false;
                        rdX = new Random().nextInt(3) + 4;
                        rdY = new Random().nextInt(7);
                        break;
                    }
                }
                if (check)
                    pointA.add(new Point(rdX,rdY));
            }
            if (rd == 0) {
                addGreenShips(rdX, rdY, i.getLevel(), false, 3);
            }
            else {
                if (rd == 1) {
                    addRedShips(rdX, rdY, i.getLevel(), false, 3);
                }
                else {
                    addBlueShips(rdX, rdY, i.getLevel(), false, 3);
                }
            }
        }
    }
    private void drawline() {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setColor(Color.WHITE);
        sr.begin(ShapeType.Line);
        for (int i = 0; i < mapBattle.length; i++) {
            sr.line(mapBattle[i][0].getxMin(),mapBattle[i][0].getyMin(),stage.getWidth(),mapBattle[i][0].getyMin());
        }
        for (int i = 0; i < mapBattle.length; i++) {
            sr.line(mapBattle[0][i].getxMin(),mapBattle[0][1].getyMin(),mapBattle[0][i].getxMin(),stage.getHeight());
        }
        sr.end();
        sr.begin(ShapeType.Line);
    }
    private void changeLabel (Label lbl, String data) {
        lbl.setText(data);
    }
    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        for (Ships i: MyShips.arrShips) {
            if (i.getAnimateBoom()) {
                batch.begin();
                i.setTimeKeyFrame(i.getTimeKeyFrame() + Gdx.graphics.getDeltaTime());
                if (i.getTimeKeyFrame() > Asset.explosionAnim.getAnimationDuration()) {//kết thúc vụ nổ
                    i.setTimeKeyFrame(0);
                    i.setAnimateBoom(false);
                }
                batch.draw(Asset.explosionAnim.getKeyFrame(i.getTimeKeyFrame()), i.getX(), i.getY(), i.getWidth(), i.getWidth());
                batch.end();
            }
        }
        for (Ships i: EnemyShips.arrShips) {
            if (i.getAnimateBoom()) {
                batch.begin();
                i.setTimeKeyFrame(i.getTimeKeyFrame() + Gdx.graphics.getDeltaTime());
                if (i.getTimeKeyFrame() > Asset.explosionAnim.getAnimationDuration()) {
                    i.setTimeKeyFrame(0);
                    i.setAnimateBoom(false);
                }
                batch.draw(Asset.explosionAnim.getKeyFrame(i.getTimeKeyFrame()), i.getX(), i.getY() - i.getHeight() / 2, i.getWidth(), i.getWidth());
                batch.end();
            }
        }
        if (ship != null && touched) {
            background.act(Gdx.graphics.getDeltaTime());
            Point position = new Point(ship.getX()+ship.getWidth()/2,ship.getY()+ship.getHeight()/2);
            if (position.getX() < 0 || position.getX() > (MapBattle.sizeWidth*(int)MapBattle.NUMCOL) || position.getY() < 0 || position.getY() > (MapBattle.sizeHeight*(int)(MapBattle.NUMROW/2))) {
                another.remove();
                savePos = oldPos;
            }
            else 
                //di chuyển thuyền hiện placeholder
                for (int i = 0; i < mapPoint.length; i++) {
                    float yy = MapBattle.sizeHeight*i;
                    float yyy = MapBattle.sizeHeight*(i+1);
                    for(int j = 0; j < mapPoint[0].length; j++) {
                        float xx = MapBattle.sizeWidth*(j);
                        float xxx = MapBattle.sizeWidth*(j+1);
                        if (position.getX() > xx && position.getX() <= xxx) {
                            if (position.getY() > yy && position.getY() <= yyy) {
                                Point temp = new Point((xx+xxx)/2,(yy+yyy)/2);
                                if (savePos.getX() != temp.getX() || savePos.getY() != temp.getY()) {
                                    savePos = temp;
                                    another.remove();
                                    another = new MyActor("dante.png", savePos, new Size(100, 100));
                                    background.addActor(another);
                                    another.setZIndex(1);
                                }
                            }
                        }
                    }
                }
            if (touched) 
                drawline();
		}
    }
    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        stage.dispose();
    }
    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        if (!fighting) {
            if (touched == false) {
                if (setUp) {
                    for (Ships ships:MyShips.arrShips)
                        if (ships.Contains(screenX, stage.getHeight() - screenY)) {
                            touched = true;
                            ship = ships;
                            ships.setTouched(true);
                            float x = ships.getX();
                            float y = ships.getY();
                            oldPos = new Point(x + ships.getWidth()/2, y + ships.getHeight()/2);
                            savePos = oldPos;
                            savePosWait = oldPos;
                            System.out.println(x);
                            System.out.println(y);
                            break;
                        }   
                    System.out.println(shipsWaiting.size());
                    for (Ships ships:shipsWaiting)
                        if (ships.Contains(screenX, stage.getHeight() - screenY)) {
                            touched = true;
                            ship = ships;
                            ships.setTouched(true);
                            float x = ships.getX();
                            float y = ships.getY();
                            oldPos = new Point(x + ships.getWidth()/2, y + ships.getHeight()/2);
                            savePos = oldPos;
                            savePosWait = oldPos;
                            System.out.println(x);
                            System.out.println(y);
                            break;
                        }
                }
            }
            buttonReRoll.Contains(screenX, stage.getHeight() - screenY);
            try {
                pickingShip(screenX, stage.getHeight() - screenY);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
		touched = false;
        if (!fighting) {
            if (ship != null) {//đặt thuyền vào vị trí
                Size size = new Size(ship.getWidth(), ship.getHeight());
                ship.setPosition(savePos,size);
                boolean check = true;
                for (Ships i : MyShips.arrShips) {
                    if (i.normalContain(ship.getX(),ship.getY()) && i.getStatus()) {
                        check = false;
                        if (!ship.getStatus() || ship != i)
                            ship.setPosition(savePosWait,size);
                    }
                }
                if (savePos != oldPos && !ship.getStatus() && check) {
                    ship.setStatus(true);
                    shipsWaiting.remove(ship);
                    MyShips.addShip(ship);
                    sortShip();
                    if (shipsWaiting.size() == 0) {
                        battle();
                    }
                }
                // if (ship.getStatus())
                //     ship.set
                ship = new Ships();
                another.remove();
            }
            if (buttonReRoll.getTouch() && buttonReRoll.Contains(screenX, stage.getHeight() - screenY) && statusReroll) {
                ++timePick;
                if (timePick == TIMETOPICK) 
                    setUpFight();
                else { 
                    changeLabel(lbltimepick, (TIMETOPICK - timePick)+"");
                    BackIn();//đã chọn reroll
                }
            }
        }
        if (btnHome.Contains(screenX, stage.getHeight() - screenY))
            game.setScreen(new MainMenuScreen(game));
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        if (touched) {//di chuyển thuyền
			ship.setPosition(screenX - ship.getWidth()/2, stage.getHeight() - ship.getHeight()/2 - screenY);
		}
		return true;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        removeLblAttribute();
        for (Ships i : shipsPick) 
            if (i != null && i.Contains(screenX, stage.getHeight() - screenY))
                addLblAttributePickAndWait(i);
        for (Ships i : shipsWaiting)
            if (i != null && i.Contains(screenX, stage.getHeight() - screenY))
                addLblAttributePickAndWait(i);
        for (Ships i : MyShips.arrShips)
            if (i != null && i.Contains(screenX, stage.getHeight() - screenY)) {
                if (!fighting)
                    addLblAttributePickAndWait(i); 
                else 
                    addLblAttributeFight(i);
            }
        for (Ships i : EnemyShips.arrShips)
            if (i != null && i.Contains(screenX, stage.getHeight() - screenY)) {
                if (!fighting)
                    addLblAttributePickAndWait(i); 
                else 
                    addLblAttributeFight(i);
            }
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void animateStarting() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void setBackground() {
        // TODO Auto-generated method stub
        batch = new SpriteBatch();
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image img = new Image(Asset.backGround);
		img.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		background.addActor(img); // your background image here.
        stage.addActor(background);
    }
    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        //thêm btn home
        Size btnHomeSize = new Size(75,75);
        btnHome = new Button("btnhome.png",new Point(stage.getWidth() - btnHomeSize.getWidth()/2 - 20, stage.getHeight() - btnHomeSize.getHeight()/2 - 20),btnHomeSize);
        background.addActor(btnHome);
        //bảng chọn
        Back.setPosition(-Back.getWidth(),stage.getHeight()/1.5f);
        Back.setSize(mapBattle[0][0].getHeight()*2,mapBattle[0][0].getHeight());
        btnReRollout = new Point(Back.getX(),Back.getY());
        btnReRollin = new Point(0,Back.getY());
        //chữ thời gian
        lbltimepick = new Label(TIMETOPICK+"",new Label.LabelStyle(Asset.font,Color.RED));
        lbltimepick.setPosition(10, 700);
        lbltimepick.setFontScale(5);
        background.addActor(Back);
        background.addActor(lbltimepick);
        background.addActor(buttonReRoll);
    }
    
}
