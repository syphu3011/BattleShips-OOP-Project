package my.gdx.game.desktop.Screens;

import java.sql.SQLException;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.User.User;
import my.gdx.game.desktop.User.UserManagement;
import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;
import my.gdx.game.desktop.Utils.StaticSupport;

public class RankScreen implements ScreenInterface, Screen {
    private Stage stage;
    private Group backGround;
    private Label lblRanking;
    private Game game;
    private TextField lookUp;
    private Table table = new Table();
    private Table ttable = new Table(); 
    public RankScreen(Game g) {
        stage = new Stage(new ScreenViewport());
        this.game = g;
        Gdx.input.setInputProcessor(stage);
        setBackground();
        setItem();
    }
    private void addLblUserWin() {
        Label lblUser = new Label("UserName: "+Asset.userName,new LabelStyle(Asset.font, Color.RED));
        Label lblWin = new Label("WIN: " + Asset.win,new LabelStyle(Asset.font, Color.RED));
        lblUser.setFontScale(0.75f);
        lblWin.setFontScale(0.75f);
        lblUser.setPosition(20, stage.getHeight() - 30);
        lblWin.setPosition(20, stage.getHeight() - 60);
        backGround.addActor(lblUser);
        backGround.addActor(lblWin);
    }
    private void addBtnHome() {
        final Button btnHome;
        Size btnHomeSize = new Size(75,75);
        btnHome = new Button("btnhome.png",new Point(stage.getWidth() - btnHomeSize.getWidth()/2 - 20, stage.getHeight() - btnHomeSize.getHeight()/2 - 20),btnHomeSize);
        backGround.addActor(btnHome);

        btnHome.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnHome.normalContain(btnHome.getX() + x, btnHome.getY() + y))
                    game.setScreen(new MainMenuScreen(game));
            }
        });
    }
    private void addBtnLookUp() {
        Size sizeBtn = new Size(31,31);
        final Button btnLookUp = new Button("btnLookUp.png", new Point(lookUp.getX() + lookUp.getWidth() + sizeBtn.getWidth() + 10,lookUp.getY() + sizeBtn.getHeight() / 2), sizeBtn);
        backGround.addActor(btnLookUp);
        btnLookUp.removeListener(btnLookUp.getListeners().get(0));
        btnLookUp.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnLookUp.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnLookUp.normalContain(btnLookUp.getX() + x, btnLookUp.getY() + y))
                    try {
                        setRankLookUp(lookUp.getText());
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                btnLookUp.addAction(Actions.scaleTo(1, 1));
            }
        });
    }
    private void addTxtFldLookUp() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        lookUp = new TextField("", skin);
        Size sizeTxt = new Size(stage.getWidth()/4,30);
        lookUp.setSize(sizeTxt.getWidth(),sizeTxt.getHeight());
        lookUp.setPosition(stage.getWidth() / 2 - sizeTxt.getWidth()/2, lblRanking.getY() - 45);
        backGround.addActor(lookUp);
    }
    private void setRankAll() throws SQLException {
        Asset.getAllUser();
        setRank(UserManagement.arrUser);
    }
    private void setRankLookUp(String txt) throws SQLException {
        Asset.getAllUser();
        ArrayList<User> listLook = new ArrayList<>();
        int textInt = StaticSupport.isDigit(txt) ? Integer.parseInt(txt):0;
        if (UserManagement.arrUser.size() <= textInt && textInt >= 1)
            listLook.add(UserManagement.arrUser.get(UserManagement.arrUser.size() - 1));
        for (User i : UserManagement.arrUser) 
            if (i.getUserName().contains(txt) || i.getRank() == textInt)
                listLook.add(i);
        setRank(listLook);
    }
    private void setRank(ArrayList<User> arrUser) {
        backGround.removeActor(table);
        backGround.removeActor(ttable);
        table.clear();
        ttable.clear();
        table = new Table();
        table.setBounds(0, 0, backGround.getWidth() - 200 , backGround.getHeight() - 250);
        Image img = new Image(new Texture("board.png"));
        img.setBounds(0, 0, backGround.getWidth(), backGround.getHeight() - 100);
        Table table1 = new Table();
        table1.setBounds(0, 0, backGround.getWidth() - 200 , backGround.getHeight() - 250);
        Label lblrank = new Label("Rank",new LabelStyle(Asset.font,Color.ORANGE));
        Label lblname = new Label("UserName",new LabelStyle(Asset.font,Color.ORANGE));
        Label lblbattleWin = new Label("Win",new LabelStyle(Asset.font,Color.ORANGE));
        table.row().fill().expand();
        table.add(lblrank);
        table.add(lblname);
        table.add(lblbattleWin);
        for (int i = 0; i < arrUser.size();i++) {
            table.row().expand().fill();
            Color color = arrUser.get(i).getUserName().equals(Asset.userName) ? Color.RED : Color.WHITE;
            Label rankNum = new Label(arrUser.get(i).getRank() + "",new LabelStyle(Asset.font,color));
            Label name = new Label(arrUser.get(i).getUserName(),new LabelStyle(Asset.font,color));
            Label battleWin = new Label(arrUser.get(i).getBattleWin()+"",new LabelStyle(Asset.font,color));
            table.add(rankNum);
            table.add(name);
            table.add(battleWin);
        }
        if (arrUser.size() < 8)
        for (int i = 0; i < 8 - arrUser.size(); i++) {
            table.row().fill().expand();
            Label space = new Label("",new LabelStyle(Asset.font,Color.RED));
            table.add(space);
        }
        final ScrollPane scroller = new ScrollPane(table);
        ttable.add(scroller).width(stage.getWidth()).height(img.getHeight() - 200);
        ttable.setPosition(480, 390);
        table1.setPosition(100, 100);
        scroller.setScrollingDisabled(true, false);
        backGround.addActor(img);
        backGround.addActor(ttable);
        backGround.addActor(table1);
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
        
    }

    @Override
    public void animateStarting() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBackground() {
        // TODO Auto-generated method stub
        backGround = new Group();
        backGround.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backGround);
        Image img = new Image(Asset.backGround);
        img.setBounds(0, 0, backGround.getWidth(), backGround.getHeight());
        backGround.addActor(img);
    }

    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        lblRanking = new Label("LEADERBOARD", new LabelStyle(Asset.font,Color.RED));
        lblRanking.setPosition(backGround.getWidth() / 2 - 80, backGround.getHeight()-50);
        backGround.addActor(lblRanking);
        try {
            setRankAll();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        addTxtFldLookUp();
        addBtnLookUp();
        addBtnHome();
        addLblUserWin();
    }
    
}
