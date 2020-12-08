import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Mark Tao, Spring 2020
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points
	private ArrayList<Point> region = new ArrayList<Point>();

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		regions = new ArrayList<ArrayList<Point>>();

		for (int y = 0; y < image.getHeight(); y++) { // looping over all pixels
			for (int x = 0; x < image.getWidth(); x++) {
				if (visited.getRGB(x, y) == 0 && colorMatch(targetColor, new Color(image.getRGB(x, y)))) { //if the pixel hasn't been visited
																										   //and matches the input color,
					ArrayList<Point> region = new ArrayList<Point>(); //create a new arraylist that tracks region.
					Deque<Point> toVisit = new ArrayDeque<Point>(); //create a new deque that tracks whether points have been visited.
					toVisit.add(new Point(x, y)); //and queue them to the deque.
					while (!toVisit.isEmpty()) { //if there are items in the deque,
						Point point = toVisit.removeLast(); //remove the last item of the list so the loop can run
						if (visited.getRGB(point.x, point.y) == 0) { //if the point hasn't been visited, mark that it has and
															         // add it to the region.
							visited.setRGB(point.x, point.y, 1);
							region.add(point);
							for (int y2 = Math.max(0, point.y - 1); y2 < Math.min(image.getHeight(), point.y + 2); y2++) { //loop through all of
								for (int x2 = Math.max(0, point.x - 1); x2 < Math.min(image.getWidth(), point.x + 2); x2++) { //the neighbors
										if (colorMatch(targetColor, new Color(image.getRGB(x2, y2))) &&
												visited.getRGB(x2, y2) == 0 && (point.x != x2 || point.y != y2)) {
											//if the color matches, hasn't been visited, and the neighboring point isn't being mistaken for the actual
											//point, then add it to the queue.
											toVisit.add(new Point(x2, y2));
										}
									}
								}
							}

						}
					if (region.size() > minRegion) { //if the region is big enough to be worth keeping, then add it to regions.
						regions.add(region);
					}

					}
				}
			}
		}




	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		if (Math.abs(c1.getRed() - c2.getRed()) < maxColorDiff && Math.abs(c1.getGreen() - c2.getGreen()) < maxColorDiff &&
		    Math.abs(c1.getBlue() - c2.getBlue()) < maxColorDiff) { //if the colors are similar enough, return true.
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		ArrayList<Point> largestRegion;
		largestRegion = regions.get(0);

		if (regions.size() == 0) { //if there are no regions, don't return anything
			return null;
		}

		for (int i = 1; i < regions.size(); i++) { //loop through all of the regions and find the biggest one
			if (regions.get(i).size() > regions.get(i - 1).size()) {
				largestRegion = regions.get(i); //update what largestRegion is if the next item in the list is bigger.
			}
		}
		return largestRegion;
	}



	/**
	 * Sets recoloredImage to be a copy of image,
	 * but with each region a uniform random color,
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		for (ArrayList<Point> region : regions) { // for all the regions, find the biggest one, and give it a random uniform color.
			Color color = new Color((int) (Math.random()*256), (int) (Math.random() * 256), (int)(Math.random()*256));
			for (Point point : region) {
				recoloredImage.setRGB(point.x, point.y, color.getRGB());
			}

		}
	}
}
