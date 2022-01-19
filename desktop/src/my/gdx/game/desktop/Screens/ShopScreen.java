package my.gdx.game.desktop.Screens;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Actor.Captain;
import my.gdx.game.desktop.Actor.CaptainManagement;
import my.gdx.game.desktop.Actor.MyActor;
import my.gdx.game.desktop.Utils.*;

public class ShopScreen implements ScreenInterface,Screen{
    private Game game;
    private Stage stage;
    private Group backGround;
    private Button btnHome;
    private Label lblShop;
    private Label lblGold;
    private Size btnSize;
    private BitmapFont font;
    private SpriteBatch batch;
    private boolean start = false;
    private ArrayList<Point> arPos = new ArrayList<>();
    private ArrayList<Group> backList = new ArrayList<>();
    private Group backTemp;
    public ShopScreen(Game g) {
        game = g;
        font = Asset.font;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        try {
            Asset.getOwnerData();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        setBackground();
        setItem();
        animateStarting();
        listenerManager();
    }
    private void renderAnimate(MyActor actor,int pos,Animation<AtlasRegion> animate) {
        actor.setTimeKeyFrame(actor.getTimeKeyFrame() + Gdx.graphics.getDeltaTime());
        // if (actor.getTimeKeyFrame() > animate.getAnimationDuration())
        //             actor.setTimeKeyFrame(0);
        batch.draw(animate.getKeyFrame(actor.getTimeKeyFrame(),true), arPos.get(pos).getX(), arPos.get(pos).getY(), actor.getWidth(), actor.getWidth());
    }
    private void addBtnBuy(final Group BACKCAPTAIN, final Label LBLPRICE, final Captain i) {
        Size sizeBtnBuy = new Size(BACKCAPTAIN.getWidth()*0.5f,BACKCAPTAIN.getWidth()*0.5f/(1572/692));
        final Button BTNBUY = new Button("btnbuy.png", new Point(BACKCAPTAIN.getWidth()/2, BACKCAPTAIN.getHeight()/10), sizeBtnBuy);
        BACKCAPTAIN.addActor(BTNBUY);
        final Captain TEMP = i;
        BTNBUY.removeListener(BTNBUY.getListeners().get(0));
        BTNBUY.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                BTNBUY.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (BTNBUY.Contains(BTNBUY.getX() + x, BTNBUY.getY() + y) && Asset.gold >= TEMP.getPrice()) {
                    //thêm bảng sở hữu
                    LocalDateTime now = LocalDateTime.now();  
                    String query = "INSERT INTO ownerCaptain VALUES("+Asset.id+",'"+TEMP.getId()+"','"+DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(now)+"',"+0+")";
                    try {
                        Asset.setData(query);
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    LBLPRICE.setText("BOUGHT!");
                    //trừ tiền
                    try {
                        StaticSupport.goldUp(-TEMP.getPrice());
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    lblGold.setText("GOLD: "+Asset.gold);
                    if (backTemp != null) {
                        System.out.println(backTemp.getChildren().size);
                        backTemp.removeActor(backTemp.getChild(backTemp.getChildren().size - 1));
                        addBtnUse(backTemp, TEMP);
                    }
                    addLblUsed(BACKCAPTAIN,i);
                    BTNBUY.remove();
                }
            BTNBUY.addAction(Actions.scaleTo(1, 1));
            }
        });
    }
    private void addLblUsed(Group BACKCAPTAIN, Captain TEMP) {
        final Label LBLUSED = new Label("USING", new LabelStyle(font, Color.GOLD));
        LBLUSED.setPosition(BACKCAPTAIN.getWidth()/2 - 30, BACKCAPTAIN.getHeight()/12);
        BACKCAPTAIN.addActorAt(BACKCAPTAIN.getChildren().size,LBLUSED);
        backTemp = BACKCAPTAIN;
        //udate sở hữu
        String query = "UPDATE ownerCaptain SET statusUse = "+0+" WHERE idCaptain = '"+CaptainManagement.captainChooseID+"' AND idPlayer = "+Asset.id;
        String query1 = "UPDATE ownerCaptain SET statusUse = "+1+" WHERE idCaptain = '"+TEMP.getId()+"' AND idPlayer = "+Asset.id;
        try {
            if (CaptainManagement.captainChooseID != null)
                Asset.setData(query);
            Asset.setData(query1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CaptainManagement.captainChooseID = TEMP.getId();
    }
    private void addBtnUse(final Group BACKCAPTAIN, Captain i) {
        Size sizeBtnBuy = new Size(BACKCAPTAIN.getWidth()*0.5f,BACKCAPTAIN.getWidth()*0.5f/(1572/692));
        final Button BTNUSE = new Button("btnUse.png", new Point(BACKCAPTAIN.getWidth()/2, BACKCAPTAIN.getHeight()/10), sizeBtnBuy);
        BACKCAPTAIN.addActor(BTNUSE);
        final Captain TEMP = i;
        BTNUSE.removeListener(BTNUSE.getListeners().get(0));
        BTNUSE.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                BTNUSE.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (BTNUSE.Contains(BTNUSE.getX() + x, BTNUSE.getY() + y)) {
                    if (backTemp != null) {
                        backTemp.removeActor(backTemp.getChild(backTemp.getChildren().size - 1));
                        addBtnUse(backTemp, TEMP);
                    }
                    BTNUSE.remove();
                    addLblUsed(BACKCAPTAIN,TEMP);
                    backTemp = BACKCAPTAIN;
                }
                BTNUSE.addAction(Actions.scaleTo(1, 1));
            }
        });
    }
    private void listenerManager() {
        //thao tác chạm
        btnHome.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnHome.Contains(btnHome.getX()+x, btnHome.getY()+y))
                    game.setScreen(new MainMenuScreen(game));
            }
        });
    }
    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if (start) {
            batch.begin();
            renderAnimate(CaptainManagement.arrCaptain[0], 0, Asset.captainGreenAnim);
            renderAnimate(CaptainManagement.arrCaptain[1], 1, Asset.captainRedAnim);
            renderAnimate(CaptainManagement.arrCaptain[2], 2, Asset.captainBlueAnim);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
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
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        stage.dispose();
    }

    @Override
    public void animateStarting() {
        // TODO Auto-generated method stub
        //đưa số vàng vào
        lblGold.addAction(Actions.sequence(Actions.moveTo(50,lblGold.getY(),0.2f)));
        for (int i = 0; i < backList.size(); i++) {
            float posY = stage.getHeight() - 100 - (30 + (stage.getWidth()/1.3f - 40)) * ((i)/3 + 1);
            final Group TEMP = backList.get(i);
            TEMP.addAction(Actions.sequence(Actions.delay(0.1f * i), Actions.moveTo(TEMP.getX(), posY, 0.25f)));
        }
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                start = true;
                for (Group back : backList) 
                    back.removeActor(back.getChild(1));
            }
        });
        stage.addAction(Actions.sequence(Actions.delay(backList.size() * 0.1f + 0.35f),run));
    }

    @Override
    public void setBackground() {
        // TODO Auto-generated method stub
        backGround = new Group();
        backGround.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backGround);
        Image img = new Image(Asset.backGround);
        img.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        backGround.addActor(img);
    }

    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        //set backtemp
        //btnhome
        btnSize = new Size(75,75);
        btnHome = new Button("btnhome.png",new Point(stage.getWidth() - btnSize.getWidth()/2 - 20, stage.getHeight() - btnSize.getHeight()/2 - 20),btnSize);
        backGround.addActor(btnHome);
        //lbl shop
        lblShop = new Label("SHOP",new LabelStyle(font,Color.ORANGE));
        lblShop.setPosition(stage.getWidth()/2 - 60, stage.getHeight()*0.9f, Align.center);
        lblShop.setFontScale(4);
        backGround.addActor(lblShop);
        //lbl Gold
        lblGold = new Label("GOLD: "+Asset.gold,new Label.LabelStyle(font,Color.RED));
        lblGold.setPosition(-250, stage.getHeight()*0.9f);
        backGround.addActor(lblGold);
        //set thành phần shop
        int count = 1;
        for (Captain i : CaptainManagement.arrCaptain) {
            final Group BACKCAPTAIN = new Group();
            Image img = new Image(Asset.backShop);
            //set vị trí back
            float posX;
            float posY;
            if (count % 3 == 0) 
                posX = stage.getWidth()-30 - stage.getWidth()/3 + 40;
            else 
                if (count % 2 == 0)
                    posX = stage.getWidth()/2 - stage.getWidth()/6 + 20;
                else 
                    posX = 30;
            posY = stage.getHeight() + stage.getWidth()/1.5f - 40;
            BACKCAPTAIN.setBounds(posX, posY, stage.getWidth()/3 - 40, stage.getWidth()/1.5f - 40);
            img.setBounds(0, 0, BACKCAPTAIN.getWidth(), BACKCAPTAIN.getHeight());
            BACKCAPTAIN.addActor(img);
            stage.addActor(BACKCAPTAIN);
            //set vị trí kích thước thuyền trưởng
            i.setWidth(img.getWidth());
            i.setHeight((img.getWidth()));
            i.setPosition(0, img.getHeight() - i.getHeight());
            BACKCAPTAIN.addActor(i);
            //cập nhật vị trí hoạt ảnh
            posY = stage.getHeight() - 100 - (30 + (stage.getWidth()/1.3f - 40)) * ((count - 1)/3 + 1);
            arPos.add(new Point(i.getX()+BACKCAPTAIN.getX(),i.getY()+posY));
            //thêm tên
            String n = i.getTextureName().replace("_0", "");
            n = n.replace("tain", "tain ");
            Label name = new Label(n, new LabelStyle(font, Color.RED));
            name.setPosition(30, BACKCAPTAIN.getHeight()/2 - 30);
            BACKCAPTAIN.addActor(name);
            //thêm label chú thích 
            Label comment = new Label(i.getAttribute(), new LabelStyle(font, Color.CYAN));
            comment.setPosition(20, BACKCAPTAIN.getHeight()/3);
            comment.setWrap(true);
            comment.setWidth(BACKCAPTAIN.getWidth() * 0.75f);
            comment.setFontScale(0.6f);
            BACKCAPTAIN.addActor(comment);
            //thêm label giá tiền
            final Label LBLPRICE = new Label("Price: "+i.getPrice(),new LabelStyle(font, Color.YELLOW));
            LBLPRICE.setPosition(20, BACKCAPTAIN.getHeight()/4 - 30);
            BACKCAPTAIN.addActor(LBLPRICE);
            //kiểm tra tồn tại
            boolean checkExist = false;
            for (String j : CaptainManagement.arrOwnCaptainID)
                if (i.getId().equals(j))
                    checkExist = true;
            //thêm button mua vào
            if (!checkExist) {
                addBtnBuy(BACKCAPTAIN, LBLPRICE, i);
           }
            else {
                LBLPRICE.setText("BOUGHT!");
                if (i.getId().equals(CaptainManagement.captainChooseID)) {
                    addLblUsed(BACKCAPTAIN,i);

                }
                else {
                    addBtnUse(BACKCAPTAIN, i);
                }
            }
            count ++;
            backList.add(BACKCAPTAIN);
        }
    }
}
