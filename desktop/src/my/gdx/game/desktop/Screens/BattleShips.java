package my.gdx.game.desktop.Screens;

import java.sql.SQLException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Map.MapBattle;
import my.gdx.game.desktop.Utils.Asset;

public class BattleShips extends Game {

    @Override
    public void create() {
        // TODO Auto-generated method stub
        try {
            Asset.load();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Stage stage = new Stage(new ScreenViewport());
        MapBattle.loadData(stage.getWidth(), stage.getHeight());
        setScreen(new LoginScreen(this));
    }
    public void changeToWinner() {
        setScreen(new WinScreen(this));
    }
    @Override
    public void render() {
        // TODO Auto-generated method stub
        super.render();
    }
}
