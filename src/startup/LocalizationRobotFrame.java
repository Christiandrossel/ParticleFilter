package startup;

import de.htwdd.robotics.localization.ParticlesPlugin;
import de.htwdd.robotics.sim2d.SimulatedRobotFrame;


/**
 * Frame of a simulated robot that is used for localization.
 */
public class LocalizationRobotFrame extends SimulatedRobotFrame {
	
	/** The default serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a frame of a simulated robot that is used for localization.
	 * 
	 * @param robot The simulated robot that is used for localization.
	 */
	public LocalizationRobotFrame(LocalizationRobot robot) {
		super(robot, "res/gui.properties");
		getEnvironmentPanel().setScale(20);
		getEnvironmentPanel().addPlugin(new ParticlesPlugin(robot.getParticlesProvider(), 0.02));
	}
}
