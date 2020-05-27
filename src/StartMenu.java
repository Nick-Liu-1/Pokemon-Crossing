import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartMenu extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();

    public StartMenu() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Title Screen.png");
		//ImageIcon background = new ImageIcon("Title Screen.png");
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
    			System.out.println("options");
    		}
		});
		optionsBtn.setBounds(653, 384, 219, 64);
		layeredPane.add(optionsBtn,1);
		optionsBtn.setOpaque(false);
		
		JButton tempBtn = new JButton();	
		tempBtn.addActionListener(new ActionListener()){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			//Rocketate PokemonCrossing = new Rocketate();
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
    
    public static void main(String[] arguments) {
		StartMenu menu = new StartMenu();
    }
}
