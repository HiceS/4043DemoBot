

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Drive {
	private Talon motorFL;
	private Talon motorFR;
	private Talon motorBL;
	private Talon motorBR;
	private Joystick jox;
	private Gyro gyro;
	private	PID pid;
	private double max;
	
	public Drive(Joystick jox, Gyro gyro) {
		this.jox = jox;
		this.gyro = gyro;
		
		pid = new PID(Config.P, Config.I, Config.D);
		
		motorFL = new Talon(0);
		motorFR = new Talon(1);
		motorBL = new Talon(2);
		motorBR = new Talon(3);
	}
	
	public void run(){
		double forward = -jox.getRawAxis(1); // push joystick1 forward to go forward
		double right = jox.getRawAxis(0); // push joystick1 to the right to strafe right
		double clockwise = jox.getRawAxis(2); // push joystick2 to the right to rotate clockwise 
		
		pid.update(gyro.getAngle(), Math.atan2(jox.getRawAxis(1),jox.getRawAxis(0)));
		
		fieldCentricSetSpeed(forward, right, clockwise, gyro.getAngle(), pid.getOutput());
	}
	
	private void fieldCentricSetSpeed(double forward, double right, double clockwise, double theta, double PIDCorrection) {
		theta = theta*180/Math.PI;
		double temp = forward*Math.cos(theta) + right*Math.sin(theta);
		right = -forward*Math.sin(theta) + right*Math.cos(theta);
		forward = temp; 
		
		setSpeed(forward, right, clockwise + PIDCorrection);
	}
	
	//max = Math.max(Math.max(Math.abs(rear_left), Math.abs(rear_right)), Math.max(Math.abs(front_left), Math.abs(front_left)));
	//max = Math.max(Math.max(rear_left, rear_right), Math.max(front_left, front_left));
	public void setSpeed(double forward, double right, double clockwise) {
		double front_right= motorFR.get();
		double front_left = motorFL.get();
		double back_left = motorBL.get();
		double back_right = motorBR.get();
		
		max = Math.abs(front_left);
		max = Math.max(Math.abs(front_right), max);
		max = Math.max(Math.abs(back_left), max);
		max = Math.max(Math.abs(back_right), max);
		
		if (max > 1) {
			front_left  /= max; 
			front_right /= max; 
			back_left   /= max; 
			back_right  /= max;
		} 
		
		motorFL.set(forward + clockwise + right);
		motorFR.set(forward - clockwise - right);
		motorBL.set(forward + clockwise - right);
		motorBR.set(forward - clockwise + right); 
	}

}
