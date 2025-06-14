package image;

import java.io.*;
import java.util.concurrent.*;

import javax.imageio.ImageIO;

import java.util.*;
import java.awt.image.BufferedImage;

public class Sobel {

	public static String sep = File.separator;
	public static String path = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	public BufferedImage original;
	public BufferedImage output;
	public int[] sobel = new int[] {-1, 0, 1, -2, 0, 2, -1, 0, 1};
	public int[] sobel1 = new int[] {1, 2, 1, -0, 0, 0, -1, -2, -1};
	
	
	
	
	public void sobelEdges(String fileName, String extention, String renameTo) {
		
		File file  = new File(path + sep + fileName + "." + extention);
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService e = Executors.newFixedThreadPool(processors);
		
		
		try {
			BufferedImage orig = ImageIO.read(file);
			int height = orig.getHeight();
			int width  = orig.getWidth();
			BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			this.output = out;
			this.original = orig;
			for(int y = 1 ; y < height - 1; y++) {
				final int rowY = y;
				e.execute(() -> sobelHorizontal(rowY));
			}
			e.shutdown();
			
			e.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS); 
			File o = new File(path + sep + renameTo + "." + extention);
			ImageIO.write(this.output, extention, o);
			
			
			
		}catch(IOException | InterruptedException ex){
			ex.printStackTrace();
		}
		
	}
	
	public void sobelHorizontal(int rowY) {
		int width = this.original.getWidth();
		try {
			BufferedImage subimage = this.original.getSubimage(0, rowY - 1, width, 3);
			 for(int x = 1; x < width - 1; x++) {
				 int redSum = 0;
				 int greenSum = 0;
				 int blueSum = 0;
				 int greenSUm = 0;
				 int counter = 0;
				 
				 for(int dY = -1; dY <= 1; dY++) {
					 for(int dX = 0; dX <= 1; dX++) {
						 int nY = 1 + dY;
						 int nX = x + dX;
						 
						 int pix = subimage.getRGB(nX, nY);
						 int multiplier = sobel[counter];
						 int nRed = ((pix >> 16) & 0xff) * multiplier;
						 int nGreen = ((pix >> 8) & 0xff) * multiplier;
						 int nBlue  = ((pix) & 0xff) * multiplier;
						 redSum += nRed;
						 greenSum += nGreen;
						 blueSum += nBlue;
						 
						 int newPixel = (redSum << 16) | (greenSum << 8) | blueSum;
						 subimage.setRGB(x, 1, newPixel);
						 
					 }
				 }
			 }
				 
				 for(int x1 = 1; x1 < width - 1; x1++) {
					 int redSum = 0;
					 int greenSum = 0;
					 int blueSum = 0;
					 int greenSUm = 0;
					 int counter = 0;
					 
					 for(int dY = -1; dY <= 1; dY++) {
						 for(int dX = 0; dX <= 1; dX++) {
							 int nY = 1 + dY;
							 int nX = x1 + dX;
							 
							 int pix = subimage.getRGB(nX, nY);
							 int multiplier = sobel1[counter];
							 int nRed = ((pix >> 16) & 0xff) * multiplier;
							 int nGreen = ((pix >> 8) & 0xff) * multiplier;
							 int nBlue  = ((pix) & 0xff) * multiplier;
							 redSum += nRed;
							 greenSum += nGreen;
							 blueSum += nBlue;
							 
							 int newPixel = (redSum << 16) | (greenSum << 8) | blueSum;
							 this.output.setRGB(x1, rowY, newPixel);
							 
						 }
					 }
				 
				 }
		}catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
