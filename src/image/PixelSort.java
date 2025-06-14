package image;

import java.net.URL;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;


public class PixelSort {
	
	public static String sep = File.separator;
	public static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	
	public static void sortAllRows(String fileName, String extention) {
			int i = 0;
		try {
			BufferedImage image = ImageIO.read(new File(folder + sep + fileName + "." + extention));
			int h = image.getHeight();
			int w = image.getWidth();
			int[] pixels = new int[h * w];
			
			image.getRGB(0, 0, w, h, pixels, 0, w);
			
			while(i < pixels.length) {
				
				int end = Math.min(i + (w - 1), pixels.length - 1);
				Sort.sort(pixels, i, end);
				
				i += w;
			}
			
			image.setRGB(0, 0, w, h, pixels, 0, w);
			
			ImageIO.write(image, extention, new File(folder + sep + fileName + "PARALLEL_SORT_COMPARISON" + "." + extention));
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void sortDarker(String fileName, String extention , int redLimit, int greenLimit, int blueLimit) {
		int i = 0;
		int j = 0;
		PriorityQueue<Integer> px = new PriorityQueue<>((a, b) -> a - b);
//		System.out.println("queue created ");
		try {
			BufferedImage image = ImageIO.read(new File(folder + sep + fileName + "." + extention));
//			System.out.println("image found ");
			int h = image.getHeight();
			int w = image.getWidth();
			int[] pixels = new int[h * w];
//			System.out.println("array created ");
			
			
			image.getRGB(0, 0, w, h, pixels, 0, w); //getRGB(int startX, int startY, int width, int length, int[] array, offset, int scansize)
			
			while(i < pixels.length) {
				int onePix = pixels[i];
				int alpha = (onePix >> 24) & 0xFF;
				int red = (onePix >> 16) & 0xFF;
				int green = (onePix >> 8) & 0xFF;
				int blue = (onePix) & 0xFF;
				
				if(green < greenLimit && blue < blueLimit && red < redLimit ) {
					px.add(onePix);
					System.out.println("added to queue ");
				}
				i++;
			}
			System.out.println("pixels are added to priority queue and its size = " + px.size());
//			int[] sortable = new int[px.size()];
			while(j < pixels.length || !px.isEmpty()) {
				int onePix = pixels[j];
				int alpha = (onePix >> 24) & 0xFF;
				int red = (onePix >> 16) & 0xFF;
				int green = (onePix >> 8) & 0xFF;
				int blue = (onePix) & 0xFF;
				
				if(green < greenLimit && blue < blueLimit && red < redLimit ) {
					pixels[j] = px.poll();
					System.out.println("pixel of value = " + pixels[j] + " back");
				}
				j++;
			
			}
			
			image.setRGB(0, 0, w, h, pixels, 0, w);
			ImageIO.write(image, extention, new File(folder + sep + fileName + "SORTED_ROWS2" + "." + extention));
			
			
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * This method sorts all columns pixels of an image separately, column by column, smallest  value at the top, greatest at the bottom
	 * @param fileName name of the file without the extention
	 * @param extention the extention of the file, like jpg, png.
	 * @param renameTo rename the sorted image to what
	 */
	public static void sortVertically(String fileName, String extention, String renameTo) {
		int i = 0;
		int j = 0;
		int k = 0;
		try {
			BufferedImage image = ImageIO.read(new File(folder + sep + fileName + "." + extention));
			int h = image.getHeight();
			int w = image.getWidth();

			int[] pixels = new int[h * w];
//			int[] newPixels = new int[h * w];
			
			image.getRGB(0, 0, w, h, pixels, 0, w);
			
			int[] pixelsT = transpose(pixels, w);
			
			while(i < pixels.length) {
				int end = Math.min(i + (h - 1), pixels.length - 1);
				Sort.sort(pixelsT, i, end);
				
				i += h;
			}
			
			int[] pixelsST = transpose(pixelsT, h);
			
			image.setRGB(0, 0, w, h, pixelsST, 0, w);
			ImageIO.write(image, extention, new File(folder + sep + renameTo + "." + extention));
			
			
			
			

		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void sortVerticallyByColor(String fileName, String extention , int redLimit, int greenLimit, int blueLimit) {
		int i = 0;
		int j = 0;
		PriorityQueue<Integer> px = new PriorityQueue<>((a, b) -> a - b);
		try {
			BufferedImage image = ImageIO.read(new File(folder + sep + fileName + "." + extention));
			int h = image.getHeight();
			int w = image.getWidth();
			int[] pixels = new int[h * w];
			
			image.getRGB(0, 0, w, h, pixels, 0, w);
			
			int[] pixelsT = transpose(pixels, w);
			
			while(i < pixels.length) {
				int pixel = pixels[i];
				
				int alpha = (pixel >> 24) & 0xFF;
				int red = (pixel >> 16) & 0xFF;
				int green = (pixel >> 8) & 0xFF;
				int blue = (pixel) & 0xFF;
				
				if(red < redLimit && green < greenLimit && blue < blueLimit) {
					px.add(pixel);
				}
				i++;
			}
			
			while(j < pixelsT.length) {
				int pixel = pixelsT[j];
				
				int alpha = (pixel >> 24) & 0xFF;
				int red = (pixel >> 16) & 0xFF;
				int green = (pixel >> 8) & 0xFF;
				int blue = (pixel) & 0xFF;
				
				if(red < redLimit && green < greenLimit && blue < blueLimit) {
					pixelsT[j] = px.poll();
				}
				j++;
			}
			
			int[] pixelsST = transpose(pixelsT, h);
			
			image.setRGB(0, 0, w, h, pixelsST, 0, w);
			ImageIO.write(image, extention, new File(folder + sep + fileName + "1House_FULLY_SORTED_COLUMS" + "." + extention));
			
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static int[] transpose(int[] arr, int w) {
		int len = arr.length;
		int i = 0;
		int k = 0;
		int[] arrT = new int[len];
		
		while(i < w) {
			int j = i;
			while(j < len - (w - 1 - i)) {
				arrT[k] = arr[j];
				
				j += w;
				k++;
			}
			
			i ++;
		}
		
//		System.out.println(Arrays.toString(arrT));
		return arrT;
	}
	
	/**
	 * Sorts values that are greater than the specified red, green and blue value when calling the method
	 * @param fileName
	 * @param extention
	 * @param renameTo new file name of the sorted image
	 * @param the lower bound of red
	 * @param the lower bound of green
	 * @param the lower bound of blue
	 * 
	 */
	public static void sortSpecificVertically(String fileName, String extention, String renameTo, int redLimit, int greenLimit, int blueLimit, int redTop,int greenTop, int blueTop) {
		int i = 0;
		int j = 0;
		int y = 1; 
		try {
			BufferedImage image = ImageIO.read(new File(folder + sep + fileName + "." + extention));
			int h = image.getHeight();
			int w = image.getWidth();

			int[] pixels = new int[h * w];
//			int[] newPixels = new int[h * w];
			
			image.getRGB(0, 0, w, h, pixels, 0, w);
			
			int[] pixelsT = transpose(pixels, w);
			
			while(i < pixels.length) {
//				int end = Math.min(i + (h - 1), pixels.length - 1);
//				Sort.sort(pixelsT, i, end);
				int k = i;
				int n = i;
				PriorityQueue<Integer> px = new PriorityQueue<>((a, b) -> a - b);
				while(k < y * h) {
					
					int pixel = pixelsT[k];
//					int alpha = (pixel >> 24) & 0xFF;
					int red = (pixel >> 16) & 0xFF;
					int green = (pixel >> 8) & 0xFF;
					int blue = (pixel) & 0xFF;
//					System.out.println("looking for pixels to replace");
			//		System.out.println("pixel = " + pixel + " red = " + red + " green = " + green + " blue = " + blue);
					if(red > redLimit && green > greenLimit && blue > blueLimit && red < redTop && green < greenTop && blue < blueTop) {
						px.add(pixelsT[k]);
						System.out.println("pixel = " + pixel + " red = " + red + " green = " + green + " blue = " + blue);
//						System.out.println("added to queue");
					}
					k++;
				}
				
				while(!px.isEmpty() || n < y*h) {
					int pixel = pixelsT[n];
//					int alpha = (pixel >> 24) & 0xFF;
					int red = (pixel >> 16) & 0xFF;
					int green = (pixel >> 8) & 0xFF;
					int blue = (pixel) & 0xFF;
//					System.out.println("looking for taken pixels");
				
					if(red > redLimit && green > greenLimit && blue > blueLimit && red < redTop && green < greenTop && blue < blueTop) {
						pixelsT[n] = px.poll();
//						System.out.println("put back from queue");
					}
					n++;
				}
//				System.out.println("next row");
				y ++;
				i += h;
			}
			
			int[] pixelsST = transpose(pixelsT, h);
			
			image.setRGB(0, 0, w, h, pixelsST, 0, w);
			ImageIO.write(image, extention, new File(folder + sep + renameTo + "." + extention));
			
			
			
			

		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	

	
	
	
	
}
