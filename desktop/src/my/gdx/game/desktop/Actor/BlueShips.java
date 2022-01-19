package my.gdx.game.desktop.Actor;

import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public class BlueShips extends Ships { 
    private static int hp = 2000;
    private static int attack = 300;
    private static int defensepercent = 15;
    private static String textureName = "BlueShips0";
    private static float criticalpercent = 15;
    public final static int TYPE = 3;
    private final static float DELAY = 1.9f;
    public BlueShips(Point position,Size size,int level,boolean clash, int id) {
        super(textureName,position,size,hp+(int)((level - 1)*1.25*hp), attack+(int)((level - 1)*1.1*attack), defensepercent+level-1, criticalpercent, clash, id,DELAY,TYPE,level);
        //TODO Auto-generated constructor stub
        if (CaptainManagement.captainChooseID.contains("003-")) {
            this.setHp((int) (this.getHp() * (1 + CaptainManagement.arrCaptain[2].getBonus() / 100f)));
            this.setAttack((int) (this.getAttack() * (1 + CaptainManagement.arrCaptain[2].getBonus() / 100f)));
        }
    }
    @Override
    public void upLevel() {
        // TODO Auto-generated method stub
        super.upLevel();
        if (CaptainManagement.captainChooseID.contains("003-")) {
            this.setHp((int) (this.getHp() * (1 + CaptainManagement.arrCaptain[2].getBonus() / 100f)));
            this.setAttack((int) (this.getAttack() * (1 + CaptainManagement.arrCaptain[2].getBonus() / 100f)));
        }
    }
}
