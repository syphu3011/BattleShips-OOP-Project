package my.gdx.game.desktop.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import my.gdx.game.desktop.Actor.CaptainBlue;
import my.gdx.game.desktop.Actor.CaptainGreen;
import my.gdx.game.desktop.Actor.CaptainManagement;
import my.gdx.game.desktop.Actor.CaptainRed;
import my.gdx.game.desktop.User.Owner;
import my.gdx.game.desktop.User.OwnerManagement;
import my.gdx.game.desktop.User.User;
import my.gdx.game.desktop.User.UserManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Asset {
    public static Animation<AtlasRegion> explosionAnim;
    public static Animation<AtlasRegion> captainGreenAnim;
    public static Animation<AtlasRegion> captainRedAnim;
    public static Animation<AtlasRegion> captainBlueAnim;
    public static Texture backGround;
    public static Texture backShop;
    public static Texture btnPlay;
    public static Texture btnSoundOn;
    public static Texture btnSoundOff;
    public static Texture btnShop;
    public static int id = 2;
    public static String userName = "";
    public static int win = 0;
    public static int gold = 0;
    public static BitmapFont font;
    public static ArrayList<String> allName = new ArrayList<>();

    public static Texture loadtexture(String textureName) {
        return new Texture(Gdx.files.internal(textureName));
    }

    public static void load() throws SQLException  {
        explosionAnim = new Animation<>(1 / 120f, new TextureAtlas("explosion").getRegions());
        captainRedAnim = new Animation<>(1/8f,new TextureAtlas("CaptainRed.atlas").getRegions());
        captainGreenAnim = new Animation<>(1/8f,new TextureAtlas("CaptainGreen.atlas").getRegions());
        captainBlueAnim = new Animation<>(1/8f,new TextureAtlas("CaptainBlue.atlas").getRegions());
        backGround = loadtexture("background.png");
        backShop = loadtexture("shopback.png");
        font = new BitmapFont(Gdx.files.internal("gamefont.fnt"));
    }
    public static void loadDatabase() throws SQLException {
        getIdGold();
        getCaptainData();
        getOwnerData();
    }
    public static Connection getConnect() {
        Connection conn = null;
        try {
            // DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
            String dbURL = "jdbc:sqlserver://localhost;databaseName=BattleShips;user=sa;password=1234MP";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("connect successful!");
            }
        } catch (SQLException ex) {
            System.err.println("Cannot connect database, " + ex);
        } 
        return conn;
    }
    public static ResultSet getData(String query) throws SQLException {
        Connection conn = getConnect();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs;
    }public static void setData(String query) throws SQLException {
        Connection conn = getConnect();
        Statement st = conn.createStatement();
        st.executeUpdate(query);
    }
    public static void setId(int idP) {
        id = idP;
    }
    private static void getIdGold() throws SQLException {
        String query = "SELECT * FROM Player WHERE id = "+id;
        ResultSet rs = getData(query);
        while (rs.next()) {
            userName = rs.getString("userName");
            win = rs.getInt("battleWin");
            gold = rs.getInt("golden");
        }
    }
    private static void getCaptainData() throws SQLException {
        String query = "SELECT * FROM Captain";
        ResultSet rs =  getData(query);
        while (rs.next()) {
            String texttureName = rs.getString("captainname");
            String idCaptain = rs.getString("id");
            int price = rs.getInt("price");
            String attribute = rs.getString("attribute");
            double bonus = rs.getDouble("bonus");
            if (idCaptain.contains("001-")) {
                CaptainGreen captain = new CaptainGreen(texttureName, idCaptain, price, attribute, bonus);
                CaptainManagement.addCaptain(captain);
            }
            else {
                if (idCaptain.contains("002-")) {
                    CaptainRed captain = new CaptainRed(texttureName, idCaptain, price, attribute, bonus);
                    CaptainManagement.addCaptain(captain);
                }
                else {
                    CaptainBlue captain = new CaptainBlue(texttureName, idCaptain, price, attribute, bonus);
                    CaptainManagement.addCaptain(captain);
                    System.out.println(Arrays.toString(CaptainManagement.arrCaptain));
                }
            }
        }
    }
    public static void getOwnerData() throws SQLException {
        CaptainManagement.arrOwnCaptainID.clear();
        String query = "SELECT * FROM ownerCaptain WHERE idPlayer = "+id;
        ResultSet rs =  getData(query);
        while (rs.next()) {
            CaptainManagement.arrOwnCaptainID.add(rs.getString("idCaptain"));
            if (rs.getBoolean("statusUse"))
                CaptainManagement.captainChooseID = rs.getString("idCaptain");
        }
    }
    public static void getAllUser() throws SQLException {
        UserManagement.arrUser.clear();
        String query = "SELECT * FROM Player ORDER BY battleWin DESC";
        ResultSet rs = getData(query);
        int rank = 0;
        while (rs.next()) {
            UserManagement.addUser(new User(++rank,rs.getInt("id"), rs.getString("userName"),rs.getInt("golden"),rs.getInt("battleWin")));
        }
    }
    public static void getAllUserSortID() throws SQLException {
        UserManagement.arrUser.clear();
        String query = "SELECT * FROM Player ORDER BY id ASC";
        ResultSet rs = getData(query);
        int rank = 0;
        while (rs.next()) {
            UserManagement.addUser(new User(++rank,rs.getInt("id"), rs.getString("userName"),rs.getInt("golden"),rs.getInt("battleWin")));
        }
    }
    public static void getAllOwner() throws SQLException {
        OwnerManagement.arrOwner.clear();
        String query = "SELECT idPlayer,idCaptain,CAST(dayOwn as DATE) day, statusUse FROM ownerCaptain";
        ResultSet rs = getData(query);
        while (rs.next()) {
            OwnerManagement.addOwner(new Owner(rs.getInt("idPlayer"), rs.getString("idCaptain"),rs.getString("day"),rs.getInt("statusUse")));
        }
    }
} 
