import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.util.Scanner;

public class NewFile extends JFrame {
	private JLayeredPane layeredPane=new JLayeredPane();
	private boolean[] slotsUsed = {false, false, false};
	private String[] names = new String[3];

    public NewFile() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Game Files Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);
		
		JButton backBtn = new JButton();	
		backBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			StartMenu menu = new StartMenu();
    			setVisible(false);
    		}
		});
		backBtn.setBounds(54, 566, 259, 77);
		layeredPane.add(backBtn,1);
		backBtn.setOpaque(false);
		
		JButton slot1 = new JButton();	
		slot1.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
				String name = JOptionPane.showInputDialog("Name:");
				String gender = JOptionPane.showInputDialog("Gender: (male/female)");
				int g = gender.toLowerCase().equals("male") ? Player.MALE : Player.FEMALE;
				createNewFile(1, name, g);
				Main main = new Main(1);
    		}			
		});
		slot1.setBounds(326, 249, 400, 60);
		layeredPane.add(slot1,1);
		slot1.setOpaque(false);
		
		JButton slot2 = new JButton();	
		slot2.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
				String name = JOptionPane.showInputDialog("Name:");
				String gender = JOptionPane.showInputDialog("Gender: (male/female)");
				int g = gender.toLowerCase().equals("male") ? Player.MALE : Player.FEMALE;
				createNewFile(2, name, g);
				Main main = new Main(2);
    		}			
		});
		slot2.setBounds(326, 309, 400, 60);
		layeredPane.add(slot2,1);
		slot2.setOpaque(false);
		
		JButton slot3 = new JButton();	
		slot3.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Name:");
				String gender = null;

				if (name != null) {
					gender = JOptionPane.showInputDialog("Gender: (male/female)");
				}

				if (name != null && gender != null) {
					int g = gender.toLowerCase().equals("male") ? Player.MALE : Player.FEMALE;
					createNewFile(3, name, g);
					Main main = new Main(3);
				}

    		}			
		});
		slot3.setBounds(326, 369, 400, 60);
		layeredPane.add(slot3,3);
		slot3.setOpaque(false);

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


		for (int i = 0; i < 3; i++) {
			JLabel text;
			if (!slotsUsed[i]) {
				text = new JLabel("New file");
			}
			else {
				text = new JLabel("Overwrite \"" + names[i] + "\"");
			}

			text.setFont(new Font("Helvetica", Font.PLAIN, 30));
			Dimension size = text.getPreferredSize();
			text.setBounds(400, 258 + 60 * i, size.width, size.height);
			layeredPane.add(text, 0);
		}
			
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
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

	public static void main(String[] arguments) {
		NewFile frame = new NewFile();
    }
}
