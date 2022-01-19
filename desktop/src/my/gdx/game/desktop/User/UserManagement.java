package my.gdx.game.desktop.User;

import java.util.ArrayList;

/**
 * UserManagement
 */
public class UserManagement {
    public static ArrayList<User> arrUser = new ArrayList<>();
    public static void addUser(User u) {
        arrUser.add(u);
    }
    public static void removeUser(User u) {
        arrUser.remove(u);
    }
    public static void removeUser(int index) {
        arrUser.remove(index);
    }
    public static void changeGoldUser(User u, int gold) {
        arrUser.remove(u);
        u.setGold(gold);
        arrUser.add(u);
    }
    public static void changeUserName(int id, String userName) {
        User u = new User();
        for (User i : arrUser)
            if (id == i.getId())
                u = i;
        arrUser.remove(u);
        u.setUserName(userName);
        arrUser.add(u);
    }
}