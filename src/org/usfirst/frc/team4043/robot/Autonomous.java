package org.usfirst.frc.team4043.robot;

import edu.wpi.first.wpilibj.Timer;

public class Autonomous {
	private Drive drive;
	private Timer timer;
	
	public Autonomous(Drive drive) {
		this.drive = drive;
	}
	
	public void init() {
		timer.reset();
		timer.start();
	}
	
	public void dance() {
		
		if(timer.get() <= 10) { //timer is in seconds 
			drive.setSpeed(0,0,1); //rotate for dayz
		}
		
		else {
			drive.setSpeed(0, 0, 0);
		}
	}
}
