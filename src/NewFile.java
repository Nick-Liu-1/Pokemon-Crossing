import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.util.Scanner;

public class NewFile extends JPanel {
	private boolean[] slotsUsed = {false, false, false};
	private String[] names = new String[3];
	Image background = new ImageIcon("Assets/Misc/Game Files Screen.png").getImage();


    public NewFile() {	

		try {
			Scanner stdin = new Scanner(new BufferedReader(new FileReader("Saves/Used Slots.txt")));
			while (stdin.hasNextLine()) {
				String[] line = stdin.nextLine().split(" ");
				slotsUsed[Integer.parseInt(line[0]) - 1] = true;
				names[Integer.parseInt(line[0]) - 1] = line[1];
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
        		if (!slotsUsed[i]) {
					g2.drawString("New file", 400, 290 + 60 * i);
				}
				else {
					g2.drawString("Overwrite \"" + names[i] + "\"", 400, 290 + 60 * i);
				}
			}
        }

        
    }


	public void createNewFile(int num, String name, int gender) {
    	slotsUsed[num-1] = true;
    	names[num-1] = name;

		PrintWriter outFile;
		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/save" + num + ".txt")));

			outFile.println(name); // name
			outFile.println(gender); // gender
			outFile.println(0); // bells
			for (int i = 0; i < 19; i++) {  // items
				outFile.println("null");
			}
			outFile.println("orange"); // wallpaper
			outFile.println("yellow"); // floor

			outFile.println(); // museum bugs
			outFile.println(); // museum fish
			outFile.println(); // museum fossils

			outFile.println(Instant.now().getEpochSecond());
			outFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/trees.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/trees.txt")));
				int n = Integer.parseInt(stdin.nextLine());
				outFile.println(n);

				for (int i = 0; i < n; i++) {
					outFile.println(stdin.nextLine());
				}

				n = Integer.parseInt(stdin.nextLine());
				outFile.println(n);

				for (int i = 0; i < n; i++) {
					outFile.println(stdin.nextLine());
				}

				outFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/map.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/map.txt")));

				for (int i = 0; i < 85; i++) {
					outFile.println(stdin.nextLine());
				}

				outFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/minigame island map.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/minigame island map.txt")));

				for (int i = 0; i < 46; i++) {
					outFile.println(stdin.nextLine());
				}

				outFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/rooms.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Rooms/Rooms.txt")));

				while (stdin.hasNextLine()) {
					outFile.println(stdin.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/outside diggable tiles.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/outside diggable tiles.txt")));

				for (int i = 0; i < 85; i++) {
					outFile.println(stdin.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/minigame island diggable tiles.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/minigame island diggable tiles.txt")));

				for (int i = 0; i < 46; i++) {
					outFile.println(stdin.nextLine());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/Used Slots.txt")));
			for (int i = 0; i < 3; i++) {
				if (slotsUsed[i]) {
					outFile.println(i+1 + " " + names[i]);
				}
			}
			outFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
