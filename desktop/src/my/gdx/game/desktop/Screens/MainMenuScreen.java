package my.gdx.game.desktop.Screens;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Actor.MyActor;
import my.gdx.game.desktop.User.UserManagement;
import my.gdx.game.desktop.Utils.Asset;
public class MainMenuScreen implements Screen,ScreenInterface {
    private Stage stage;
    private Game game;
    private Group backGround;
    private Button btnPlay;
    private Button btnShop;
    private Button btnRank;
    private Button btnSetting;
    private Button btnChange;
    private Size btnSize;
    private final float RATIO = 697f / 706;
    private BitmapFont font;
    private Label lblGold;
    private Label lblWin;
    private Label lblUserName;
    private MyActor gameName;
    public MainMenuScreen(Game g) {
        stage = new Stage(new ScreenViewport());
        game = g;
        font = Asset.font;
        Gdx.input.setInputProcessor(stage);
        setBackground();
        setItem();
        animateStarting();
    }
    private void listenerManager() {
        //thao tác chạm
        btnPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnPlay.Contains(btnPlay.getX()+x, btnPlay.getY()+y))
                    game.setScreen(new GameScreen(game));
            }
        });
        btnShop.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnShop.Contains(btnShop.getX()+x, btnShop.getY()+y))
                    game.setScreen(new ShopScreen(game));
            }
        });
        btnRank.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnRank.Contains(btnRank.getX()+x, btnRank.getY()+y))
                    game.setScreen(new RankScreen(game));
            }
        });
        btnChange.clearListeners();
        btnChange.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnChange.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnChange.Contains(btnChange.getX() + x, btnChange.getY() + y))
                    addBoardChange();
                    btnChange.addAction(Actions.scaleTo(1, 1));
            }
        });
        btnSetting.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnSetting.Contains(btnSetting.getX()+x, btnSetting.getY()+y))
                    game.setScreen(new ManagerScreen(game));
            }
        });
    }
    private void addBoardChange() {
        final MyActor board = new MyActor("back.png", new Point(stage.getWidth()/2, stage.getHeight() + 400), new Size(600,400));
        backGround.addActor(board);
        board.addAction(Actions.moveTo(board.getX(), stage.getHeight()/2-board.getHeight()/2,0.2f));

        final Label quest = new Label("Change UserName",new LabelStyle(Asset.font, Color.YELLOW));
        quest.setPosition(board.getX() + 140,board.getY() + 350);
        backGround.addActor(quest);
        quest.addAction(Actions.moveTo(quest.getX(), stage.getHeight()/2 + 100,0.2f));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        final TextField txtUser = new TextField("", skin);
        txtUser.setTextFieldFilter(new TextFieldFilter() {
            public  boolean acceptChar(TextField textField, char c) {
                final char nonaccepted = ' ';
                if (c != nonaccepted)
                    return true;
                return false;
            }
        });
        Size txtSize = new Size(board.getWidth() * 3/4f, 30);
        txtUser.setSize(txtSize.getWidth(), txtSize.getHeight());
        txtUser.setPosition(stage.getWidth()/2 - txtSize.getWidth() / 2, quest.getY() - 50);
        txtUser.addAction(Actions.moveTo(txtUser.getX(), stage.getHeight()/2 + 50,0.2f));
        backGround.addActor(txtUser);

        final Button btnYes = new Button("btnYes.png",new Point(stage.getWidth() / 2 - 100,board.getY() - 150),new Size(80,80));
        backGround.addActor(btnYes);
        btnYes.addAction(Actions.moveTo(btnYes.getX(), stage.getHeight()/2-board.getHeight()/4,0.2f));

        final Button btnNo = new Button("btnNo.png",new Point(stage.getWidth()/2+ 100,board.getY() - 150),new Size(80,80));
        backGround.addActor(btnNo);
        btnNo.addAction(Actions.moveTo(btnNo.getX(), stage.getHeight()/2-board.getHeight()/4,0.2f));

        final Label lblExist = new Label("Existed", new LabelStyle(Asset.font,Color.RED));
        lblExist.setPosition(stage.getWidth()/2-50, stage.getHeight()/2);
        btnYes.clearListeners();
        btnYes.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnYes.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //lblExist.remove();
                if (btnYes.normalContain(btnYes.getX() + x, btnYes.getY() + y)) {
                    lblExist.remove();
                    try {
                        if (txtUser.getText() != "") {
                            String checkExist = "SELECT * FROM Player WHERE userName = '"+txtUser.getText()+"'";
                            ResultSet rsExist = Asset.getData(checkExist);
                            if (!rsExist.next()) {
                                String query = "UPDATE Player SET userName = '"+ txtUser.getText()+"' WHERE id = "+Asset.id;
                                Asset.setData(query);
                                Asset.userName = txtUser.getText();
                                UserManagement.changeUserName(Asset.id, txtUser.getText());
                                lblUserName.setText("username: "+txtUser.getText());
                                board.remove();
                                quest.remove();
                                txtUser.remove();
                                btnYes.remove();
                                btnNo.remove();
                            }
                            else {
                                backGround.addActor(lblExist);
                            }
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                btnYes.addAction(Actions.scaleTo(1, 1));
            }
        });

        btnNo.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnNo.normalContain(btnNo.getX() + x, btnNo.getY() + y)) {
                    board.remove();
                    quest.remove();
                    txtUser.remove();
                    btnYes.remove();
                    btnNo.remove();
                    lblExist.remove();
                }
            }
        });
    }
    @Override
    public void animateStarting() {
        final float TIMEWAIT = 0.25f;
        //đưa button vào
        btnPlay.addAction(Actions.sequence(Actions.moveTo(btnPlay.getX(), stage.getHeight()/8,TIMEWAIT)));
        int count = 1;
        //đưa dòng tên game vào
        gameName.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(gameName.getX(),stage.getHeight()*0.5f,TIMEWAIT)));
        //đưa số vàng vào
        lblUserName.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(20,lblUserName.getY(),TIMEWAIT)));
        lblGold.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(20,lblGold.getY(),TIMEWAIT)));
        lblWin.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(20,lblWin.getY(),TIMEWAIT)));
    }
    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        // super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

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
    public void setBackground() {
        // TODO Auto-generated method stub
        backGround = new Group();
        backGround.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image img = new Image(Asset.backGround);
        stage.addActor(backGround);
        img.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        backGround.addActor(img);
    }
    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        //tiền
        lblGold = new Label("GOLD: "+Asset.gold,new Label.LabelStyle(font,Color.RED));
        lblGold.setPosition(-250, stage.getHeight()*0.9f);
        backGround.addActor(lblGold);

        lblWin = new Label("WIN: "+Asset.win,new Label.LabelStyle(font,Color.RED));
        lblWin.setPosition(-250, stage.getHeight()*0.85f);
        backGround.addActor(lblWin);

        lblUserName = new Label("username: "+Asset.userName, new Label.LabelStyle(font,Color.ORANGE));
        lblUserName.setPosition(-400, stage.getHeight()*0.95f);
        backGround.addActor(lblUserName);
        //tên game
        Size gameNameSize = new Size(stage.getWidth()/2, stage.getWidth()/2);
        gameName = new MyActor("gamename.png", new Point(stage.getWidth()/2, stage.getHeight() + gameNameSize.getHeight()), gameNameSize);
        backGround.addActor(gameName);
        //button
        //play
        btnSize = new Size(stage.getWidth() / 6, stage.getWidth() / 6 * RATIO);
        btnPlay = new Button("btnPlay.png",new Point(stage.getWidth() / 2, 0), btnSize);
        backGround.addActor(btnPlay);
        //shop
        btnSize = new Size(75,75);
        btnShop = new Button("btnShop.png",new Point(stage.getWidth() - btnSize.getWidth()/2 - 20, stage.getHeight() - btnSize.getHeight()/2 - 20),btnSize);
        backGround.addActor(btnShop);
        //rank
        btnRank = new Button("btnRank.png",new Point(stage.getWidth() - btnSize.getWidth()/2 - 20, stage.getHeight() - btnSize.getHeight()/2 - 100),btnSize);
        backGround.addActor(btnRank);
        //change
        btnChange = new Button("btnChange.png",new Point(stage.getWidth() - btnSize.getWidth()/2 - 20, stage.getHeight() - btnSize.getHeight()/2 - 180),btnSize);
        if (!Asset.userName.equals("admin")) 
            backGround.addActor(btnChange);
        //setting
        btnSetting = new Button("btnSetting.png",new Point(stage.getWidth() - btnSize.getWidth()/2 - 20, stage.getHeight() - btnSize.getHeight()/2 - 180),btnSize);
        if (Asset.userName.equals("admin")) 
            backGround.addActor(btnSetting);
        //thao tác chạm
        listenerManager();
    }

        
}
