package de.htwdd.robotics.localization;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.htwdd.robotics.gui.environment.EnvironmentPlugin;
import de.htwdd.robotics.pose.Pose;
import de.htwdd.robotics.state.State;
import de.htwdd.robotics.state.StateObserver;
import de.htwdd.robotics.state.container.StateProvider;


/**
 * Plug-in of the environment panel that displays particles.
 * 
 * @author Peter Poschmann
 */
public class ParticlesPlugin extends EnvironmentPlugin implements StateObserver<List<Particle>> {
	
	/** The provider of the particles. */
	private StateProvider<List<Particle>> particlesProvider;
	
	/** The current particles of the localization. */
	private List<Particle> currentParticles;
	
	/** Determines the increment of the index of the particles that should be painted. */
	private int currentIncrement = 1;
	
	/** The radius of the particles. */
	private double radius;
	
	/** The size in world coordinates needed to display the particles. */
	private Rectangle2D currentSize;
	
	/** The shape of the particles. */
	private Area shape;
	
	/** Marks the direction the robot is looking into. */
	private Line2D orientationMarker;
	
	/**
	 * Creates a new plug-in that displays particles.
	 * 
	 * @param poseHypothesesProvider The provider of the particles.
	 * @param radius The radius of the pose hypotheses.
	 */
	public ParticlesPlugin(StateProvider<List<Particle>> poseHypothesesProvider, double radius) {
		super("Particles");
		this.particlesProvider = poseHypothesesProvider;
		this.radius = radius;
		currentSize = new Rectangle2D.Double(0, 0, 0, 0);
		shape = new Area(new Ellipse2D.Double(-radius, -radius,
				radius + radius, radius + radius));
		orientationMarker = new Line2D.Double(0.0, 0.0, 3 * radius, 0.0);
		currentParticles = Collections.emptyList();
	}
	
	@Override
	public JPanel getConfigurationPanel() {
		JLabel everyNthLabel = new JLabel("Show every nth particle:");
		JComboBox<Integer> everyNthChooser = new JComboBox<Integer>();
		everyNthChooser.addItem(1);
		everyNthChooser.addItem(2);
		everyNthChooser.addItem(5);
		everyNthChooser.addItem(10);
		everyNthChooser.addItem(20);
		everyNthChooser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Integer item = (Integer) e.getItem();
				currentIncrement = item.intValue();
			}
		});
		
		JPanel configPanel = new JPanel();
		configPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		configPanel.add(everyNthLabel);
		configPanel.add(everyNthChooser);
		return configPanel;
	}
	
	@Override
	protected void onActivate() {
		particlesProvider.addObserver(this);
	}
	
	@Override
	protected void onDeactivate() {
		particlesProvider.addObserver(this);
	}
	
	@Override
	protected void paint(Graphics2D g) {
		List<Particle> hypotheses = currentParticles;
		int increment = currentIncrement;
		if (!hypotheses.isEmpty()) {
			double zeroHue = 0.0 / 360.0;
			double maxHue = 120.0 / 360.0;
			double maxWeight = hypotheses.get(hypotheses.size() - 1).getWeight();
			AffineTransform backup = g.getTransform();
			for (int i = 0; i < hypotheses.size(); i += increment) {
				Particle particle = hypotheses.get(i);
				Pose pose = particle.getPose();
				float hue = (float)(zeroHue + (maxHue - zeroHue) * (particle.getWeight() / maxWeight));
				g.setColor(Color.getHSBColor(hue, 1.f, 0.8f));
				g.translate(pose.getX(), pose.getY());
				g.rotate(pose.getPhiRadians());
				g.draw(orientationMarker);
				g.draw(shape);
				g.setTransform(backup);
			}
		}
	}
	
	@Override
	public void stateChanged(final State<List<Particle>> newState) {
		runInBackground(new Runnable() {
			@Override
			public void run() {
				double xMin = Double.POSITIVE_INFINITY;
				double xMax = Double.NEGATIVE_INFINITY;
				double yMin = Double.POSITIVE_INFINITY;
				double yMax = Double.NEGATIVE_INFINITY;
				List<Particle> particles = newState.getData();
				Collections.sort(particles, new Comparator<Particle>() {
					@Override
					public int compare(Particle o1, Particle o2) {
						return Double.compare(o1.getWeight(), o2.getWeight());
					}
				});
				for (Particle particle : particles) {
					Pose pose = particle.getPose();
					xMin = Math.min(xMin, pose.getX());
					xMax = Math.max(xMax, pose.getX());
					yMin = Math.min(yMin, pose.getY());
					yMax = Math.max(yMax, pose.getY());
				}
				Rectangle2D oldSize = currentSize;
				Rectangle2D newSize = new Rectangle2D.Double(
						xMin - radius, yMin - radius, xMax - xMin + 2 * radius, yMax - yMin + 2 * radius);
				if (particles instanceof RandomAccess)
					currentParticles = particles;
				else
					currentParticles = new ArrayList<Particle>(particles);
				currentSize = newSize;
				repaint(oldSize);
				repaint(newSize);
			}
		});
	}
}
