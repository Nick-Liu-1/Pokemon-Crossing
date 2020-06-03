import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
class Level{
	private int levelNum;
	private Image background, bullet;
	private int numTargets;
	private int numBullets, totBullets;
	private int numWalls;
	private ArrayList<Target> targets = new ArrayList<Target>();
	private ArrayList<Rectangle> walls = new ArrayList<Rectangle>();
	private boolean complete = false;
	
	public Level(int levelNum, Image background, int numBullets, int numTargets, int numWalls){
		this.levelNum = levelNum;
    	this.background = background;
        this.numTargets = numTargets;
        this.numBullets = numBullets;
        totBullets = numBullets;
        this.numWalls = numWalls;
        bullet = new ImageIcon("Sprites/bullet.png").getImage();
        
        
        try{
            Scanner levelFile = new Scanner(new BufferedReader(new FileReader("Levels/Level"+levelNum+".txt")));
            String[] line;
            for (int i = 0 ; i < numTargets; i++) {
                line = levelFile.nextLine().split(" ");
                targets.add(new Target(Integer.parseInt(line[0]), Integer.parseInt(line[1]) , Integer.parseInt(line[2]), Integer.parseInt(line[3]), Integer.parseInt(line[4])));
                for(int j=0; j<(Integer.parseInt(line[5]))*2; j+=2){
                	(targets.get(i)).addPos(new Point(Integer.parseInt(line[5+j+1]), Integer.parseInt(line[5+j+2])));
                }
            }
		}
        catch(FileNotFoundException e) {
            System.out.println("level file not found");
        }
    }
    
    public int getNumTargets(){return numTargets;}
    public int getNumBullets(){return numBullets;}
    public void removeBullet(){numBullets-=1;}
    public ArrayList<Target> getTargets(){return targets;}
    public ArrayList<Rectangle> getWalls(){return walls;}
    public boolean getComplete(){return complete;}
    
    public void addWall(Rectangle newWall){
    	walls.add(newWall);
    }
    
    public void moveTargets(){
    	int count=0;
    	for(int i=0; i<numTargets; i++){
    		if(targets.get(i).getIsHit()){
    			count++;
    		}
    		targets.get(i).move();
    	}
    	if(count==numTargets){
    		complete=true;
    	}
    }
    
    public void restart(){
    	for(int i=0; i<numTargets; i++){
    		targets.get(i).setIsHit(false);
    	}
    	while(walls.size()>numWalls){
    		walls.remove(walls.size()-1);
    	}
    	numBullets = totBullets;
    	complete=false;
    }
    
    public void draw(Graphics g){
    	g.drawImage(background, 0, 0, null);
    	if(complete){
    		try {
 		    	Thread.sleep(500);
			} 
			catch (InterruptedException ie) {
    			Thread.currentThread().interrupt();
			}
    	}
    	for(int i=0; i<targets.size(); i++){
    		targets.get(i).draw(g);
    	}
    	for(int i=0; i<numBullets; i++){
    		g.drawImage(bullet, 60 + (i*30), 600, null);
    	}
    }
}