package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** README 
 * There is a fundamental flaw in this
 * The bullets are fired based on a polar coordinate system
 * And all objects are painted based on this
 * However, in calculating collisions, one must convert to a common system
 * Not on where bullets are rotated around certain points
 * Thus it is difficult, not to mention imprecise, 
 * And very hard to debug
 * 
 * Also, delete out of bounds is messed up b/c the x,y coordinates inside bullets/missiles are not accurate
 * x corresponds to the r polar coordinate
 * y = 0
 * 
 * Currently, it seems to be working. However if much future development in the future...
 * You've been warned
 * 
 * See print statements in checkCollisions()
 * **/

@SuppressWarnings("serial")
public class Environment extends JPanel implements ActionListener {

	final static int WINDOW_SIZE = 500;
	final static int BASE_LOCATION = WINDOW_SIZE / 2 - 25;
	final static int FIRE_RATE = 1; // in seconds
	ImageIcon background;
	Base homeBase;
	Base enemyBase;
	ArrayList<Bullet> bullets;
	ArrayList<Bullet> missiles;
	ArrayList<Rectangle> targets;
	javax.swing.Timer guiTimer;
	javax.swing.Timer targetPicker;
	
	public Environment(){
		// set layout
		setSize(WINDOW_SIZE,WINDOW_SIZE);

		// instantiate components 
		background = new ImageIcon("images/background.jpg");
		homeBase = new Base(this,false);
		homeBase.setPosition(new Position(BASE_LOCATION,WINDOW_SIZE-70));
		enemyBase = new Base(this,true);
		enemyBase.setPosition(new Position(0,BASE_LOCATION));
		bullets = new ArrayList<Bullet>();
		missiles = new ArrayList<Bullet>();
		targets = new ArrayList<Rectangle>();
		guiTimer = new javax.swing.Timer(5,this);
		targetPicker = new javax.swing.Timer(1000 * FIRE_RATE,this);
		
		// add action listeners
		addMouseListener(new MyMouseListener());
		
		startAnimation();
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		Environment e = new Environment();
		f.add(e);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(WINDOW_SIZE,WINDOW_SIZE);
		f.setVisible(true);
		f.setResizable(false);
		f.addKeyListener(e.new MyKeyListener());

	}
	public void addBullet(Bullet b){
		bullets.add(b);
	}
	public void addMissile(Bullet b){
		missiles.add(b);
	}
	
	public void moveBullets(){
		for (Bullet b : bullets){
			double newX = b.getPosition().getX() + b.getVelocity().getDX();
			double newY = b.getPosition().getY() + b.getVelocity().getDY();
			b.setPosition(new Position(newX,newY));
		}
		for (Bullet b : missiles){
			double newX = b.getPosition().getX() + b.getVelocity().getDX();
			double newY = b.getPosition().getY() + b.getVelocity().getDY();
			b.setPosition(new Position(newX,newY));
		}		
	}
	
	public void startAnimation(){
		guiTimer.start();
		//targetPicker.start();
	}
	
	public void checkCollisions(){
		
		// Bullets fly based on polar coordinate system (rotation and flies linearly)
		// Targets are based on Cartesian coordinate system
		// Convert bullet to Cartesian system for comparison and collision check
		
		for (int i=0;i<missiles.size();i++){
			// Polar coordinates
			double mRadAngle = Math.toRadians(missiles.get(i).getRotation());
			double mR = missiles.get(i).getPosition().getX();
			// Cartesian coordinates 
			double mX = mR * Math.cos(mRadAngle);
			double mY = BASE_LOCATION + mR * Math.sin(mRadAngle);
			
			System.out.println("Missile Location: (" + mX + "," + mY + ")");
			Rectangle trueM = new Rectangle((int)mX,(int)mY,20,20);
						
			for (int j=0;j<targets.size();j++){
				if (trueM.intersects(targets.get(j))){
					
					long impactTime = System.currentTimeMillis();
					long travelTime = impactTime - missiles.get(i).getFireTime();
					System.out.println("Travel Time: " + travelTime);
					
					missiles.remove(i);
					targets.remove(j);
				}
			}
			
			for (int k=0;k<bullets.size();k++){
				// Polar Coordinates
				double bRadAngle = Math.toRadians(bullets.get(k).getRotation());
				double bR = bullets.get(k).getPosition().getX() - 225;
				// Cartesian Coordinates
				
				double bX = BASE_LOCATION + bR * Math.cos(bRadAngle);
				double bY = (WINDOW_SIZE-70) + bR * Math.sin(bRadAngle);
				
				System.out.println("Bullet Location: (" + bX + "," + bY + ")");
				Rectangle trueB = new Rectangle((int)bX,(int)bY,20,20);
				
				if (trueM.intersects(trueB)){
					missiles.remove(i);
					bullets.remove(k);
				}
			}
		}
	}
	
