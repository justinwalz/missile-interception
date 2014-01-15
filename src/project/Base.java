package project;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Base {
	
	Environment e;
	Position p;
	ImageIcon image;
	boolean isEnemy;
	
	@SuppressWarnings("unused")
	private Base(){};
	
	public Base(Environment parent, boolean ie){
		e = parent;
		isEnemy = ie;
		p = new Position(0,0);
		image = new ImageIcon("images/base.png");			
	}
	
	public Position getPosition(){
		return p;
	}
	public void setPosition(Position a){
		p = a;
	}
	public Image getImage(){
		return image.getImage();
	}
	
	public void fire(Position target){
		Bullet b = new Bullet();
		
		// set position, velocity and rotation
		b.setPosition(p);
		b.setVelocity(new Velocity(1,0));
		double opp = target.getX() - p.getX(); // dx
		double adj = target.getY() - p.getY(); // dy
		double rotation = Math.toDegrees(Math.atan(adj/opp));
		b.setRotation(rotation);
		// add to the environment
		if (isEnemy){
			System.out.println("Rotation of missile: " + rotation);
			e.addMissile(b);
		} else {
			System.out.println("Rotation of bullet: " + rotation);
			System.out.println("Position of bullet: " + b.getPosition());
			e.addBullet(b);
		}
		
		// calculations
		double distance = Math.sqrt( Math.pow(opp, 2) + Math.pow(adj, 2));
		System.out.println("Distance to targ: " + distance);
		
	}
}
