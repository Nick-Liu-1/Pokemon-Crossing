import java.awt.*;

public class Item {
    private int id;
    private Image image;
    private int buyCost;
    private int sellCost;
	

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
}
