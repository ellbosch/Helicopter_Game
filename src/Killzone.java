import java.awt.Rectangle;
import java.util.LinkedList;

public class Killzone {
	final static int gameWidth = 900;
	final static int gameHeight = 600;
	final static int rectWidth = 50;
	final static int tunnelStart = 450;

	// beginning of game
	public static void Begin(LinkedList<Rectangle> north, LinkedList<Rectangle> south) {
		if (north.isEmpty() && south.isEmpty()) {			
			north.add(new Rectangle(gameWidth + (2 * rectWidth), 50));
			south.add(new Rectangle(0, (gameHeight - 50), (gameWidth + (2 * rectWidth)), 100));
		} else System.out.println("LinkedList of Rectangles not empty");
	}


	// enqueue rectangles while the game runs
	public static void Enq(LinkedList<Rectangle> north, LinkedList<Rectangle> south, int c, int t) {
		if ((north == null) || (south == null)) {
			System.out.println("Null argument in Enq");
		}

		if (c > 4) {
			System.out.println("Illegal argument in Enq");
		}

		int xLast = (int) north.getLast().getX();
		int wLast = (int) north.getLast().getWidth();
		int hLast = (int) north.getLast().getHeight();

		double x = Math.random();

		// rectangles in interval with decreasing height
		if (c == 0) {
			if (x < 0.5) {
				north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast-2)));
			} else {
				north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast-4)));
			}
		}
		// rectangles in interval with increasing height
		else if (c == 1) {
			if (x < 0.5) {
				north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast+2)));
			} else {
				north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast+4)));
			}
			// rectangles in interval with quickly decreasing height
		} else if (c == 2) {
			north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast-10)));
			// rectangles in interval with quickly increasing height
		} else if (c == 3) {
			north.add(new Rectangle((xLast+wLast), 0, rectWidth, (hLast+10)));
			// rectangles in interval with non-varying height
		} else {
			north.add(new Rectangle((xLast+wLast), 0, rectWidth, hLast));
		}

		int newH = (int) north.getLast().getHeight();

		// make sure rectangles aren't negative
		if (newH < 10) {
			north.getLast().setBounds(xLast, 0, rectWidth, 100);
		}
		if ((t + newH) > (gameHeight - 10)) {
			north.getLast().setBounds(xLast, 0, rectWidth, (newH-100));
		}

		// enqueue south rectangle
		int newerH = (int) north.getLast().getHeight();
		int ySouth = t + newerH;

		south.add(new Rectangle((xLast+wLast), ySouth, rectWidth, (gameHeight-ySouth)));

	}

	// dequeue rectangle when off screen
	public static void Deq(LinkedList<Rectangle> north, LinkedList<Rectangle> south) {
		north.removeFirst();
		south.removeFirst();
	}
	// determine whether helicopter hits north boundary
	public static boolean hitsNorthBoundary(int y, LinkedList<Rectangle> north) {

		int size = north.size();
		int yMin = 0;
		for (int i = 0; i < size; i++) {
			if ((north.get(i).x < Helicopter.xHead)
					&& (north.get(i).x + north.get(i).width > Helicopter.position)) {
				yMin = north.get(i).height - 15;
			}
		}
		if ((yMin > y) && (yMin < y + Helicopter.height)) {
			return true;
		} else return false;
	}
	
	// determine whether helicopter hits south boundary
		public static boolean hitsSouthBoundary(int y, LinkedList<Rectangle> south) {

			int size = south.size();
			int yMax = gameHeight;
			for (int i = 0; i < size; i++) {
				if ((south.get(i).x < Helicopter.xHead)
						&& (south.get(i).x + south.get(i).width > Helicopter.position)) {
					yMax = south.get(i).y - 5;
				}
			}
			if ((yMax > y) && (yMax < y + Helicopter.height)) {
				return true;
			} else return false;
		}
	
	// determine wither helicopter hits obstacle
	public static boolean hitsObstacle(int y, LinkedList<Rectangle> obstacles) {

		int size = obstacles.size();
		int yMin = 0;
		int yMax = 0;
		for (int i = 0; i < size; i++) {
			if ((obstacles.get(i).x < Helicopter.xHead)
					&& (obstacles.get(i).x + Killzone.rectWidth > Helicopter.position)) {
				yMin = obstacles.get(i).y;
				yMax = yMin + obstacles.get(i).height - 5;
			}
		}
		if (((yMax > y) && (yMax < y + Helicopter.height))
				|| ((yMin > y) && (yMin < y + Helicopter.height))) {
			return true;			
		} else return false;
	}

}
