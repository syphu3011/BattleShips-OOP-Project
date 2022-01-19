package my.gdx.game.desktop.Map;

public class MapBattle {
    public static Box[][] mapbattle;
    public static float sizeWidth;
    public static float sizeHeight;
    public static final int NUMROW = 7;
    public static final int NUMCOL = 7;
    public static void loadData(float screenX,float screenY) {
        mapbattle = new Box[NUMROW][NUMCOL];
        sizeWidth = screenX / (NUMCOL);
        sizeHeight = screenY / (NUMROW);
        int count = 0;
        for (int i = 0; i < NUMROW/2; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                mapbattle[i][j] = new Box(true, j * sizeWidth, (j+1) * sizeWidth, i * sizeHeight, (i+1) * sizeHeight,count++);
            }
        }
        for (int i = NUMROW/2; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                mapbattle[i][j] = new Box(false, j * sizeWidth, (j+1) * sizeWidth, i * sizeHeight, (i+1) * sizeHeight,count++);
            }
        }
    }
    public void getPosAll() {
        for (int i = 0; i < 10/2; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf ("(%f %f),",mapbattle[i][j].getPoint().getX(),mapbattle[i][j].getPoint().getY());
            }
            System.out.println();
        }
        for (int i = 10/2; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.printf ("(%f %f),",mapbattle[i][j].getPoint().getX(),mapbattle[i][j].getPoint().getY());
            }
            System.out.println();
        }
    }
}
