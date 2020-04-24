import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Hashtable;

public class Item {
    private int id;
    private static Hashtable<String, Image> bugImages = new Hashtable<>();
    private static Hashtable<String, Image> fishImages = new Hashtable<>();
    private static Hashtable<String, Image> generalImages = new Hashtable<>();
    private static Hashtable<String, Image> shellImages = new Hashtable<>();

    public Item(int id) {
        this.id = id;
    }

    public void load() {
        File folder = new File("Assets/Items/Bugs");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                bugImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Items/Bugs"+listOfFiles[i].getName()).getImage());
            }
        }

        folder = new File("Assets/Items/Fish");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fishImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Items/Fish"+listOfFiles[i].getName()).getImage());
            }
        }

        folder = new File("Assets/Items/General");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                generalImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Items/General"+listOfFiles[i].getName()).getImage());
            }
        }


        folder = new File("Assets/Items/Shell");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                shellImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Items/Shell"+listOfFiles[i].getName()).getImage());
            }
        }
    }
}
