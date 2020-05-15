import java.awt.*;

public class Item {
    private int id;
    private Image image;
    private int buyCost;
    private int sellCost;
    private static final int[] canBeEquipped = new int[]{1, 5, 6};
	

    public Item(int id, Image image, int buyCost, int sellCost) {
        this.id = id;
        this.image = image;
        this.buyCost = buyCost;
        this.sellCost = sellCost;
        
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public int getBuyCost() {
        return buyCost;
    }

    public int getSellCost() {
        return sellCost;
    }

    public static boolean canBeEquipped(int n) {
        return GamePanel.contains(n, canBeEquipped);
    }

    public boolean canBeEquipped() {
        return GamePanel.contains(id, canBeEquipped);
    }
}

class DroppedItem extends Item {
    private int xTile, yTile;

    public DroppedItem(Item item, int xTile, int yTile) {
        super(item.getId(), item.getImage(), item.getBuyCost(), item.getSellCost());
        this.xTile = xTile;
        this.yTile = yTile;
    }

    public int getxTile() {
        return xTile;
    }

    public int getyTile() {
        return yTile;
    }
}