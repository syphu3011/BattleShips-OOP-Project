package my.gdx.game.desktop.Actor;

import java.util.Arrays;

public class ShipsManagement {
    public Ships[] arrShips;

    public ShipsManagement() {
        arrShips = new Ships[0];
    }

    public ShipsManagement(Ships[] arrShips) {
        this.arrShips = arrShips;
    }
    public void addShip(Ships ship) {
        arrShips = Arrays.copyOf(arrShips, arrShips.length + 1);
        arrShips[arrShips.length - 1] = ship;
    }
    public void removeShip(Ships ships) {
        int index = this.arrShips.length;
        for (int i = 0; i < this.arrShips.length; i++) 
            if (this.arrShips[i] == ships) {
                index = i;
                break;
            }
        for (int i = index; i < this.arrShips.length - 1; i++) 
            this.arrShips[i] = this.arrShips[i+1];
        if (index != this.arrShips.length) {
            this.arrShips = Arrays.copyOf(this.arrShips, this.arrShips.length-1);
            ships.remove();
        }
    }
}
