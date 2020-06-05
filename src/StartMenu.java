import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class StartMenu extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	private int num = 1;

    public StartMenu() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Title Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);
		
		JButton loadBtn = new JButton();	
		loadBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			LoadFile frame = new LoadFile();
    			setVisible(false);
    		}
		});
		loadBtn.setBounds(152, 384, 219, 64);
		layeredPane.add(loadBtn,1);
		loadBtn.setOpaque(false);
		
		JButton newBtn = new JButton();	
		newBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			NewFile frame = new NewFile();
    			setVisible(false);
    		}			
		});
		newBtn.setBounds(401, 384, 219, 64);
		layeredPane.add(newBtn,1);
		newBtn.setOpaque(false);
		
		JButton optionsBtn = new JButton();	
		optionsBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			Options frame = new Options();
    			setVisible(false);
    		}
		});
		optionsBtn.setBounds(653, 384, 219, 64);
		layeredPane.add(optionsBtn,1);
		optionsBtn.setOpaque(false);
		
		JButton tempBtn = new JButton();	
		tempBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			Main PokemonCrossing = new Main(num);
    			setVisible(false);
    		}
		});
		tempBtn.setBounds(0, 470, 1020, 235);
		layeredPane.add(tempBtn,1);
		tempBtn.setOpaque(false);
			
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

    }

    public void createNewFile(int num, String name, int gender) {
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(
				new BufferedWriter (new FileWriter ("Saves/save" + num + "/save" + num + ".txt")));

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

			outFile.println(); // placed furniture

			outFile.close();

		}
    	catch (IOException e) {
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
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch (IOException e) {
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
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch (IOException e) {
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
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outFile = new PrintWriter(
				new BufferedWriter(new FileWriter("Saves/save" + num + "/rooms.txt")));

			try {
				Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Rooms/Rooms.txt")));

				for (int i = 0; i < 218; i++) {
					outFile.println(stdin.nextLine());
				}
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		}
		catch (IOException e) {
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
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		}
		catch (IOException e) {
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
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			outFile.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
    
    public static void main(String[] arguments) {
		StartMenu menu = new StartMenu();
    }
}
