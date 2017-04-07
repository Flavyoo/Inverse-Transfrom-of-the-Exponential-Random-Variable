/*
 ****************************************************************************
 * Flavio Andrade
 * 4-5-2017
 * Compilation:  javac InverseTransform.java
 * Execution:    java InverseTransform numberOfSamples lambda
 * Dependencies: StdDraw.java
 *****************************************************************************
 */
import java.lang.Math;
import java.awt.Font;

public class InverseTransform {
	public static int y2Scale = 0;  // Amount of time return value occurs in interval
	public static int x2Scale = 150; // Range for waiting time.
	public static int ymod = 10;
        public static Font font = new Font("SansSerif", Font.PLAIN, 11);

	// Compute the inverse of the exponential function and pass in a
	// uniformly distributed number to sample.
	public static double inverse(double u, double lambda) {
		double y = Math.log(1 - u);
		return (-1.0 / lambda) * y;
	}

	// Mark the y-axis using intervals of 10.
	public static void markY(int yscale) {
		StdDraw.setFont(font);
		if (yscale / 100 > 1) { ymod = (yscale / 100) * 10; }
		for(int i = 0; i <= yscale - 10; i++) {
			if (i % ymod == 0) {
				StdDraw.line(-1.5, i, 0, i);
				StdDraw.textRight(-2.0, i, Integer.toString(i / 2));
			}
		}
	}

        // Mark the x-axis using intervals of 10.
	public static void markX(int xscale) {
		StdDraw.setFont(font);
		int x = -ymod / 5;
		for(int i = 10; i <= xscale - 10; i++) {
			if (i % 10 == 0.0) {
				StdDraw.line(i, 0, i,(x / 2));
				StdDraw.textRight(i, x, Integer.toString(i));
			}
		}
	}

	// Round up to the nearest ten.
	public static double roundUp(double inverse) {
		return Math.ceil(Math.ceil(inverse * 60 / 10) * 10);
	}

	// Round down to the nearest ten.
	public static double roundDown(double inverse) {
		return Math.floor(Math.floor(inverse * 60 / 10) * 10);
	}

	public static void main(String[] args) {
		int samples = Integer.parseInt(args[0]);
		y2Scale = samples * 2;
		int y1 = y2Scale / 10;
		int lambda = Integer.parseInt(args[1]);
		StdDraw.enableDoubleBuffering();
		StdDraw.setPenRadius(0.01);
		StdDraw.setCanvasSize(500, 500);
		StdDraw.setXscale(-40, x2Scale);
	        StdDraw.setYscale(-y1, y2Scale);
		StdDraw.line(0, 0, x2Scale - 5, 0); // Draw x-axis
		StdDraw.line(0, 0, 0, y2Scale - 5); // Draw y-axis
		markY(y2Scale);
		markX(x2Scale);
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.text(x2Scale / 2, y2Scale - y1, "Samples of the Inverse Transform of an Exponential" +
		        " Random Variable");
		StdDraw.text(x2Scale / 2, -y1 + y1 / 10, "Minutes Waiting");
		StdDraw.text(-25, y2Scale / 2, "Occurences");
		StdDraw.text(-25, (y2Scale / 2) - y1, "Samples: " + Integer.toString(samples));
		StdDraw.text(-25, (y2Scale / 2) - (y1 * 2), "Lambda: " + Integer.toString(lambda));
		int i = 0;
		// Save number of occurences to determine height of each bar.
		int[] height = new int[(x2Scale / 10) + 1];
		while (true) {
			long time = System.currentTimeMillis() / 10;
			if ((System.currentTimeMillis() / 10) - time == 1) {
				i++;
				double u = Math.random();
				double value = inverse(u, lambda);
				double x1 = roundDown(value);
				double x2 = roundUp(value);
				int position = (int) x2 / 10;
				int realPosition = 0;
				// Prevent IndexOutOfBoundsException.
				if (position < height.length) { realPosition = position; }
				height[realPosition]++;
				if (value * 60 <= x2 && value * 60 >= x1) {
					int h = height[realPosition];
					// x, y, height:  Center of rectangle     // Width and height.
					StdDraw.rectangle((x2 - (x2 - x1) / 2 ), h, (x2 - x1) / 2, h);
					StdDraw.show();
					StdDraw.pause(10);
				}
			}
			if (i == samples) { break; }
		}
	}
}
