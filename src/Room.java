/*
    Room.java
    Nick Liu + Annie Zhang
    ICS4U
    Room class allows room objects to be created and contain the necessary information ind dealing with rooms.
 */

import java.awt.*;
import java.util.Hashtable;

public class Room {
    private int[][] grid;  // Tile information
    private final int[][] originalGrid;  // Original version without furniture
    private final Image image;

    // Entry and exit tiles
    private final int entryX;
    private final int entryY;
    private final int exitX;
    private final int exitY;
    private final int exitX2;
    private final int exitY2;
    private String name;

    private Hashtable<Point, DroppedItem> droppedItems = new Hashtable<>();  // All dropped items in the room

    // Constructor
    public Room(int[][] grid, Image image, int entryX, int entryY, int exitX, int exitY, int exitX2, int exitY2, String name) {
        this.grid = grid;
        this.image = image;
        this.entryX = entryX;
        this.entryY = entryY;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitX2 = exitX2;
        this.exitY2 = exitY2;
        this.name = name;

        // Copying the grid
        originalGrid = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                originalGrid[i][j] = grid[i][j];
            }
        }
    }

    // Getters and setters
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

    public int getExitX2() {
        return exitX2;
    }

    public int getExitY2() {
        return exitY2;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public int[][] getOriginalGrid() {
        return originalGrid;
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

    public String getName() {
        return name;
    }
}
