import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings("serial")
public class HeliMap extends JComponent {
	private Boundary boundary;
	private Helicopter helicopter;

	private int interval = 20;
	private Timer timer;
	private int counter;
	private int c;
	private int t;
	private int tDead;
	private int time;
	private int finalD;
	private int highScore;
	private boolean isClicked;
	private int vel0;

	final int gameWidth = Killzone.gameWidth;
	final int gameHeight = Killzone.gameHeight;
	final int tunnelStart = Killzone.tunnelStart;

	static boolean gameStart = false;
	static boolean isDead;
	static boolean hitsNorth;
	static boolean hitsSouth;
	static boolean hitsObstacle;

	public HeliMap() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);

		timer = new Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) { tick(); }});
		timer.start();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				gameStart = true;
				isClicked = true;
				Helicopter.tilt = true;
				vel0 = helicopter.velocityY;
				time = 0;
			}
			public void mouseReleased(MouseEvent e) {
				isClicked = false;
				Helicopter.tilt = false;
				vel0 = helicopter.velocityY;
				time = 0;
			}
		});
	}

	public void reset() {
		boundary = new Boundary();
		helicopter = new Helicopter();
		gameStart = false;
		tDead = 0;
		isDead = false;
		hitsNorth = false;
		hitsSouth = false;
		hitsObstacle = false;
		vel0 = 0;
		time = 0;
		counter = 0;
		requestFocusInWindow();
	}

	void tick() {

		if (gameStart) {
			
			// end game if helicopter hits boundary
			int yHeli = helicopter.y;
			if (Killzone.hitsNorthBoundary(yHeli, boundary.nRectangles)) {
				isDead = true;
				hitsNorth = true;
			}
			if (Killzone.hitsSouthBoundary(yHeli, boundary.sRectangles)) {
				isDead = true;
				hitsSouth = true;
			}

			// end game if helicopter hits obstacle
			if (Killzone.hitsObstacle(yHeli, boundary.obstacles)) {
				isDead = true;
				hitsObstacle = true;
			}

			if (!isDead) {
				// helicopter accelerates up
				if (isClicked) {
					time += 1;
					int vel = (int) (0.9 * vel0) - (int) Math.round(0.9 * time);
					if (vel < -7) {
						helicopter.setVelocity(0, -7);
					} else helicopter.setVelocity(0, vel);
				}
				// helicopter free-falls
				if (!isClicked) {
					time += 1;
					int vel = (int) (0.8 * vel0) + (int) Math.round(1 * time);
					if (vel > 10) {
						helicopter.setVelocity(0, 10);
					} else helicopter.setVelocity(0, vel);
				}
			} else {
				helicopter.setVelocity(500, 0);
				tDead += 1;
				finalD = counter;
				if (tDead == 30) {
					reset();
				}
			}


			helicopter.setBounds(gameWidth, gameHeight);

			// algorithm for slope of boundary
			if (!isDead) counter += 1;

			// algorithm for boundary slope
			if ((counter % 100) == 0) {
				double x = Math.random();
				if (x < 0.3) {
					c = 0;
				} else if (x < 0.6) {
					c = 1;
				} else if (x < 0.9) {
					c = 4;
				} else {
					if (c == 0) c = 3;
					else if (c == 1) c = 2;
					else if (c == 2) c = 1;
					else if (c == 3) c = 0;
					else {
						if (x < 0.95) c = 2;
						else c = 3;
					}
				}
			}
			// algorithm for space between north and south boundary
			if ((counter % 150) == 0) {
				t -= 10;
				if (t < 270) {
					t = 270;
				}
			}

			int xFirst = (int) boundary.nRectangles.getFirst().getX();
			int wFirst = (int) boundary.nRectangles.getFirst().getWidth();
			int xLast = (int) boundary.nRectangles.getLast().getX();
			int wLast = (int) boundary.nRectangles.getLast().getWidth();

			// Enqueue new rectangle and obstacle
			if ((xLast + wLast) <= (Killzone.gameWidth + Killzone.rectWidth)) {
				Killzone.Enq(boundary.nRectangles, boundary.sRectangles, c, t);
				
				double k = Math.random();
				int yLast = (int) boundary.sRectangles.getLast().getY();
				int hLast = (int) boundary.nRectangles.getLast().getHeight();
				int possibleSpace = yLast - hLast - boundary.oHeight;
				if (((counter % 10) == 0) && (xLast < 895) && (xLast > 885)) {
					boundary.obstacles.add(new Rectangle(xLast, (int)(k*possibleSpace) + hLast, Killzone.rectWidth,
							boundary.oHeight));
				}
			}
			// Dequeue old rectangle
			if (!boundary.nRectangles.isEmpty() && (xFirst + wFirst < 0)) {
				Killzone.Deq(boundary.nRectangles, boundary.sRectangles);
			}
			// Dequeue old obstacle
			if (!boundary.obstacles.isEmpty() &&
					(boundary.obstacles.getFirst().x + boundary.obstacles.getFirst().width < 0)) {
				boundary.obstacles.remove();
			}
			boundary.move();
			helicopter.move();
		} else {
			t = tunnelStart;
			counter = 0;
		}
		repaint();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gameWidth, gameHeight);
		boundary.draw(g);
		
		// display distance score
		Font minor1 = new Font("04B", Font.PLAIN, 15);
		g.setFont(minor1);
		g.setColor(new Color(20, 56, 17));
		if (gameStart) {
			g.drawString("Distance = " + counter, 100, 588);
		} else {
			g.drawString("Distance = " + finalD, 100, 588);
		}
		// show high score
		if ((finalD > highScore) && (!gameStart)) {
			highScore = finalD;
		}
		g.drawString("High Score = " + highScore, 600, 588);

		if (!gameStart) {
			Font major = new Font("Stencil", Font.PLAIN, 40);
			g.setFont(major);
			g.setColor(Color.CYAN);
			g.drawString("CLICK  TO  START", 400, 320);

			Font minor2 = new Font("04B", Font.PLAIN, 20);
			g.setFont(minor2);
			g.setColor(new Color(20, 56, 17));
			g.drawString("Click and hold the left mouse button", 50, 450);
			g.drawString("to go up, release to go down.", 50, 475);
		}
		try {
			helicopter.draw(g);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(gameWidth, gameHeight);
	}
}
