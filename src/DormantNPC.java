import java.awt.*;

public class DormantNPC {
    private int x, y;
    private int xTile, yTile;

    private final int tileSize = GamePanel.tileSize;
    private final int wSpeed = 2;

    private Room curRoom;
    private Image image;

    private final String name;

    public DormantNPC(int xTile, int yTile, Room curRoom, Image image, String name) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.x = xTile * tileSize;
        this.y = yTile * tileSize;
        this.curRoom = curRoom;
        this.image = image;
        this.name = name;
    }

    public void draw(Graphics g, int playerX, int playerY, Room curRoom) {
        if (curRoom == this.curRoom) {
            g.drawImage(image, x - playerX  + 480, y - playerY + 300, null);
            System.out.println("lolxd");
        }

    }

    public int getxTile() {
        return xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Room getCurRoom() {
        return curRoom;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
