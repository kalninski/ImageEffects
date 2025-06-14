package image;

import java.util.*;
import java.lang.StringBuilder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class Gauss {
	
	public BufferedImage original;
	public BufferedImage output;
	public int[] kernel;
	
	public static String sep = File.separator;
	public static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	
	
	public static double distribution(double deviation, int x, int y) {
		double numeratorExp =x * x + y * y;
		double denominatorExp = deviation * deviation * 2;
		double exp = (-1.0f) * (numeratorExp/denominatorExp);
		double coeff = 2 * Math.PI * deviation * deviation;
		coeff = 1/coeff;
		double result = coeff * Math.exp(exp);
		
		return result;
	}
	
	public static int[] kernel(double deviation, int radius) {
		
		StringBuilder matrixLook = new StringBuilder();
		int sumOfValues = 0;
		int size = 2 * radius + 1;
		int[] kernel = new int[size * size];
		int i = 0;
		for(int y = -radius; y <= radius; y++) {
			for(int x = -radius; x <= radius; x++) {
				kernel[i] = (int) (distribution(deviation, x, y) * 1000);
				sumOfValues += kernel[i];
				String value = (x == radius) ? String.valueOf(kernel[i]) + "\n" : String.valueOf(kernel[i]) + " \t ";
				matrixLook.append(value);
				i++;
			}
		}
		System.out.println("Sum of distribution values = " + sumOfValues);
		System.out.println(matrixLook.toString());	
		return kernel;
	}
	
	
	public void gaussBlur2(double deviation, int radius, String fileName, String extention, String renameTo) {
		File file  = new File(folder + sep + fileName + "." + extention);
		
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		try {
			BufferedImage orig = ImageIO.read(file);
			this.original = orig;
			BufferedImage out = ImageIO.read(file);
			this.output = out;
			int width = orig.getWidth() - 1 - (radius * 2);
			int height = orig.getHeight() - 1 - (radius * 2);
			this.kernel = kernel(deviation, radius);
			
			for(int y = radius ; y < height; y++) {
				final int rowY = y;
				executor.execute(() -> blurOneRow(orig, width, height, rowY, radius));
			}
			
		//	executor.shutdown();
			
			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS); // Wait forever if needed

			
			File fileOut  = new File(folder + sep + renameTo + "." + extention);
			ImageIO.write(this.output, extention, fileOut);
			
		}catch(IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void blurOneRow(BufferedImage original, int width, int height, int rowY, int radius) {
		int size = 2 * radius + 1;
		
		try {
			int cornerY = rowY - radius;
			int cornerY1 = (cornerY < 0) ? 0 : cornerY;
			int size1 = (rowY + radius >= height - 1) ? height - cornerY1 : size;
			BufferedImage subimage = this.original.getSubimage(0, cornerY1, width, size1);
			
			for(int x = radius; x < width; x++) {
				int redSum = 0;
				int greenSum = 0;
				int blueSum = 0;
				
				int counter = 0;
				int divideBy = 0;
				
				for(int dY = -radius; dY <= radius; dY++) {
					for(int dX = -radius; dX <= radius; dX++) {
						int nX = x + dX;
						int nY = radius + dY;//Do not blur all the image but with a few pixel padding
						if(nY >= 0 && nX >= 0 && nY < subimage.getHeight() && nX < width) {
							int pixel = subimage.getRGB(nX, nY);
							int nRed = (pixel >> 16) & 0xff;
							int nGreen = (pixel >> 8) & 0xff;
							int nBlue = (pixel) & 0xff;
							
							int multiplier = this.kernel[counter];
	//						System.out.println(multiplier);
							divideBy += multiplier;
							
							redSum += nRed * multiplier;
							greenSum += nGreen * multiplier;
							blueSum += nBlue * multiplier;
//							System.out.println("pixel value =  " + pixel + "   X : " + nX + "   Y : " + nY);

						}
						counter++;
					}
				}
//				int red = (int) Math.round((double) redSum / divideBy);
//				int green = (int) Math.round((double) greenSum / divideBy);
//				int blue = (int) Math.round((double) blueSum / divideBy);
				
				int red =  redSum / divideBy;
				int green = greenSum / divideBy;
				int blue = blueSum / divideBy;
				
				

				
				int newPixel = (red << 16) | (green << 8) | blue;
	//			System.out.println("oldpixel value = " + subimage.getRGB(x, radius) + " new Value = " + newPixel);
				
				this.output.setRGB(x, rowY, newPixel);
			}
			
			
		}catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}
	
}
