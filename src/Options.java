import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Options extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	
    public Options() {
		super("Pokemon Crossing");
		setSize(1020,695);
	
		ImageIcon background = new ImageIcon("Assets/Misc/Options Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);
	
		JButton plus = new JButton();	
		plus.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			System.out.println("plus");
    		}			
		});
		plus.setBounds(735, 543, 83, 83);
		layeredPane.add(plus,1);
		plus.setOpaque(false);
		
		JButton minus = new JButton();	
		minus.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			System.out.println("minus");
    		}			
		});
		minus.setBounds(222, 543, 83, 83);
		layeredPane.add(minus,1);
		minus.setOpaque(false);
	
		JButton backBtn = new JButton();	
		backBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			StartMenu menu = new StartMenu();
    			setVisible(false);
    		}			
		});
		backBtn.setBounds(29, 29, 195, 57);
		layeredPane.add(backBtn,1);
		backBtn.setOpaque(false);
	
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
    }
    
    public static void main(String[] arguments) {
		Options frame = new Options();
    }
}
