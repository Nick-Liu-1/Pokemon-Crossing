import java.awt.*;

public class Room {
    private final int[][] grid;
    private final Image image;
    private final int entryX;
    private final int entryY;
    private final int exitX;
    private final int exitY;

    public Room(int[][] grid, Image image, int entryX, int entryY, int exitX, int exitY) {
        this.grid = grid;
        this.image = image;
        this.entryX = entryX;
        this.entryY = entryY;
        this.exitX = exitX;
        this.exitY = exitY;
    }

    public Image getImage() {
        return image;
    }

    public int getEntryX() {
        return entryX;
    }

    public int getEntryY() {
        return entryY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public int[][] getGrid() {
        return grid;
    }
}
