package my.gdx.game.desktop.Utils;

import java.sql.SQLException;

public class StaticSupport {

    public static void goldUp(int prize) throws SQLException {
        //up vàng lên và set vào database
        Asset.gold += prize;
        String query = "UPDATE Player SET golden = " + Asset.gold + " WHERE id = " + Asset.id;
        Asset.setData(query);
    }
    public static boolean isDigit(String input) {
        // null hoặc length < 0 -> false.
        if (input == null || input.length() < 0)
            return false;
        // empty -> false
        input = input.trim();
        if ("".equals(input))
            return false;
        if (input.startsWith("-")) {
            // nếu là số âm thì cắt ký tự đầu tiên
            return input.substring(1).matches("[0-9]*");
        } else {
            // nếu là số dương cũng thực hiện kiểm tra qua
            return input.matches("[0-9]*");
        }
    }
    public static boolean checkSpace(String s) {
        if (s == null || s == "" || s.contains(" ")) {
            return true;
        }
        return false;
    }
    public static void winUp() {
        Asset.win += 1;
        String query = "UPDATE Player SET battleWin = "+Asset.win+" WHERE id = "+Asset.id;
        try {
            Asset.setData(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
