package project;

public class Acceleration {

	double d2x,dy2;
	
	@SuppressWarnings("unused")
	private Acceleration(){};
	
	public Acceleration(double a, double b){
		d2x = a;
		dy2 = b;
	}
	
	public double getD2X(){
		return d2x;
	}
	public double getDY2(){
		return dy2;
	}
	public void setD2X(double a){
		d2x = a;
	}
	public void setDY2(double b){
		dy2 = b;
	}
}
