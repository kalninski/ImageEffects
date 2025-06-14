package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class Convolution{
	public static String sep = File.separator;
	public static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	
	public static double pow(double base, int power) {
		double result  = 1;
		for(int i = 0; i < power;  i++) {
			result *= base;
		}
		
		return result;
	}
	
	public static double factorial(double a) {
		double result = 1.0;
		if(a == 0) {
			return 1;
		}
		for(int i = 1; i <= a; i++) {
			result *= i;
		}
		return result;
	}
	
	public static double cos(double x, int terms) {
		double result = 0;

		for(int i = 0; i < terms; i++) {
			double sign = (i % 2 == 0) ? 1 : -1;
			result += ((sign * pow(x, i * 2)) / ( factorial(i*2)));
			System.out.println(result + " = " + sign + " * " + pow(x, i*2) + " / " + factorial(i * 2));
			
		}
		
		return result;
	}
	
	public static double sin(double x, int terms) {
		double result  = 0;
		
		for(int i = 0; i < terms; i++) {
			double sign  = (i % 2 == 0) ? 1 : -1;
			double numerator = pow(x, i * 2 + 1);
			double denominator = factorial(i * 2 + 1);
			double term = (sign *  numerator) / denominator;
			result += term;
		}
		return result;
	}
	
	public static BufferedImage color(int height, int width, String fileName, String extention, int color) {

		BufferedImage img  = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		int[] pixels = new int[height * width];
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
		img.setRGB(0, 0, width, height, pixels, 0, width);
		try {
			File file = new File(folder + sep + fileName + "." + extention);
			ImageIO.write(img, extention, file);
			System.out.println(file.getAbsolutePath());
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		return img;
	}
	
	public static void colorCircle(int x, int y, int r,int height, int width, String fileName, String extention, int bckgrnd, int circleCol) {
		BufferedImage img = color(height, width, fileName, extention, bckgrnd);
		int slices = r/10;
		int rows = 90 * slices;
		for(int i = 1; i <= rows; i++) {
			double rad = ( Math.PI / (180  * slices) ) * i;
			double x1 = cos(rad, 10) * r;
			double y1 = sin(rad, 10) * r;
			
			int newLeftX = x - (int) x1;
			int newRightX = x + (int) x1;
			
			int newYhigh = y + (int) y1;
			int newYlow = y - (int) y1;
			
			int size = newRightX - newLeftX;
			
			for(int j = 0; j < size; j ++) {
				img.setRGB(newLeftX + j, newYhigh, circleCol);
				img.setRGB(newLeftX + j, newYlow, circleCol);
			}
		}
		try {
			File file = new File(folder + sep + fileName + "." + extention);
			ImageIO.write(img, extention, file);
			System.out.println(file.getAbsolutePath());
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void colorCircleThreads(int x, int y, int r,int height, int width, String fileName, String extention, int bckgrnd, int circleCol) {
		BufferedImage img = color(height, width, fileName, extention, bckgrnd);
		List<Thread> list = new ArrayList<>();
		double len = 2 * Math.PI * r;
		double quarter = len/4;
		int quart =(int) len/4;
		double slices = quarter / 90;
		int rows = 90 * 1000;
		for(int i = 1; i <= quart; i++) {
			double rad = ( Math.PI / (180 * (quarter / 90))) * i;
			double x1 = cos(rad, 10) * r;
			double y1 = sin(rad, 10) * r;
			
			int newLeftX = x - (int) x1;
			int newRightX = x + (int) x1;
			
			int newYhigh = y + (int) y1;
			int newYlow = y - (int) y1;
			
			int size = newRightX - newLeftX;
			
			int red = (circleCol >> 16) & 0xff;
			int green  = (circleCol >> 8) & 0xff;
			int blue = (circleCol) & 0xff;
			
			int colorIncrement =(int) quarter/255;
			
//			red = Math.max(0, Math.min(255,(int) ( sin( i, 10)) * 100 + 127));
//			green = Math.max(0, Math.min(255, green + colorIncrement));
//			blue = Math.max(0, Math.min(255, i));
			
//			int pixel = red | green | blue;
			
			ThreadsFunc t = new ThreadsFunc(img, size, newLeftX, newYhigh, newYlow, ((circleCol + bckgrnd)/2));
			t.start();
			list.add(t);
		}
		

		try {
			for(Thread thrd : list) {
				thrd.join();
			}
			
			File file = new File(folder + sep + fileName + "." + extention);
			ImageIO.write(img, extention, file);
			System.out.println(file.getAbsolutePath());
		}catch(IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void shitNoise(String fileName, String extention, String newName) {
		File file = new File(folder + sep + fileName + "." + extention);
		
		try {
			BufferedImage img = ImageIO.read(file);
			int height = img.getHeight();
			int width = img.getWidth();
			int[] pixels = new int[width * height];
//			int y = 0;
			for(int y = 0; y < height - 1; y++) {
				System.out.println("row y = " + y);
				for(int x = 0; x < width - 1; x++) {
					int p22 = img.getRGB(x, y);// pixel at the center of the kernel
					int p11 = (x - 1 < 0 || y - 1 < 0) ? p22 : img.getRGB(x - 1, y - 1);
					int p12 = (y - 1 < 0) ? p22 : img.getRGB(x, y - 1);
					int p13 = (x + 1 > width || y - 1 < 0 ) ? p22 : img.getRGB(x + 1, y - 1);
					int p21 = (x - 1 < 0) ? p22 : img.getRGB(x - 1, y);
					
					int p23 = (x + 1 > width) ? p22 : img.getRGB(x + 1, y);
					int p31 = (x - 1 < 0 || y + 1 > height) ? p22 : img.getRGB(x - 1, y + 1);
					int p32 = (y + 1 > height) ? p22 : img.getRGB(x, y + 1);
					int p33 = (x + 1 > width || y + 1 > height) ? p22 : img.getRGB(x + 1, y + 1);
					
					int newPixel = (p11 + p12 + p13 + p21 + p22 + p23 + p31 + p32 + p33)/9;
//					System.out.println("11 = " + p11 + " 12 = " + p12 + " 13 = " + p13 + " 21 = " + p21 + " center = " + p22  + " 23 = " + p23  + "31 = " + p31 + " 32 = " + p32 + " 33 = " + p33);
//					System.out.println("oldVal = " + p22 + " newVal = " + newPixel);
					System.out.println("col x = " + x);
//					img.setRGB(x, y, newPixel);
					pixels[x + y * width] = newPixel;
				}

			}
			img.setRGB(0, 0, width, height, pixels, 0, width);
			
			File f = new File(folder + sep + newName + "." + extention);
			ImageIO.write(img, extention, f);
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static void blurBasic(String fileName, String extention, String newName) {
		File file = new File(folder + sep + fileName + "." + extention);
		
		try {
			BufferedImage img = ImageIO.read(file);
			int height = img.getHeight();
			int width = img.getWidth();
			int[] pixels = new int[width * height];
//			int y = 0;
			for(int y = 0; y < height; y++) {
				System.out.println("row y = " + y);
				for(int x = 0; x < width; x++) {
					int p22 = img.getRGB(x, y);// pixel at the center of the kernel
					int red22 = (p22 >> 16) & 0xff;
					int green22 = (p22 >> 8) & 0xff;
					int blue22 = (p22) & 0xff;
					System.out.println("new row ");
					int newRed = 0;
					int newGreen = 0;
					int newBlue = 0;
					int count = 1;
					
					for(int dY = -1;dY <=1; dY++) {
						for(int dX = -1; dX <=1; dX++) {
							int nX = x + dX;
							int nY = y + dY;
							if(nY < height && nY >= 0 && nX < width && nX >= 0) {
								int neighbour = img.getRGB(nX, nY);
								newRed += (neighbour >> 16) & 0xff;
								newGreen += (neighbour >> 8) & 0xff;
								newBlue += (neighbour) & 0xff;
								count++;
								System.out.println("pixel average ");
							}
						}
					}
					int red = newRed/count;
					int green = newGreen/count;
					int blue = newBlue/count;
					
					int newPixel = (red << 16) | (green << 8) | blue;
					pixels[x + y * width] = newPixel;
					
					
//					System.out.println("11 = " + p11 + " 12 = " + p12 + " 13 = " + p13 + " 21 = " + p21 + " center = " + p22  + " 23 = " + p23  + "31 = " + p31 + " 32 = " + p32 + " 33 = " + p33);
//					System.out.println("oldVal = " + p22 + " newVal = " + newPixel);
					
//					img.setRGB(x, y, newPixel);
					
				}

			}
			img.setRGB(0, 0, width, height, pixels, 0, width);
			
			File f = new File(folder + sep + newName + "." + extention);
			ImageIO.write(img, extention, f);
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static class ThreadsFunc  extends Thread{
		public BufferedImage image;
		public int size;
		public int startX;
		public int rowHigh;
		public int rowLow;
		public int color;
		
		public ThreadsFunc(BufferedImage image,int size, int startX, int rowHigh, int rowLow, int color) {
			this.image = image;
			this.size = size;
			this.startX = startX;
			this.rowHigh = rowHigh;
			this.rowLow = rowLow;
			this.color = color;
		}
		
		@Override
		public void run() {
			for(int i = 0; i < this.size; i  ++) {
				this.image.setRGB(startX + i, rowHigh, color);
				this.image.setRGB(startX + i, rowLow, color);
			}
		}
	}
}