	public void deleteOutOfBounds(){
		for (int i=0;i<bullets.size();i++){
			Position pos = bullets.get(i).getPosition();
			if (pos.getX() > WINDOW_SIZE || pos.getX() < 0 || pos.getY() > WINDOW_SIZE || pos.getY() < 0){
				bullets.remove(i);
			}
		}
		for (int i=0;i<missiles.size();i++){
			Position pos = missiles.get(i).getPosition();
			if (pos.getX() > WINDOW_SIZE || pos.getX() < 0 || pos.getY() > WINDOW_SIZE || pos.getY() < 0){
				missiles.remove(i);
			}
		}
	}
	
	public synchronized void actionPerformed(ActionEvent ae){
		if (ae.getSource() == guiTimer){
			moveBullets();
			checkCollisions();
			deleteOutOfBounds();
			repaint();			
		} else if (ae.getSource() == targetPicker){
			// pick a position
			Random r = new Random();
			double x = r.nextDouble() * 1000 % WINDOW_SIZE;
			double y = r.nextDouble() * 1000 % WINDOW_SIZE;
			Position target = new Position(x,y);
			System.out.println(target);
			targets.add(new Rectangle((int)target.getX(),(int)target.getY(),10,10));
			enemyBase.fire(target);
		}
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;		
		// draw background
		g2.drawImage(background.getImage(), 0, 0, null);
		// draw bases
		g2.drawImage(homeBase.getImage(),(int)homeBase.getPosition().getX(), (int)homeBase.getPosition().getY(),50,50,null);
		g2.drawImage(enemyBase.getImage(),(int)enemyBase.getPosition().getX(), (int)enemyBase.getPosition().getY(),50,50,null);
		// draw missiles 
		for (Bullet b : missiles){
			g2.rotate(Math.toRadians(b.getRotation()), enemyBase.getPosition().getX(), enemyBase.getPosition().getY());
			g2.drawImage(b.getImage(), (int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)b.getWidth(), (int)b.getHeight(), null);
			g2.rotate(-Math.toRadians(b.getRotation()), enemyBase.getPosition().getX(), enemyBase.getPosition().getY());
		}
		// draw bullets 
		for (Bullet b : bullets){
			g2.rotate(Math.toRadians(b.getRotation()), homeBase.getPosition().getX(), homeBase.getPosition().getY());
			g2.drawImage(b.getImage(), (int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)b.getWidth(), (int)b.getHeight(), null);
			g2.rotate(-Math.toRadians(b.getRotation()), homeBase.getPosition().getX(), homeBase.getPosition().getY());
		}
		// draw targets 
		for (Rectangle r : targets){
			g2.fill(r);
		}
		
	}
	
	public void printTime(){
		printDate();
		// to format milli for easy reading 
		System.out.print("\t");
		printMilli();
	}
	public void printDate(){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		System.out.println(c.getTime());
	}
	public void printMilli(){
		System.out.println(System.currentTimeMillis());
	}

	// MOUSE LISTENER
    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent me){ //press and release
        	
        	Position target = new Position(me.getPoint().getX(),me.getPoint().getY());
			targets.add(new Rectangle((int)target.getX(),(int)target.getY(),10,10));
			enemyBase.fire(target);
        	
        }
        @Override
        public void mouseEntered(MouseEvent e){} //enter component
        @Override
        public void mouseExited(MouseEvent e){} //leave component                                                
        @Override
        public void mousePressed(MouseEvent e){} //press
        @Override
        public void mouseReleased(MouseEvent e){}; //release
    }
    // KEY LISTENER
    private class MyKeyListener implements KeyListener {
        @Override
        public void keyPressed (KeyEvent ke){

        	switch(ke.getKeyCode()){
            case KeyEvent.VK_SPACE: 
            	
            	Position target = new Position(homeBase.getPosition().getX(),0);
    			//targets.add(new Rectangle((int)target.getX(),(int)target.getY(),10,10));
    			homeBase.fire(target);
            	break;
            	
            default: break;
            }
        }
        @Override
        public void keyTyped(KeyEvent event){
        }
        @Override
        public void keyReleased(KeyEvent event){
        }
    }
}
