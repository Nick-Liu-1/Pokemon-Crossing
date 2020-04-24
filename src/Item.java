import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Hashtable;

public class Item {
    private int id;
    private int value;
    private static Hashtable<String, Image> bugImages = new Hashtable<>();
    private static Hashtable<String, Image> fishImages = new Hashtable<>();
    private static Hashtable<String, Image> generalImages = new Hashtable<>();
    private static Hashtable<String, Image> shellImages = new Hashtable<>();

    public Item(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public static void load() {

    }
}
