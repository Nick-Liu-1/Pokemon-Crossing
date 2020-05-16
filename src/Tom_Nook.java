import javax.swing.*;
import java.awt.*;

public class Tom_Nook {
    private final Image image = new ImageIcon("Assets/NPCs/tom nook.png").getImage();
    private final Room room;


    public Tom_Nook(Room room) {
        this.room = room;
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
