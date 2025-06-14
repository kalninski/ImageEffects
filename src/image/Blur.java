package image;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Blur {
	
	private BufferedImage image;
	private static String sep = File.separator;
	private static String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
	
	public Blur() {
		
	}
	
	public void boxBlur(String fileName, String extention, String newName, int blurriness) {
		File file  = new File(folder + sep + fileName + "." + extention);
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(processors);
		
		try {
			BufferedImage original = ImageIO.read(file);
			BufferedImage subimage = original.getSubimage(0, 0, original.getWidth(), original.getHeight());
			this.image = original;
			int height = original.getHeight();
			
			for(int y = 0; y < height; y++) {
				final int row = y;
				executor.execute(() -> blurRow(row, subimage, blurriness));
			}
			
			executor.shutdown();
			
			while(!executor.awaitTermination(15, TimeUnit.SECONDS)) {
				System.out.println("Waiting for a thread to finish");
				executor.shutdownNow();
				
			}
			
			File blurredImage = new File(folder + sep + newName + "." + extention);
			ImageIO.write(original, extention, blurredImage);
			
			
			
		}catch(IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void blurRow(int row, BufferedImage img, int blurriness) {
 
		for(int x = 0; x < img.getWidth(); x++) {
			int currPix = img.getRGB(x, row);
			int redSum = (currPix >> 16) & 0xFF;
			int greenSum = (currPix >> 8) & 0xFF;
			int blueSum = (currPix) & 0xFF;
			
			int count = 1;
			for(int dY = -blurriness; dY <= blurriness; dY++) {
				for(int dX = -blurriness; dX < blurriness; dX++) {
					int nX = x + dX;
					int nY = row + dY;
					if(nY >= 0 && nY < img.getHeight() && nX >= 0 && nX < img.getWidth()) {
						int neighPix = img.getRGB(nX, nY);
						redSum += (neighPix >> 16) & 0xff;
						greenSum += (neighPix >> 8) & 0xff;
						blueSum += (neighPix) & 0xff;
						count++;
					}
				}
			}
			
			int newRed = redSum / count;
			int newGreen = greenSum / count;
			int newBlue = blueSum / count;
			
			int newPixel = (newRed << 16) | (newGreen << 8) | (newBlue);
			
			this.image.setRGB(x, row, newPixel);
			
			
		}

	}
	
}
