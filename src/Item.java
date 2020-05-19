import java.awt.*;

public class Item {
    private int id;
    private String name;
    private Image image;
    private int buyCost;
    private int sellCost;
    public static final int[] canBeEquipped = new int[]{1, 5, 6};
    public static final int[] soldAtStore = new int[]{1, 5, 6, 131, 132, 133, 134, 135, 136, 137, 138, 130, 140, 141, 142, 143, 144, 145, 146};
	

    public Item(int id, String name, Image image, int buyCost, int sellCost) {
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }
}

class DroppedItem extends Item {
    private int xTile, yTile;

    public DroppedItem(Item item, int xTile, int yTile) {
        super(item.getId(), item.getName(), item.getImage(), item.getBuyCost(), item.getSellCost());
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