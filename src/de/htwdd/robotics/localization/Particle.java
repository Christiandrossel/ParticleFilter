package de.htwdd.robotics.localization;

import de.htwdd.robotics.pose.Pose;


/**
 * A particle that is used within a particle filter.
 */
public class Particle {
	
	/** The pose represented by this particle. */
	private Pose pose;
	
	/** The importance factor of this particle. */
	private double weight;
	
	/**
	 * Constructs a copy of another particle with a weight of one.
	 * 
	 * @param other The particle that should be copied (excluding its weight).
	 */
	public Particle(Particle other) {
		pose = other.pose;
		weight = 1;
	}
	
	/**
	 * Constructs a new particle with a weight of one.
	 * 
	 * @param pose The pose represented by this particle.
	 */
	public Particle(Pose pose) {
		this(pose, 1);
	}
	
	/**
	 * Constructs a new particle.
	 * 
	 * @param pose The pose represented by this particle.
	 * @param weight The importance factor of this particle.
	 */
	public Particle(Pose pose, double weight) {
		this.pose = pose;
		this.weight = weight;
	}
	
	/**
	 * Creates a copy of this particle with a weight of one.
	 * 
	 * @return A new particle with the same pose as this particle and a weight of one.
	 */
	public Particle createUnweightedCopy() {
		return new Particle(this);
	}
	
	/**
	 * @return The pose represented by this particle.
	 */
	public Pose getPose() {
		return pose;
	}
	
	/**
	 * Changes the pose represented by this particle.
	 * 
	 * @param pose The new pose.
	 */
	public void setPose(Pose pose) {
		this.pose = pose;
	}
	
	/**
	 * @return The importance factor of this particle.
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Changes the importance factor of this particle.
	 * 
	 * @param weight The new importance factor.
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
