package my.gdx.game.desktop.Screens;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;
import my.gdx.game.desktop.Utils.StaticSupport;

public class LoginScreen implements Screen,ScreenInterface{
    private Group backGround;
    private Group backLogin;
    private Game game;
    private Stage stage;
    private Button btnLogin;
    private Button btnRegis;
    private TextField txtUserName;
    private TextField txtPassWord;
    private Label lblUserName;
    private Label lblPassWord;
    private BitmapFont font;
    private Label lblError;

    public LoginScreen(Game g) {
        game = g;
        stage = new Stage(new ScreenViewport());
        font = Asset.font;
        setBackground();
        setItem();
        stage.addActor(backGround);
        Gdx.input.setInputProcessor(stage);
        //label.setPosition(10,70);
    }
    private void listenerManager() {
        btnRegis.removeListener(btnRegis.getListeners().get(0));
        btnLogin.removeListener(btnLogin.getListeners().get(0));
        btnLogin.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnLogin.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnLogin.Contains(btnLogin.getX() + x, btnLogin.getY() + y)) {
                    backLogin.removeActor(lblError);
                    String usrname = txtUserName.getText().toString();
                    String passwd = txtPassWord.getText().toString();
                    String query ="SELECT * FROM Player WHERE userName = '"+usrname+"'";
                    ResultSet rs = null;
                    String realPass = new String();
                    int idP = -1;
                    try {
                        rs = Asset.getData(query);
                        if (rs.next()) {
                            realPass = rs.getString("passWordP");  
                            idP = rs.getInt("id");

                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (passwd.equals(realPass) && !StaticSupport.checkSpace(passwd) && !StaticSupport.checkSpace(usrname)) {
                        try {
                            entryGame(idP);
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else {
                        lblError = new Label("Username or Password invalid!", new LabelStyle(font, Color.WHITE));
                        lblError.setPosition(backLogin.getWidth()/2 - 200, backLogin.getHeight() * 0.8f);
                        backLogin.addActor(lblError);
                    }
                }
                btnLogin.addAction(Actions.scaleTo(1, 1));
            }
        });
        btnRegis.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnRegis.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                if (btnRegis.Contains(btnRegis.getX() + x, btnRegis.getY() + y)) {
                    backLogin.removeActor(lblError);
                    String usrname = txtUserName.getText().toString();
                    String passwd = txtPassWord.getText().toString();
                    String query = "SELECT * FROM Player WHERE userName = '"+usrname+"'";
                    ResultSet rs = null;
                    try {
                        if (StaticSupport.checkSpace(usrname) || StaticSupport.checkSpace(passwd)) {
                            lblError = new Label("Wrong format", new LabelStyle(font, Color.WHITE));
                            lblError.setPosition(backLogin.getWidth()/2 - 50, backLogin.getHeight() * 0.8f);
                            backLogin.addActor(lblError);
                        }
                        else {
                            rs = Asset.getData(query);
                            if (rs.next()) {
                                lblError = new Label("Username have existed", new LabelStyle(font, Color.WHITE));
                                lblError.setPosition(backLogin.getWidth()/2 - 100, backLogin.getHeight() * 0.8f);
                                backLogin.addActor(lblError);
                            }
                            else {
                                String queryGetMax = "SELECT DISTINCT MAX(id) MAXI FROM Player";
                                try {
                                    rs = Asset.getData(queryGetMax);
                                    int maximumID;
                                    if (rs.next()) {
                                        maximumID = rs.getInt("MAXI")+1;
                                        String queryInsert = "INSERT INTO Player VALUES("+maximumID+",'"+usrname+"',"+"'"+passwd+"',0,0)";
                                        Asset.setData(queryInsert);
                                    }
                                    else {
                                        String queryInsert = "INSERT INTO Player VALUES("+0+",'"+usrname+"',"+"'"+passwd+"',0)";
                                        maximumID = 0;
                                        try {
                                            Asset.setData(queryInsert);
                                        } catch (SQLException e2) {
                                            // TODO Auto-generated catch block
                                        }
                                    }
                                    entryGame(maximumID);
                                } catch (SQLException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                            }
                            
                            rs.refreshRow();
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        
                        e.printStackTrace();
                    }
                } 
                btnRegis.addAction(Actions.scaleTo(1, 1));
            }
        });
    }
    public void entryGame(int idP) throws SQLException {
        Asset.setId(idP);
        Asset.loadDatabase();
        game.setScreen(new MainMenuScreen(game));
    }
    @Override
    public void render(float delta) {
        stage.act();
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
    public void animateStarting() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBackground() {
        // TODO Auto-generated method stub
        backGround = new Group();
        backGround.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image img = new Image(Asset.backGround);
        img.setBounds(0,0,stage.getWidth(),stage.getHeight());
        backGround.addActor(img);
    }

    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        backLogin = new Group();
        Image img = new Image(Asset.loadtexture("back.png"));
        Size backSize = new Size(stage.getWidth()*0.75f,stage.getWidth()*0.75f);
        backLogin.setBounds(stage.getWidth()/2 - backSize.getWidth()/2, stage.getHeight()/2 - backSize.getHeight()/2, backSize.getWidth(), backSize.getHeight());
        img.setBounds(0,0,backLogin.getWidth(),backLogin.getHeight());
        backGround.addActor(backLogin);
        backLogin.addActor(img);
        //thêm textfield
        Size txtfieldSize = new Size(backLogin.getWidth()*0.6f,backLogin.getWidth()*0.6f/8);
        Skin sk = new Skin(Gdx.files.internal("uiskin.json"));
        txtUserName = new TextField("",sk);
        txtUserName.setSize(txtfieldSize.getWidth(),txtfieldSize.getHeight());
        txtUserName.setPosition(backLogin.getWidth() - txtUserName.getWidth() - 50, backLogin.getHeight() - txtUserName.getHeight()*5);
        backLogin.addActor(txtUserName);
        
        txtPassWord = new TextField("", sk);
        txtPassWord.setSize(txtfieldSize.getWidth(),txtfieldSize.getHeight());
        txtPassWord.setPosition(backLogin.getWidth() - txtPassWord.getWidth() - 50, backLogin.getHeight() - txtPassWord.getHeight()*7);
        backLogin.addActor(txtPassWord);
        //thêm label chú thích
        lblUserName = new Label("USERNAME", new LabelStyle(font, Color.YELLOW));
        lblUserName.setPosition(50, txtUserName.getY()+10);
        backLogin.addActor(lblUserName);

        lblPassWord = new Label("PASSWORD", new LabelStyle(font, Color.YELLOW));
        lblPassWord.setPosition(50, txtPassWord.getY() + 10);
        backLogin.addActor(lblPassWord);

        //thêm button
        Size btnSize = new Size(backLogin.getWidth()/4,backLogin.getWidth()/4*(692/1571f));
        float posY = txtPassWord.getY() - 70;

        btnLogin = new Button("btnlogin.png", new Point(backLogin.getWidth()/4,posY), btnSize);
        backLogin.addActor(btnLogin);

        btnRegis = new Button("btnregis.png", new Point(backLogin.getWidth()*3/4,posY), btnSize);
        backLogin.addActor(btnRegis);
        listenerManager();
    }
}