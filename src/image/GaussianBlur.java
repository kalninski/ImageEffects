package image;

import java.util.*;
import java.lang.StringBuilder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class GaussianBlur {
	
	public BufferedImage image;
	public BufferedImage outPutImage;
	public Kernel kernel;
//	public int width = image.getWidth();
	
	public static String sep = File.separator;
	public static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	
	
	public void blurGaussian(String fileName, String extention, String renameTo, float deviation, int strength) {//strength = halfSize parameter
		//instantiate the Kernel array first
		File file = new File(folder + sep + fileName + "." + extention );
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		
		try {
			BufferedImage original = ImageIO.read(file);
//			BufferedImage subimage = original.getSubimage(0, 0, original.getWidth(), original.getHeight());
			Kernel krnl = Kernel.newKernel(deviation, strength);
			this.kernel = krnl;
			float[][] values = krnl.kernelValues;
			this.kernel.kernelValues = values;
			this.image = original;
			int height = original.getHeight();
			int width = original.getWidth();
			for(int y = 0; y < width ; y++) {
				final int rowY = y;
//				BufferedImage subimage = original.getSubimage(0, 0, original.getWidth(), original.getHeight());
				executor.execute(() -> blurRow(original.getSubimage(0, 0, original.getWidth(), original.getHeight()), width, rowY, strength));
			}
			executor.shutdown();
			
			if(executor.awaitTermination(60, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
			
			File blurred = new File(folder + sep +  renameTo + "." + extention);
			ImageIO.write(original, extention, blurred);
			System.out.println("path = " + file.getAbsolutePath());
			
		}catch(IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void blurRow(BufferedImage img,int width, int rowY, int strength) {//img is the subimage instantiated in the calling method above
		BufferedImage original = this.image; //set pixels for this, read pixels from "img"
		for(int x = 0; x < width; x++) {
			int px = img.getRGB(x, rowY);
			float r = (float) ((px >> 16) & 0xff);
			float g = (float) ((px >> 8) & 0xff);
			float b = (float) ((px) & 0xff);
			
			
			float redSum = r ;//* this.kernel.kernelValues[strength + 1][strength + 1];
			float greenSum = g ;//* this.kernel.kernelValues[strength + 1][strength + 1];
			float blueSum = b ;//* this.kernel.kernelValues[strength + 1][strength + 1];
			int counterX;//for the array that is the kernel
			int counterY = 0;//for the array that is the kernel
			for(int dY = -strength; dY <= strength ; dY++) {
				counterX = 0;//for the array that is the kernel
				for(int dX = -strength; dX <= strength ; dX++) {
					int nY = rowY + dY; // get y coordinate of a neighbouring pxl;
					int nX = x + dX;//get x coordinate of a neighbour pxl;
					if(nX >=0 && nX < width && nY >= 0 && nY < width) {
	//					int absNX = Kernel.abs(nX);
	//					int absNY = Kernel.abs(nY);
	//					System.out.println(absNY + " = absNY" + absNX + " = absNX");
						float multiplier = this.kernel.kernelValues[counterY][counterX];
						System.out.println(counterY + " =  counterY" + counterX + " = counterX");
						int pixel = img.getRGB(nX, nY);
						int red = (pixel >> 16) & 0xff;
						int green = (pixel >> 8) & 0xff;
						int blue = pixel & 0xff;
						
						redSum += multiplier * (float) red;
						greenSum += multiplier * (float) green;
						blueSum += multiplier * (float) blue;
						
						
						
						
					}
					counterX++;
					
				}
				
				counterY++;
				
			}
			
			int redSumInt = (int) redSum;
			int greenSumInt = (int) greenSum;
			int blueSumInt = (int) blueSum;
			int newPixel = (redSumInt << 16) | (greenSumInt << 8) | blueSumInt;
			
			this.image.setRGB(x, rowY, newPixel);
			
			counterX = 0;
			counterY = 0;
		}
	}
	
	public void gaussianBlur(float deviation, int halfSize, String fileName, String extention, String renameTo) {
		File file  = new File(folder + sep + fileName + "." + extention);
		Kernel kernel = new Kernel();
		int[][] arr = kernel.newKernelArray(deviation, halfSize);
		this.kernel = kernel;
		this.kernel.kernelIntValues = arr;
		
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		
		try {
			BufferedImage original = ImageIO.read(file);
			BufferedImage output = ImageIO.read(file);
			this.outPutImage = output;
			int height = original.getHeight();
			int width = original.getWidth();
			
			for(int y = 2; y < height - 2 ; y++ ) {
				final int rowY = y;
				executor.execute(() -> blurRowThreadMethod(original, width, height, rowY, halfSize, fileName, extention, renameTo));
			}
			
			executor.shutdown();
			if(!executor.awaitTermination(50, TimeUnit.SECONDS)) {
				executor.shutdownNow();
				System.out.println("This took to fckn long!");
			}
			
			File outputPath  = new File(folder + sep + renameTo + "." + extention);
			ImageIO.write(this.outPutImage, extention, outputPath);
			
		}catch(IOException  | InterruptedException ex ) {
			ex.printStackTrace();
		}
		
		
	}
	
	public void blurRowThreadMethod(BufferedImage original,int width, int height, int rowY, int halfSize, String fileName, String extention, String renameTo) {
		
			int kernelSize = halfSize * 2 + 1;
		try {
			int topY = (rowY - halfSize < 0) ? 0: rowY - halfSize;
			int heightOfSub = rowY + halfSize - topY;
			int heightOfSub1 = (rowY + halfSize > height - 1) ?  height - 1 - topY: heightOfSub;
			BufferedImage subimage = original.getSubimage(0, topY, width, heightOfSub1 + 1);
			for(int x = 2; x < width - 2 ; x++) {
//				int pixel = subimage.getRGB(x, rowY);
				int redSum = 0;
				int greenSum = 0;
				int blueSum = 0;
				
				int counterY = 0;
				int divideBy = 0;
				
				for(int dY = -halfSize; dY <= halfSize ; dY++ ) {
					int counterX = 0;
					for(int dX = -halfSize ; dX <= halfSize; dX++) {
						
						int nX = x + dX;
						int nY = subimage.getHeight() / 2 - 1 + dY;
						
						int kernelVal = this.kernel.kernelIntValues[counterY][counterX];
						
						
						if(nY >= 0 && nY < subimage.getHeight() && nX >= 0 && nX < subimage.getWidth()) {
							
							divideBy += kernelVal;
							System.out.println(kernelVal + " =  kernelVal");
							
							
							int pixel = subimage.getRGB(nX, subimage.getHeight() / 2 + 1);
							int red = (pixel >> 16) & 0xff;
							int green = (pixel >> 8) & 0xff;
							int blue = pixel & 0xff;
							
							redSum += (red * kernelVal);
							greenSum += (green * kernelVal);
							blueSum += (blue * kernelVal);
							System.out.println(counterY + " = counter y" + counterX + " =  counter x");
						}
						counterX ++;
					}
					counterY ++;
					
				}
				System.out.println(divideBy + " =  divideBy");
				int newRed = redSum / divideBy;
				int newGreen = greenSum / divideBy;
				int newBlue = blueSum / divideBy;
				
				int newPixel = (newRed << 16) | (newGreen << 8) | (newBlue);
				
				outPutImage.setRGB(x, rowY, newPixel);
				
				
			}
			
		}catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}
}
