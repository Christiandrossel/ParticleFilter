package startup;

import java.io.IOException;
import java.util.List;

import de.htwdd.robotics.localization.LocalizationProcessor;
import de.htwdd.robotics.localization.MonteCarloLocalization;
import de.htwdd.robotics.localization.Particle;
import de.htwdd.robotics.pose.Pose;
import de.htwdd.robotics.pose.RobotPose;
import de.htwdd.robotics.sim2d.SimulatedRobot;
import de.htwdd.robotics.state.container.StateContainers;
import de.htwdd.robotics.state.container.StateProvider;


/**
 * Simulated robot that is used for localization.
 */
public class LocalizationRobot extends SimulatedRobot {
	
	/** The localization. */
	private final LocalizationProcessor localization;
	
	/**
	 * Constructs a new simulated robot that is used for localization.
	 * 
	 * @throws IOException If the occupancy grid map could not be loaded.
	 */
	public LocalizationRobot() throws IOException {
		setEnvironment(new CorridorEnvironment());
		getSimulation().velocityNoise = 0.2;
		getSimulation().scanRangeNoise = 0.005;
		getSimulation().spuriousMeasurements = true;
		getSimulation().initialPose = Pose.ZERO;
		loadOccupancyMap("res/map.png", 0.1, 22, 150);
		localization = addProcessor(new LocalizationProcessor(getSimulation().initialPose,
				new MonteCarloLocalization(), getRawPoseProvider(), getLaserScanProvider(),
				getOccupancyMapProvider(), StateContainers.<RobotPose>fixedPeriod(1500)));
	}
	
	@Override
	public StateProvider<RobotPose> getPoseProvider() {
		return localization.getPoseProvider();
	}
	
	/**
	 * @return The provider of the particles.
	 */
	public StateProvider<List<Particle>> getParticlesProvider() {
		return localization.getParticlesProvider();
	}
}
