import java.util.*;
import java.io.*;

public class NPC {
    public static final ArrayList<String> greetings = new ArrayList<>();
    public static final ArrayList<String> chats = new ArrayList<>();
    public static final ArrayList<String> goodbyes = new ArrayList<>();

    public NPC() {

    }

    public static void loadDialogue() {
        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/greetings.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                greetings.add(stdin.nextLine().trim());
            }

            stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/chats.txt")));
            n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                chats.add(stdin.nextLine().trim());
            }

            stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/goodbyes.txt")));
            n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                goodbyes.add(stdin.nextLine().trim());
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Dialogue stuffs not found");
        }
    }
}
