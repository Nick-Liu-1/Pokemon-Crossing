import java.awt.*;

public class Furniture {
    private int xTile, yTile;
    private int length, width;
    private Image image;
    private int id;

    public Furniture(int xTile, int yTile, int id, Image image) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.image = image;
        this.id = id;
    }

}
