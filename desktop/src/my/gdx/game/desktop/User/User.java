package my.gdx.game.desktop.User;

public class User {
    private int rank;
    private int id;
    private String userName;
    private int gold;
    private int battleWin;
    public User(){}
    public User(int rank, int id, String userName, int gold, int battleWin) {
        this.rank = rank;
        this.id = id;
        this.userName = userName;
        this.gold = gold;
        this.battleWin = battleWin;
    }
    public int getRank() {
        return rank;
    }
    public int getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public int getGold() {
        return gold;
    }
    public int getBattleWin() {
        return battleWin;
    }
}
