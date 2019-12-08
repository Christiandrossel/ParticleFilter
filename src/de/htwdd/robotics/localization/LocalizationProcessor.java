package de.htwdd.robotics.localization;

import java.util.List;

import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.map.container.GridMapProvider;
import de.htwdd.robotics.pose.Pose;
import de.htwdd.robotics.pose.RobotPose;
import de.htwdd.robotics.processor.SyncProcessor2;
import de.htwdd.robotics.range.laser.LaserRangeScan;
import de.htwdd.robotics.state.State;
import de.htwdd.robotics.state.container.StateContainer;
import de.htwdd.robotics.state.container.StateContainers;
import de.htwdd.robotics.state.container.StateProvider;


/**
 * Processor that is used for the localization of the robot.
 */
public class LocalizationProcessor extends SyncProcessor2<LaserRangeScan, RobotPose> {
	
	/** The provider of the occupancy grid map. */
	private GridMapProvider<? extends OccupancyGridMap> occupancyMapProvider;
	
	/** The container of the corrected pose. */
	private StateContainer<RobotPose> correctedRobotPoseContainer;
	
	/** The container of the particles. */
	private StateContainer<List<Particle>> particlesContainer = StateContainers.single();
	
	/** The occupancy grid map. */
	private OccupancyGridMap occupancyMap;
	
	/** The actual Monte-Carlo localization. */
	private MonteCarloLocalization localization;
	
	/** The initial pose. */
	private Pose initialPose;
	
	/** The odometry based pose of the previous call to {@link #onData(State, State)}. */
	private Pose previousPose;
	
	/**
	 * Constructs a new localization processor.
	 * 
	 * @param initialPose The initial pose.
	 * @param localization The actual Monte-Carlo localization.
	 * @param robotPoseProvider The provider of the robot pose determined using raw odometry.
	 * @param laserScanProvider The provider of the laser range scan.
	 * @param occupancyMapProvider The provider of the occupancy grid map.
	 * @param correctedRobotPoseContainer The container of the corrected pose.
	 */
	public LocalizationProcessor(Pose initialPose,
			MonteCarloLocalization localization,
			StateProvider<RobotPose> robotPoseProvider,
			StateProvider<LaserRangeScan> laserScanProvider,
			GridMapProvider<? extends OccupancyGridMap> occupancyMapProvider,
			StateContainer<RobotPose> correctedRobotPoseContainer) {
		super("Localization", laserScanProvider, robotPoseProvider);
		if (initialPose == null || localization == null || occupancyMapProvider == null || correctedRobotPoseContainer == null)
			throw new IllegalArgumentException("The arguments must not be null");
		this.initialPose = initialPose;
		this.localization = localization;
		this.occupancyMapProvider = occupancyMapProvider;
		this.correctedRobotPoseContainer = correctedRobotPoseContainer;
	}
	
	@Override
	protected void onStart() {
		previousPose = null;
		correctedRobotPoseContainer.clear();
		occupancyMap = occupancyMapProvider.get();
		if (occupancyMap == null)
			throw new NullPointerException("Map must not be null");
	}
	
	@Override
	protected void onData(State<LaserRangeScan> primary, State<RobotPose> secondary) {
		RobotPose uncorrectedPose = secondary.getData();
		Pose currentPose = uncorrectedPose.getPose();
		RobotPose correctedPose = null;
		if (previousPose == null) {
			localization.init(occupancyMap, initialPose);
			correctedPose = new RobotPose(uncorrectedPose.getConfiguration(), initialPose, uncorrectedPose.getMileage());
		} else {
			Pose movement = previousPose.differenceTo(currentPose);
			double rotation = movement.getPhiRadians();
			double translation = Math.signum(movement.getX()) * movement.getDistanceToOrigin();
			LaserRangeScan laserScan = primary.getData();
			Pose pose = localization.update(rotation, translation, laserScan);
			correctedPose = new RobotPose(uncorrectedPose.getConfiguration(), pose, uncorrectedPose.getMileage());
		}
		correctedRobotPoseContainer.add(State.of(primary.getTime(), correctedPose));
		particlesContainer.add(State.of(primary.getTime(), localization.getParticles()));
		previousPose = currentPose;
	}
	
	/**
	 * @return The provider of the corrected pose.
	 */
	public StateProvider<RobotPose> getPoseProvider() {
		return correctedRobotPoseContainer;
	}
	
	/**
	 * @return The provider of the particles.
	 */
	public StateProvider<List<Particle>> getParticlesProvider() {
		return particlesContainer;
	}
}
