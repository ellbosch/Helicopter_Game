import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Helicopter extends GameObject{
	final static int position = 200;
	final static int width = 106;
	final static int height = 51;
	final static int xHead = position + width;
	static boolean tilt;

	public Helicopter() {
		super(position, 275, 0, 0, width, height);
	}

	public void accelerate() {

	}


	public void draw(Graphics g) throws IOException {
		try {
			Graphics2D g2d = (Graphics2D)g;
			BufferedImage img = ImageIO.read(new File("helicopter.png"));

			// tilt helicopter slightly while it accelerates
			double locationX = img.getWidth() / 2;
			double locationY = img.getHeight() / 2;

			if (tilt) {
				AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI/10, locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

				// Drawing the rotated image at the required drawing locations
				g2d.drawImage(op.filter(img, null), position, y, null);

			} else {
				if (!HeliMap.gameStart) {
					AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI/15, locationX, locationY);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					// Drawing the rotated image at the required drawing locations
					g2d.drawImage(op.filter(img, null), position, y, null);
				} else {
					AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI/70, locationX, locationY);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					// Drawing the rotated image at the required drawing locations
					g2d.drawImage(op.filter(img, null), position, y, null);
				}
			}


		} catch (IOException e) {

		}		
	}

}
