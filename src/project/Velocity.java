package project;

public class Velocity {

	double dx,dy;
	
	@SuppressWarnings("unused")
	private Velocity(){};
	
	public Velocity(double a, double b){
		dx = a;
		dy = b;
	}
	
	public double getDX(){
		return dx;
	}
	public double getDY(){
		return dy;
	}
	public void setDX(double a){
		dx = a;
	}
	public void setDY(double b){
		dy = b;
	}
}
