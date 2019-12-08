package startup;

import java.awt.geom.Point2D;

import de.htwdd.robotics.sim2d.Environment;
import de.htwdd.robotics.sim2d.StaticObstacle;

/**
 * A simple environment consisting of corridors.
 * 
 * @author Peter Poschmann
 */
public class CorridorEnvironment extends Environment {
	
	/**
	 * Constructs a new corridor environment.
	 */
	public CorridorEnvironment() {
		
		StaticObstacle outerWalls = new StaticObstacle();
		outerWalls.addVertex(new Point2D.Double( -2.0, -15.0));
		outerWalls.addVertex(new Point2D.Double( -2.0,  -5.0));
		outerWalls.addVertex(new Point2D.Double( -2.2,  -5.0));
		outerWalls.addVertex(new Point2D.Double( -2.2,  -3.5));
		outerWalls.addVertex(new Point2D.Double( -2.0,  -3.5));
		outerWalls.addVertex(new Point2D.Double( -2.0,   7.0));
		outerWalls.addVertex(new Point2D.Double( -2.2,   7.0));
		outerWalls.addVertex(new Point2D.Double( -2.2,   8.5));
		outerWalls.addVertex(new Point2D.Double( -2.0,   8.5));
		outerWalls.addVertex(new Point2D.Double( -2.0,  15.0));
		outerWalls.addVertex(new Point2D.Double( 12.0,  15.0));
		outerWalls.addVertex(new Point2D.Double( 12.0, -10.0));
		outerWalls.addVertex(new Point2D.Double(  8.0, -10.0));
		outerWalls.addVertex(new Point2D.Double(  8.0, -15.0));
		addObstacle(outerWalls);
		
		StaticObstacle upperInnerWalls = new StaticObstacle();
		upperInnerWalls.addVertex(new Point2D.Double(  2.0,  13.0));
		upperInnerWalls.addVertex(new Point2D.Double(  2.0,  10.5));
		upperInnerWalls.addVertex(new Point2D.Double(  2.2,  10.5));
		upperInnerWalls.addVertex(new Point2D.Double(  2.2,   9.0));
		upperInnerWalls.addVertex(new Point2D.Double(  2.0,   9.0));
		upperInnerWalls.addVertex(new Point2D.Double(  2.0,   6.0));
		upperInnerWalls.addVertex(new Point2D.Double(  6.0,   6.0));
		upperInnerWalls.addVertex(new Point2D.Double(  6.0,   2.0));
		upperInnerWalls.addVertex(new Point2D.Double( 10.0,   2.0));
		upperInnerWalls.addVertex(new Point2D.Double( 10.0,  13.0));
		addObstacle(upperInnerWalls);
		
		StaticObstacle lowerInnerWalls = new StaticObstacle();
		lowerInnerWalls.addVertex(new Point2D.Double( 10.0,  -2.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  6.0,  -2.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  6.0,  -6.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.0,  -6.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.0,  -8.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.2,  -8.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.2,  -9.5));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.0,  -9.5));
		lowerInnerWalls.addVertex(new Point2D.Double(  2.0, -13.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  6.0, -13.0));
		lowerInnerWalls.addVertex(new Point2D.Double(  6.0, -8.0));
		lowerInnerWalls.addVertex(new Point2D.Double( 10.0, -8.0));
		addObstacle(lowerInnerWalls);

		StaticObstacle upperPillar = new StaticObstacle();
		upperPillar.addVertex(new Point2D.Double(2.0, 2.0));
		upperPillar.addVertex(new Point2D.Double(2.0, 2.5));
		upperPillar.addVertex(new Point2D.Double(2.5, 2.5));
		upperPillar.addVertex(new Point2D.Double(2.5, 2.0));
		addObstacle(upperPillar);

		StaticObstacle lowerPillar = new StaticObstacle();
		lowerPillar.addVertex(new Point2D.Double(2.0, -2.0));
		lowerPillar.addVertex(new Point2D.Double(2.0, -2.5));
		lowerPillar.addVertex(new Point2D.Double(2.5, -2.5));
		lowerPillar.addVertex(new Point2D.Double(2.5, -2.0));
		addObstacle(lowerPillar);
	}
}
