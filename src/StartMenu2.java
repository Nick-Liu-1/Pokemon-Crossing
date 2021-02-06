import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class StartMenu2 extends JPanel {
    // Basic UI images used by most menu pages
    Image background = new ImageIcon("Assets/Misc/Title Screen.png").getImage();


    @Override
    public void addNotify() {
        /*
            Gets focus of panel
         */
        super.addNotify();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g) {
        /*
            Paints the stuff to be drawn. Called automatically
         */
        g.drawImage(background, 0, 0, null);
    }

    
}