package my.gdx.game.desktop.Screens;

import java.sql.*;

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
import my.gdx.game.desktop.Utils.StaticSupport;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Utils.Asset;

public class WinScreen implements Screen,ScreenInterface {
    private Stage stage;
    private Game game;
    private Group backGround;
    private Button btnReplay;
    private Button btnHome;
    private Size btnSize;
    private int prize = 100;
    private final float RATIO = 697f / 706;
    private BitmapFont font;
    public WinScreen(Game g) {
        StaticSupport.winUp();
        stage = new Stage(new ScreenViewport());
        game = g;
        font = Asset.font;
        Gdx.input.setInputProcessor(stage);
        setBackground();
        setItem();
        animateStarting();
        listenerManager();
    }
    private void addStatistical() {
        Label attack = new Label("ATTACK: "+GameScreen.attack,new LabelStyle(font,Color.ORANGE));
        Label defend = new Label("DEFEND: "+GameScreen.defend,new LabelStyle(font,Color.ORANGE));
        attack.setPosition(20, stage.getHeight() - 400);
        defend.setPosition(600, stage.getHeight() - 400);
        backGround.addActor(attack);
        backGround.addActor(defend);
    }
    private void listenerManager() {
        //thao tác chạm
        btnReplay.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnReplay.Contains(btnReplay.getX()+x, btnReplay.getY()+y))
                    game.setScreen(new GameScreen(game));
            }
        });
        btnHome.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnHome.Contains(btnHome.getX()+x, btnHome.getY()+y))
                    game.setScreen(new MainMenuScreen(game));
            }
        });
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
    public void animateStarting() {
        // TODO Auto-generated method stub
        //tăng vàng lên
        try {
            StaticSupport.goldUp(prize);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final float TIMEWAIT = 0.25f;
        //đưa 2 button vào
        btnHome.addAction(Actions.sequence(Actions.moveTo(btnHome.getX(), stage.getHeight()/8,TIMEWAIT)));
        btnReplay.addAction(Actions.sequence(Actions.delay(TIMEWAIT),Actions.moveTo(btnReplay.getX(), stage.getHeight()/8,TIMEWAIT)));
        String youwin = "CONGRATULATION!";
        int count = 2;
        Label lblWinner;
        //đưa dòng chữ chúc mừng vào
        for (int i = 0; i < youwin.length(); i++) {
            lblWinner = new Label(youwin.charAt(i)+"",new Label.LabelStyle(font,Color.ORANGE));
            lblWinner.setFontScale(3);
            lblWinner.setPosition(50+i*50, 900);
            lblWinner.setAlignment(Align.center);
            lblWinner.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(lblWinner.getX(), 700,TIMEWAIT-0.05f)));
            backGround.addActor(lblWinner);
        }
        //đưa prize vào
        lblWinner = new Label("PRIZE: "+prize,new Label.LabelStyle(font,Color.RED));
        lblWinner.setFontScale(2);
        lblWinner.setPosition(-250, stage.getHeight()/2 + 50);
        lblWinner.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(stage.getWidth()/2-100,lblWinner.getY(),TIMEWAIT)));
        backGround.addActor(lblWinner);
        //đưa số tiền vào
        lblWinner = new Label("GOLD: "+Asset.gold,new Label.LabelStyle(font,Color.RED));
        lblWinner.setFontScale(2);
        lblWinner.setPosition(stage.getWidth() + 250, stage.getHeight()/2 - 50);
        lblWinner.addAction(Actions.sequence(Actions.delay((TIMEWAIT-0.2f) * count++),Actions.moveTo(stage.getWidth()/2-100,lblWinner.getY(),TIMEWAIT)));
        backGround.addActor(lblWinner);
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
        btnSize = new Size(stage.getWidth() / 6, stage.getWidth() / 6 * RATIO);
        btnReplay = new Button("btnReplay.png",new Point(stage.getWidth() / 2 - btnSize.getWidth() * 1.05f, 0), btnSize);
        btnHome = new Button("btnhome.png",new Point(stage.getWidth() / 2 + btnSize.getWidth() * 1.05f, 0), btnSize);
        backGround.addActor(btnReplay);
        backGround.addActor(btnHome);
        addStatistical();
    }

}
