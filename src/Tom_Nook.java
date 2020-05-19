import com.sun.corba.se.spi.ior.IORTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Tom_Nook {
    private final Image image = new ImageIcon("Assets/NPCs/tom nook.png").getImage();
    private final Room room;
    private final Image speechBubble = new ImageIcon("Assets/Misc/speech bubble copy.png").getImage();
    private final Image shopImage = new ImageIcon("Assets/Misc/shop menu.png").getImage();

    private Player player;

    private ArrayList<Item> storeItems = new ArrayList<>();



    public Tom_Nook(Room room, Player player) {
        this.room = room;
        this.player = player;
    }

    public void draw(Graphics g, int playerX, int playerY) {
        g.drawImage(image, getxTile() * GamePanel.tileSize - playerX + 480, getyTile() * GamePanel.tileSize - playerY + 300, null);

        if (player.isTalkingToNPC() && player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
            //g.drawImage(speechBubble, 10, 500, null);
            player.setShopOpen(true);
            g.drawImage(shopImage, (1020 - 825) / 2, (695 - 587) / 2, null);
        }

    }

    public void generateStoreItems() {
        for (int i = 0; i < 4; i++) {
            storeItems.add(GamePanel.getItems().get(GamePanel.randint(0, Item.soldAtStore.length)));
        }
    }

    public Image getImage() {
        return image;
    }

    public int getxTile() {
        return 11;
    }

    public int getyTile() {
        return 8;
    }

    public Room getRoom() {
        return room;
    }
}
