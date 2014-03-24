import java.awt.*;
import java.awt.Rectangle;
import java.util.LinkedList;


public class Boundary extends GameObject {
	LinkedList<Rectangle> nRectangles = new LinkedList<Rectangle>();
	LinkedList<Rectangle> sRectangles = new LinkedList<Rectangle>();
	LinkedList<Rectangle> obstacles = new LinkedList<Rectangle>();
	private static int xVelocity = 11;
	private static int arc = 10;
	final int oHeight = 100;
	//public boolean drawObstacle = false;

	public Boundary() {
		super(0,0,-xVelocity,0,0,0);
		Killzone.Begin(nRectangles, sRectangles);

	}

	public void draw(Graphics g) {

		for (int i = 0; i < nRectangles.size(); i++) {
			// Draw north and south boundaries
			int x = (int) nRectangles.get(i).getX();
			int yNorth = (int) nRectangles.get(i).getY();
			int ySouth = (int) sRectangles.get(i).getY();
			int w = (int) nRectangles.get(i).getWidth();
			int hNorth = (int) nRectangles.get(i).getHeight();
			int hSouth = (int) sRectangles.get(i).getHeight();

			// turn boundary red if helicopter hits it
			if (HeliMap.hitsNorth) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.CYAN);
			}
			g.fillRoundRect(x, yNorth - arc, w, hNorth+arc, arc, arc);
			g.setColor(Color.CYAN);
			if (HeliMap.hitsSouth) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.CYAN);
			}
			g.fillRoundRect((x - Killzone.rectWidth), ySouth, w, hSouth+arc, arc, arc);		
		}
		for (int i = 0; i < obstacles.size(); i++) {
			Rectangle o = obstacles.get(i);

			if ((HeliMap.hitsObstacle)) {
					//((o.x > (Helicopter.position - 5)) && ((o.x + o.width) < (Helicopter.xHead + 5)))) {
				g.setColor(Color.RED);
				g.fillRoundRect((int) o.getX(), (int) o.getY(), (int) o.getWidth(),
						(int) o.getHeight(), arc, arc);
			} else {
				g.setColor(Color.CYAN);
				g.fillRoundRect((int) o.getX(), (int) o.getY(), (int) o.getWidth(),
						(int) o.getHeight(), arc, arc);
			}
		}
	}




	public void move() {
		for (int i = 0; i < nRectangles.size(); i++) {
			if (!HeliMap.isDead) {
				nRectangles.get(i).translate(-xVelocity, 0);
				sRectangles.get(i).translate(-xVelocity, 0);
			} else {
				nRectangles.get(i).translate(0, 0);
			}
		}
		for (int i = 0; i < obstacles.size(); i++) {
			if (!HeliMap.isDead) {
				obstacles.get(i).translate(-xVelocity, 0);
			} else {
				obstacles.get(i).translate(0,0);
			}
		}

	}

	public void accelerate() {

	}
}
