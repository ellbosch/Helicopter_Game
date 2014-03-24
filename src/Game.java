import java.awt.*;

import javax.swing.*;
public class Game implements Runnable {

	public void run() {
		// top-level frame
		final JFrame frame = new JFrame("Helicopter Game");
		frame.setLocation(Killzone.gameWidth, Killzone.gameHeight);

		// Main playing area
		final HeliMap map = new HeliMap();
		frame.add(map, BorderLayout.CENTER);

		// Put frame on screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Start running game
		map.reset();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}
