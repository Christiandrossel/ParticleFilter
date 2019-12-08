package startup;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


/**
 * Starts the simulated robot environment.
 * 
 * @author Peter Poschmann
 */
public class StartLocalizationApp {
	
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LocalizationRobot robot = new LocalizationRobot();
					LocalizationRobotFrame frame = new LocalizationRobotFrame(robot);
					frame.setSize(900, 600);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (IOException exc) {
					exc.printStackTrace();
				}
			}
		});
	}
}
