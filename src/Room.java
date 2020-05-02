import java.awt.*;
import java.util.Hashtable;

public class Room {
    private int[][] grid;
    private final Image image;
    private final int entryX;
    private final int entryY;
    private final int exitX;
    private final int exitY;
    private final int exitX2;
    private final int exitY2;

    private Hashtable<Point, DroppedItem> droppedItems = new Hashtable<>();

    public Room(int[][] grid, Image image, int entryX, int entryY, int exitX, int exitY, int exitX2, int exitY2) {
        this.grid = grid;
        this.image = image;
        this.entryX = entryX;
        this.entryY = entryY;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitX2 = exitX2;
        this.exitY2 = exitY2;
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

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public Hashtable<Point, DroppedItem> getDroppedItems() {
        return droppedItems;
    }

    public void setDroppedItems(Hashtable<Point, DroppedItem> droppedItems) {
        this.droppedItems = droppedItems;
    }

    public void addDroppedItem(DroppedItem item) {
        droppedItems.put(new Point(item.getxTile(), item.getyTile()), item);
    }
}
