import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Item {
    private int id;
    private String name;
    private Image image;
    private int buyCost;
    private int sellCost;
    public static final int[] canBeEquipped = new int[]{1, 5, 6};
    public static final int[] soldAtStore = new int[]{1, 5, 6, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 130, 140, 141, 142, 143, 144};
	public static final int FURNITURE_START = 128;
	public static final int FURNITURE_END = 143;

	public static final Image leafImage = new ImageIcon("Assets/Items/General/leaf.png").getImage();
	public static final Image storeLeafImage = leafImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

	public static ArrayList<String> fossilNames = new ArrayList<>();

    public static final ArrayList<Image> foundItemImages = new ArrayList<>();



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

    public boolean isFurniture() {
        return id >= FURNITURE_START && id <= FURNITURE_END;
    }

    public static void loadFossils() {
        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Items/Fossils.txt")));

            int n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                fossilNames.add(stdin.nextLine());
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading fossil names");
        }

    }

    public static void loadFoundImages() {
        for (int i = 0; i < GamePanel.getItems().size(); i++) {
            foundItemImages.add(GamePanel.getItems().get(i).getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));
        }
    }

    public boolean isBug() {
        return id >= 7 && id <= 37;
    }

    public boolean isOceanFish() {
        return id >= 38 && id <= 69;
    }

    public boolean isRiverFish() {
        return id >= 75 && id <= 105;
    }

    public boolean isPondFish() {
        return id >= 70 && id <= 74;
    }

    public boolean isFossil() {
        return id == 2;
    }

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWallpaper() {
        return id == 125;
    }

    public boolean isFloor() {
        return id == 144;
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