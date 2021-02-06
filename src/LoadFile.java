import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class LoadFile extends JPanel{
	private boolean[] slotsUsed = {false, false, false};
	private String[] names = new String[3];

	Image background = new ImageIcon("Assets/Misc/Game Files Screen.png").getImage();


    public LoadFile() {
		try {
			Scanner stdin = new Scanner(new BufferedReader(new FileReader("Saves/Used Slots.txt")));
			while (stdin.hasNextLine()) {
				String[] line = stdin.nextLine().split(" ");
				slotsUsed[Integer.parseInt(line[0]) - 1] = true;
				names[Integer.parseInt(line[0]) - 1] = line[1];
				System.out.println(line[1]);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
    }

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


        if (g instanceof Graphics2D) {
        	Graphics2D g2 = (Graphics2D) g;

        	Font helvetica = new Font("Helvetica", Font.PLAIN, 30);
	        FontMetrics fontMetrics = new JLabel().getFontMetrics(helvetica);
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setFont(helvetica);
	        g2.setColor(Color.BLACK);

	        for (int i = 0; i < 3; i++) {
        		if (slotsUsed[i]) {
					g2.drawString(names[i], 400, 290 + 60 * i);
				}

			}
        }

        
    }

	
}
