import java.awt.*;

public class Room {
    private Tile[][] grid;
    private Image map;

    public Room(Tile[][] grid, Image map) {
        this.grid = grid;
        this.map = map;
    }
}
