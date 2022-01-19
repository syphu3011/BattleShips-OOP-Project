package my.gdx.game.desktop.User;

public class Owner {
    private int idPlayer;
    private String idCaptain;
    private String dayOwn;
    private int statusUse;
    public Owner(int idPlayer, String idCaptain, String dayOwn, int statusUse) {
        this.idPlayer = idPlayer;
        this.idCaptain = idCaptain;
        this.dayOwn = dayOwn;
        this.statusUse = statusUse;
    }
    public int getIdPlayer() {
        return idPlayer;
    }
    public String getIdCaptain() {
        return idCaptain;
    }
    public String getDayOwn() {
        return dayOwn;
    }
    public int getStatusUse() {
        return statusUse;
    }
}
