package de.htwdd.robotics.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.htwdd.robotics.map.OccupancyGridMap;
import de.htwdd.robotics.pose.Pose;
import de.htwdd.robotics.range.laser.LaserRangeScan;


/**
 * Monte-Carlo localization.
 */
public class MonteCarloLocalization {
	
	/** The fixed amount of particles. */
	private final int count = 100;
	
	/** The random number generator. */
	private final Random random;
	
	/** The occupancy grid map of the environment. */
	private OccupancyGridMap map;
	
	/** The particles. */
	private List<Particle> particles;
	
	/** The estimated pose of the robot. */
	private Pose estimatedPose;
	
	/**
	 * Constructs a new Monte-Carlo localization.
	 */
	public MonteCarloLocalization() {
		random = new Random();
		particles = new ArrayList<Particle>(count);
	}
	
	/**
	 * @return A copy of the current particles.
	 */
	public List<Particle> getParticles() {
		return particles.stream().map(p -> new Particle(p.getPose(), p.getWeight())).collect(Collectors.toList());
	}
	
	/**
	 * Initializes the localization and returns an initial pose.
	 * 
	 * @param map The occupancy grid map of the environment.
	 * @param initialPose The initial pose.
	 */
	public void init(OccupancyGridMap map, Pose initialPose) {
		this.map = map;
		estimatedPose = initialPose;
		particles.clear();
		double weight = 1.0 / count;
		for (int i = 0; i < count; i++)
			particles.add(createRandomParticle(initialPose, weight));
	}
	
	/**
	 * Creates a particle whose pose is sampled according to a normal distribution around the given pose.
	 * 
	 * @param pose The pose to sample around.
	 * @param weight The weight of the particle.
	 * @return The randomly created particle.
	 */
	private Particle createRandomParticle(Pose pose, double weight) {
		double standardDeviation = 0.01;
		double x = pose.getX() + standardDeviation * random.nextGaussian();
		double y = pose.getY() + standardDeviation * random.nextGaussian();
		double phi = pose.getPhiRadians() + standardDeviation * random.nextGaussian();
		return new Particle(new Pose(x, y, phi), weight);
	}
	
	/**
	 * Updates the state and determines the current pose.
	 * 
	 * @param rotation The rotation in radians.
	 * @param translation The translation in meters.
	 * @param laserScan The current laser range scan.
	 * @return The current corrected pose.
	 */
	public Pose update(double rotation, double translation, LaserRangeScan laserScan) {
		// TODO replace the following line by a particle filter based localization algorithm
		// hint: start by moving each particle
		
		//List<Particle> myParticle =	getParticles();
		//Pose myPose= new Pose(rotation,translation,1);
		double tr,phi;
		
		if(Math.abs(translation) < 0.005 && Math.abs(rotation) < 0.005){
			return estimatedPose;
		}
		
		for(Particle p : particles) {
			tr = translation+ random.nextGaussian()*translation*0.05;
			phi=rotation+random.nextGaussian()*rotation*0.05;
			p.setPose(p.getPose().turn(phi).move(tr));
		}
		
		
		estimatedPose = estimatedPose.turn(rotation).move(translation); // this line will be removed in the end
		return estimatedPose;
	}
	
	/**
	 * Determines the most likely pose based on the normalized weighted particle set (the weights must sum up to one)
	 * by computing the weighted average of the particle poses.
	 * 
	 * @return The weighted average pose.
	 */
	private Pose computeAverageParticlePose() {
		double x = 0;
		double y = 0;
		double tx = 0;
		double ty = 0;
		for (Particle particle : particles) {
			x += particle.getWeight() * particle.getPose().getX();
			y += particle.getWeight() * particle.getPose().getY();
			tx += particle.getWeight() * particle.getPose().getPhi().getCosine();
			ty += particle.getWeight() * particle.getPose().getPhi().getSine();
		}
		return new Pose(x, y, Math.atan2(ty, tx));
	}
}
