package my.gdx.game.desktop.Actor;

import java.util.ArrayList;
import java.util.Arrays;

public class CaptainManagement {
    public static Captain[] arrCaptain = new Captain[0];
    public static ArrayList<String> arrOwnCaptainID = new ArrayList<>();
    public static String captainChooseID = "";
    public static void addCaptain(Captain captain){
        arrCaptain = Arrays.copyOf(arrCaptain, arrCaptain.length + 1);
        arrCaptain[arrCaptain.length - 1] = captain;
    }

    public static void removeCaptain(Captain captain){
        int index = arrCaptain.length;
        for (int i = 0;i < arrCaptain.length;i++){
            if (arrCaptain[i] == captain) {
                index = i;
                break;
            }
        }
        for (int i = index;i < arrCaptain.length;i++)
            arrCaptain[i] = arrCaptain[i + 1];
        if (index != arrCaptain.length){
            arrCaptain = Arrays.copyOf(arrCaptain,arrCaptain.length - 1);
            captain.remove();
        }
    }
}
