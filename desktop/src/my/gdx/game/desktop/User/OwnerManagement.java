package my.gdx.game.desktop.User;

import java.util.ArrayList;

public class OwnerManagement {
    public static ArrayList<Owner> arrOwner = new ArrayList<>();
    public static void addOwner (Owner o){
        arrOwner.add(o);
    }
    public static void removeOwner(Owner u) {
        arrOwner.remove(u);
    }
    public static void removeOwner(int index) {
        arrOwner.remove(index);
    }
}
