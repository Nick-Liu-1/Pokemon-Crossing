import java.awt.*;

public class Room {
    private int[][] grid;
    private Image image;
    private int entryX;
    private int entryY;

    public Room(int[][] grid, Image image, int entryX, int entryY) {
        this.grid = grid;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
