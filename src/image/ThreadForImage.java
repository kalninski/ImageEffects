package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class ThreadForImage extends Thread {
	public static String sep = File.separator;
	public static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	public BufferedImage image;
	
	
	public ThreadForImage(BufferedImage image) {
		this.image = image;
	}
	
	public ThreadForImage() {}
	
	@Override
	public void run() {
		
	}
	
	public void sortRows(String fileName, String extention, String renameTo) {
		File image = new File(folder + sep + fileName + "." + extention);
		
		 int numThreads = Runtime.getRuntime().availableProcessors();
	     ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		
		try {
			this.image = ImageIO.read(image);
			
	        for (int y = 0; y < this.image.getHeight(); y++) {
	            int row = y;
	            executor.execute(() -> sortRow(row));
	        }

	        executor.shutdown();
	        while (!executor.isTerminated()) {
	            // Wait for all threads to finish
	        	System.out.println("sorting");
	        }
	        ImageIO.write(this.image, extention, new File(folder + sep + renameTo + "." + extention)); 
	        
	        
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void sortVertical(String fileName, String extention, String renameTo) {
		File img = new File(folder + sep + fileName + "." + extention);
		
		int numThreads = Runtime.getRuntime().availableProcessors();//get the number of available processors
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);//as many available processors there are, there can be parallel threads
		//executor service manages tasks
		
		try {
			this.image = ImageIO.read(img);
			int h = this.image.getHeight();
			int w = this.image.getWidth();
			for(int x = 0; x < this.image.getWidth(); x++) {
				int column = x;
				executor.execute(() -> sortColumn(column)); 
			}
			executor.shutdown();
	        while (!executor.isTerminated()) {
	            // Wait for all threads to finish
	        	System.out.println("sorting");
	        }
	        ImageIO.write(this.image, extention, new File(folder + sep + renameTo + "." + extention)); 
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void sortSpecVertical(String fileName, String extention, String renameTo, int redLow, int greenLow, int blueLow, int redTop,int greenTop, int blueTop) {
		File img = new File(folder + sep + fileName + "." + extention);
		
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("processors available = " + processors);
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		
		try {
			this.image = ImageIO.read(img);
			
			for(int x = 0; x < this.image.getWidth(); x ++) {
				int column = x;
				System.out.println("column " + x + " out of " + image.getWidth());
				executor.execute(() -> sortColumnSpecific(column, redLow, greenLow, blueLow, redTop, greenTop, blueTop));
			}
			//blocks new tasks from beign added, if I have other code that also calls it. This is important since I have finite amount of processing power
			executor.shutdown();
			try {
			if(!executor.awaitTermination(60, TimeUnit.SECONDS)) {//Without this condition the method jumps to saving the image 
				System.out.println("processing");				//without waiting for the thread to finish
				executor.shutdownNow();
			}
			}catch(InterruptedException ex) {
				ex.printStackTrace();
				executor.shutdownNow();
	            Thread.currentThread().interrupt();
			}
			ImageIO.write(image, extention, new File(folder + sep + renameTo + "." + extention));
			
		}catch(IOException ex) {
			ex.printStackTrace();
			
		}
	}
	
	public void sortRow(int y) {
		int width = this.image.getWidth();
		int[] pixelsOfOneRow = new int[width];
		
		for(int x = 0; x < pixelsOfOneRow.length; x++) {
			pixelsOfOneRow[x] = this.image.getRGB(x, y);
		}
		
		Sort.sortArr(pixelsOfOneRow);
		
		for(int x = 0; x < pixelsOfOneRow.length; x++) {
			this.image.setRGB(x, y, pixelsOfOneRow[x]);
		}
	}
	
	public static void rotateImage(String fileName, String extention, String renameTo) {
		try {
			BufferedImage img = ImageIO.read(new File(folder + sep + fileName + "." + extention));
			int h = img.getHeight();
			int w = img.getWidth();
			int[] pixels = new int[w * h];
			
			img.getRGB(0, 0, w, h, pixels, 0, w);
			
			int[] pixelsT = transpose(pixels, w);
			
			BufferedImage newImg = new BufferedImage(h, w, img.getType());
			
			newImg.setRGB(0, 0, h, w, pixelsT, 0, h);
			
			ImageIO.write(newImg, extention, new File(folder + sep + renameTo + "." + extention));
			
			
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void sortColumn(int x) {
		int height = this.image.getHeight();
		int[] pixels = new int[height];
		
		for(int y = 0; y < pixels.length; y++) {
			pixels[y] = this.image.getRGB(x, y);
		}
		Sort.sortArr(pixels);
		
		for(int y = 0; y < pixels.length; y++) {
			this.image.setRGB(x, y, pixels[y]);
		}
		
	}
	
	public void sortColumnSpecific(int x, int redLow, int greenLow, int blueLow, int redTop,int greenTop, int blueTop) {
		int height = this.image.getHeight();
		
//		int[] pixels = new int[height];
		PriorityQueue<Integer> pixelsToRearrange = new PriorityQueue<>((a, b) -> a - b);
		
		for(int y = 0; y < height; y++) {
			int pixel = this.image.getRGB(x, y);
			int red = (pixel >> 16) & 0xFF;
			int green = (pixel >> 8) & 0xFF;
			int blue = (pixel) & 0xFF;
//			System.out.println("px value = " + pixel);
			if(red > redLow && green > greenLow && blue > blueLow && red < redTop && green < greenTop && blue < blueTop){
//				pixels[y] = this.image.getRGB(x, y);
//				System.out.println("pixel = " + pixel + " red = " + red + " green = " + green + " blue = " + blue);
				pixelsToRearrange.add(this.image.getRGB(x, y));
			}
		}
//		Sort.sortArr(pixels);
		System.out.println(pixelsToRearrange.size());
		int y1 = 0; 
		while(y1 < height) {
			int pixel = this.image.getRGB(x, y1);
			int red = (pixel >> 16) & 0xFF;
			int green = (pixel >> 8) & 0xFF;
			int blue = (pixel) & 0xFF;
		//	System.out.println("-->red = " + red + " green = " + green + " blue = " + blue);
			if(red > redLow && green > greenLow && blue > blueLow && red < redTop && green < greenTop && blue < blueTop){
				this.image.setRGB(x, y1, pixelsToRearrange.poll());
				
			}
			y1++;
			
		}
		
	}
	
	

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
					System.out.println("looking for pixels to replace");
				
					if(red > redLimit && green > greenLimit && blue > blueLimit && red < redTop && green < greenTop && blue < blueTop) {
						px.add(pixelsT[k]);
						System.out.println("added to queue");
					}
					k++;
				}
				
				while(!px.isEmpty() || n < y*h) {
					int pixel = pixelsT[n];
//					int alpha = (pixel >> 24) & 0xFF;
					int red = (pixel >> 16) & 0xFF;
					int green = (pixel >> 8) & 0xFF;
					int blue = (pixel) & 0xFF;
					System.out.println("looking for taken pixels");
				
					if(red > redLimit && green > greenLimit && blue > blueLimit && red < redTop && green < greenTop && blue < blueTop) {
						pixelsT[n] = px.poll();
						System.out.println("put back from queue");
					}
					n++;
				}
				System.out.println("next row");
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
	
}
