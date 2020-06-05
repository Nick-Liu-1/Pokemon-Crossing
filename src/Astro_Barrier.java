import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.applet.*;

class Astro_Barrier extends JPanel implements KeyListener {
	private Rocket rocket;
	private boolean [] keys;
	private boolean restarting = false;
	private ArrayList<Level> allLevels = new ArrayList<Level>();
	private int currentLevel, numLives, score;
	private Font font = new Font("Helvetica", Font.PLAIN, 30);
	
	private Image endScreen = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/endScreen.png").getImage();
	private Image rocketPic = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/rocketLives.png").getImage();
	private Image restartScreen = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/restartScreen.png").getImage();
	private Point mouse = new Point(0, 0);
	private Main mainframe;
	
	private File pewWav = new File("Assets/Sounds/Pew.wav");	//for when the rocket shoots
	private AudioClip pewSFX;
	
	public Astro_Barrier(Main m){
		mainframe = m;
		addKeyListener(this);
        addMouseListener(new clickListener());
		setSize(1020,695);
	}
	
	public void init(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		rocket = new Rocket(510, 548);

		currentLevel=0;
		numLives=3;
		score=0;
		
		//getting level info
		try{
            Scanner levelsFile = new Scanner(new BufferedReader(new FileReader("Assets/Minigames/Astro Barrier/Levels/allLevels.txt")));
            int numLevels = Integer.parseInt(levelsFile.nextLine());
            String[] line;
            for (int i = 0 ; i < numLevels; i++) {
                line = levelsFile.nextLine().split(" ");
                allLevels.add(new Level(i+1, new ImageIcon("Assets/Minigames/Astro Barrier/Levels/"+line[0]).getImage(), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3])));
                //adding walls to each level:
                for (int j=0; j<(Integer.parseInt(line[3]))*4; j+=4){
            		allLevels.get(allLevels.size()-1).addWall(new Rectangle(Integer.parseInt(line[3+j+1]), Integer.parseInt(line[3+j+2]), Integer.parseInt(line[3+j+3]), Integer.parseInt(line[3+j+4])));
            	}
            }
		}
        catch(FileNotFoundException e) {
            System.out.println("levels file not found");
        }
        
        try{
			pewSFX = Applet.newAudioClip(pewWav.toURL());
		}
		catch(Exception e){
			System.out.println("Can't find sound");
		}
	}
	
	public void addNotify(){
        super.addNotify();
        requestFocus();
    }
	
	public void move(){
		rocket.moveBullet();
		allLevels.get(currentLevel).moveTargets();
		if(keys[KeyEvent.VK_D] ){
			rocket.move(2);
		}
		if(keys[KeyEvent.VK_A] ){
			rocket.move(-2);
		}
		if(keys[KeyEvent.VK_SPACE]){
			if(allLevels.get(currentLevel).getNumBullets() > 0){
				pewSFX.play();
				rocket.shoot();
			}
		}
    }
    
    public void checkCollisions(){
    	if(rocket.getIsShooting()){
    		Rectangle bullet = new Rectangle(rocket.getBulletX(), rocket.getBulletY(), 1, 1);
    		for(int i=0; i<allLevels.get(currentLevel).getWalls().size(); i++){
    			if (bullet.intersects(allLevels.get(currentLevel).getWalls().get(i))){	
    				rocket.setIsShooting(false);					//if a bullet collides with a wall, stops bullet
    				allLevels.get(currentLevel).removeBullet();		
    			}
    		}
    		
    		//checking bullet collions with targets:
    		for(int i=0; i<allLevels.get(currentLevel).getTargets().size(); i++){
    			Target target = allLevels.get(currentLevel).getTargets().get(i);
    			Rectangle tmp = new Rectangle(target.getX()-(target.getLength()/2), target.getY()-(target.getWidth()/2), target.getLength(), target.getWidth());
    			if (bullet.intersects(tmp) && target.getIsHit()==false){
    				rocket.setIsShooting(false);
    				allLevels.get(currentLevel).removeBullet();
    				allLevels.get(currentLevel).addWall(tmp);	//once target is hit it becomes a wall, must be added to array of walls for the level
    				allLevels.get(currentLevel).getTargets().get(i).setIsHit(true);
    				//number of points depends on size of target:
    				if(target.getSize()==0){
    					score += 15;
    				}
    				else if(target.getSize()==1){
    					score += 10;
    				}
    				else if(target.getSize()==2){
    					score += 5;
    				}
    			}
    		}
    	}
    }
    
    public void checkComplete(){
		if(allLevels.get(currentLevel).getComplete() && currentLevel<(allLevels.size()-1)){	//moves on if level is complete
			currentLevel++;
		}
		if(allLevels.get(currentLevel).getNumBullets() <= 0){	//lose life when out of bullets
			numLives--;
		}
		//restarting game:
		if(numLives<=0){
			for(int i=0; i<allLevels.size(); i++){
				score -= allLevels.get(i).restart();
			}
			numLives=3;
			currentLevel=0;
			score=0;
			restarting=true;
		}
		//restarting level:
		else if (allLevels.get(currentLevel).getNumBullets() <= 0 && numLives>0){
			score -= allLevels.get(currentLevel).restart();	//subtracts points gained from failed level
		}
	}
	
	public void paint(Graphics g){
		if(allLevels.get(currentLevel).getComplete() && currentLevel==(allLevels.size()-1)){
			g.drawImage(endScreen, 0, 0, null);
		}
		else{
			allLevels.get(currentLevel).draw(g);
			rocket.draw(g);
			for(int i=0; i<numLives; i++){
				g.drawImage(rocketPic, 750 + (i*60), 610, null);
			}
		}

		if(restarting){
			g.drawImage(restartScreen, 0, 0, null);
			try {
 		    	Thread.sleep(1000);
			} 
			catch (InterruptedException ie) {
    			Thread.currentThread().interrupt();
			}
			restarting=false;
		}
		g.setColor(new Color(255, 255, 255)); 
		g.setFont(font);
		g.drawString("Score: " + score, 750, 45);
		g.drawString("Level " + (currentLevel+1), 200, 45);
    }
    
    public void keyTyped(KeyEvent e) {}
    
	public void keyPressed(KeyEvent e) {
	    keys[e.getKeyCode()] = true;
	}	
		    
	public void keyReleased(KeyEvent e) {
	    keys[e.getKeyCode()] = false;
	}
	
    class clickListener implements MouseListener{
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	    public void mouseReleased(MouseEvent e) {}
	    public void mouseClicked(MouseEvent e){
	    	if (e.getButton() == MouseEvent.BUTTON1) {
	    		if (new Rectangle(25, 16, 60, 38).contains(mouse)) {
	    			mainframe.changeGame("game");
				}
			}
		}
	    public void mousePressed(MouseEvent e){
	    	Point mousePos = MouseInfo.getPointerInfo().getLocation();
			Point offset = getLocationOnScreen();
			mouse = new Point (mousePos.x-offset.x, mousePos.y-offset.y);
	    }
    }
		
}



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
        totBullets = numBullets;	//used to reset level
        this.numWalls = numWalls;
        bullet = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/bullet.png").getImage();
        
        //ADDING TARGETS:
        try{
            Scanner levelFile = new Scanner(new BufferedReader(new FileReader("Assets/Minigames/Astro Barrier/Levels/Level"+levelNum+".txt")));
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
    	int count=0;	//coutns number of hit targets in the level
    	for(int i=0; i<numTargets; i++){
    		if(targets.get(i).getIsHit()){
    			count++;
    		}
    		targets.get(i).move();
    	}
    	if(count==numTargets){	//level complete when all targets are hit
    		complete=true;
    	}
    }
    
    public int restart(){
    	int scoreChange = 0;	//subtracted from total score when level is failed
    	for(int i=0; i<numTargets; i++){
    		if(targets.get(i).getIsHit()){
    			if(targets.get(i).getSize() == 0){
    				scoreChange += 15;
    			}
    			else if(targets.get(i).getSize() == 1){
    				scoreChange += 10;
    			}
    			else if(targets.get(i).getSize() ==2){
    				scoreChange += 5;
    			}
    		}
    		targets.get(i).restart();
    	}
    	while(walls.size()>numWalls){		//removes all the hit targets from the arraylist of walls that were added later
    		walls.remove(walls.size()-1);	//leaves only the orginal number of walls
    	}
    	numBullets = totBullets;
    	complete=false;
    	return scoreChange;
    }
    
    public void draw(Graphics g){
    	g.drawImage(background, 0, 0, null);
    	if(complete){
    		try {
 		    	Thread.sleep(1000);	//slows down transitions
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



class Rocket{
	private int x, y, bulletX, bulletY;
	private boolean isShooting;
    private Image rocketPic, bulletPic;
    
    public Rocket(int x, int y){
    	this.x = x;
        this.y = y;
        bulletX = -1;
        bulletY = -1;
        rocketPic = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/rocket.png").getImage();
        bulletPic = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/bullet.png").getImage();
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
	public int getBulletX(){return bulletX;}
    public int getBulletY(){return bulletY;}
    public boolean getIsShooting(){return isShooting;}
    public void setIsShooting(boolean x){isShooting=x;}

    public void move(int n){
    	if(x+n > 72 && x+n < 950){
    		x+=n;
    	}
    }
    
    public void shoot(){
    	if(isShooting==false){
    		isShooting=true;
    		bulletX=x;	//starts bullet at same pos as rocket
    		bulletY=y;
    	}
    }
    
    public void moveBullet(){
    	bulletY-=7;
    }
    
    public void draw(Graphics g){
    	g.drawImage(rocketPic, x-50, y-29, null);
    	if(isShooting){
    		g.drawImage(bulletPic, bulletX-10, bulletY-20, null);
    	}
    }
}


class Target{
	private int x, y, size, length, width, startx, starty;
	private ArrayList<Point> pos = new ArrayList<Point>();		//list of all turning points for the target
	private int nextPos;
    private Image targetPic, targetPicHit;
    private boolean isHit =false;
    private int speed;
    public static final int FORWARD = 1, BACKWARD = -1;
    private int dir, startDir;
    
    public Target(int size, int x, int y, int nextPos, int dir){
    	this.x = x;
        this.y = y;
        startx=x;
        starty=y;
        this.size = size;
        this.nextPos = nextPos;
        this.dir = dir;
        startDir = dir;
        targetPic = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/target"+size+".png").getImage();
        targetPicHit = new ImageIcon("Assets/Minigames/Astro Barrier/Sprites/target"+size+" hit.png").getImage();
        if(size==0){
        	length = 50;
        	width=50;
        	speed = 6;
        }
        else if(size==1){
        	length=75;
        	width=75;
        	speed = 5;
        }
        else if(size==2){
        	length=120;
        	width=80;
        	speed = 4;
        }
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
    public int getSize(){return size;}
    public int getLength(){return length;}
    public int getWidth(){return width;}
   	public ArrayList<Point> getPoints(){return pos;}
   	public boolean getIsHit(){return isHit;}
   	public void setIsHit(boolean x){isHit=x;}
    
    public void addPos(Point newPos){
    	pos.add(newPos);
    }
    
    public void move(){
    	if(isHit == false){			
			if(nextPos<0){	//once target reaches first position it turns around
	    		dir = FORWARD;
	    		nextPos = 1;    		
	    	}
	    	else if(nextPos>=pos.size()){	//once target reaches last position, turns around again
	    		dir = BACKWARD;    
	    		nextPos = pos.size()-2;
	    	}
			
	    	x += pos.get(nextPos).getX() > x ? speed:0;
	    	y += pos.get(nextPos).getY() > y ? speed:0;
	    	x -= pos.get(nextPos).getX() < x ? speed:0;
	    	y -= pos.get(nextPos).getY() < y ? speed:0;
	    	
	    	if(Math.abs(pos.get(nextPos).getX()-x)<=speed && Math.abs(pos.get(nextPos).getY()-y)<=speed){	//once the "next position" is reached, moves target on to next position in arraylist
	    		nextPos += dir;
	    	}
    	}
    }
    
    public void restart(){
    	x = startx;
    	y = starty;
    	isHit = false;
    	dir = startDir;
    }
    
    public void draw(Graphics g){
    	if(isHit){
    		g.drawImage(targetPicHit, x-(length/2), y-(width/2), null);
    	}
    	else{
    		g.drawImage(targetPic, x-(length/2), y-(width/2), null);
    	}
    }
}