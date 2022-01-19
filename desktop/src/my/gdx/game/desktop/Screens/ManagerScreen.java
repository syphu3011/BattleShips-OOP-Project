package my.gdx.game.desktop.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public class ManagerScreen implements Screen, ScreenInterface{
    private Game game;
    private Stage stage;
    private Group backGround;
    private Button btnManagePlayer;
    private Button btnManageOwner;
    public ManagerScreen(Game g) {
        stage = new Stage(new ScreenViewport());
        this.game = g;
        Gdx.input.setInputProcessor(stage);
        setBackground();
        setItem();
    }
    private void addManager() {
        Label lblManager = new Label("MANAGER", new LabelStyle(Asset.font, Color.RED));
        lblManager.setFontScale(2);
        lblManager.setPosition(stage.getWidth()/2 - 100, 650);
        backGround.addActor(lblManager);
    }
    private void addBtnHome() {
        final Button btnHome;
        Size btnHomeSize = new Size(75, 75);
        btnHome = new Button("btnhome.png", new Point(stage.getWidth() - btnHomeSize.getWidth() / 2 - 20,
                stage.getHeight() - btnHomeSize.getHeight() / 2 - 20), btnHomeSize);
        backGround.addActor(btnHome);

        btnHome.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnHome.normalContain(btnHome.getX() + x, btnHome.getY() + y))
                    game.setScreen(new MainMenuScreen(game));
            }
        });
    }
    private void addBtnManagePlayer() {
        btnManagePlayer = new Button("btnPlayer.png",new Point(stage.getWidth()/2 - 200,stage.getHeight()/2), new Size(200,200));
        backGround.addActor(btnManagePlayer);
        btnManagePlayer.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                if (btnManagePlayer.normalContain(btnManagePlayer.getX() + x, btnManagePlayer.getY() + y));
                    game.setScreen(new PlayerManageScreen(game));
            }
        });
    }
    private void addBtnManageOwner() {
        btnManageOwner = new Button("btnOwner.png",new Point(stage.getWidth()/2 + 200,stage.getHeight()/2), new Size(200,200));
        backGround.addActor(btnManageOwner);
        btnManageOwner.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // TODO Auto-generated method stub
                if (btnManageOwner.normalContain(btnManageOwner.getX() + x, btnManageOwner.getY() + y));
                    game.setScreen(new OwnerManageScreen(game));
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
        img.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        backGround.addActor(img);
    }

    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        addBtnManageOwner();
        addBtnManagePlayer();
        addBtnHome();
        addManager();
    }
}
