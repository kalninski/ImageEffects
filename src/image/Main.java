package image;

import java.net.URL;
import java.time.Clock;
import java.net.MalformedURLException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;
import java.io.*;

public class Main {
	public static String sep = File.separator;
	

	public static void main(String[] args) {
		
		String folder = "C:" + sep + "Users" + sep + "Toms" + sep + "Desktop" + sep + "ImageEXPERIMENTS";
		Clock clock = Clock.systemDefaultZone();
		
//		PixelSort.sortAllRows("385888981_3009431119187812_9203513556659327631_n", "jpg");
//		PixelSort.sortDarker("385888981_3009431119187812_9203513556659327631_n", "jpg", 180, 180, 180);
//		PixelSort.sortVertically("382946456_731049572372895_1363472189920677209_n - Copy", "jpg", "SortedHouse");
		long mill1 = clock.millis();
//		ThreadForImage thread = new ThreadForImage();
//		ThreadForImage.rotateImage("Old_Painting_Copy", "jpg", "ROTATED_IMAGE");
//		PixelSort.sortAllRows("Old_Painting_Copy", "jpg");
//		thread.sortRows("Old_Painting_Copy", "jpg", "PARALLEL_SORT");
//		thread.sortVertical("Old_Painting_Copy", "jpg", "V_PARALLEL_SORT");
//		thread.sortSpecVertical("1956-bmw-isetta-300-cabrio", "jfif", "_0.0.0.50.255.255_", 0, 0, 0, 50, 255, 255);
//		PixelSort.sortSpecificVertically("276102493_10222667339575277_1161656941377277818_n", "jpg", "WTF_BUG", 50, 50, 50, 250, 250, 250);
//		System.out.println(Convolution.pow(2, 7));
//		System.out.println(Convolution.factorial(0));
//		System.out.println(Convolution.cos(Math.PI, 20));
//		System.out.println(Convolution.sin(Math.PI, 20));
//		Convolution.color(200, 200, "BlueColorSquare", "jpeg", 0x4282bd);
//		Convolution.colorCircle(1500, 1500, 1000, 3000, 3000, "Circle", "jpg", 0x4282bd, 0xffffff);
//		Convolution.colorCircleThreads(1500, 1500, 1000, 3000, 3000, "CircleThreadsCol_AVG1", "jpg", 0x4282bd, 0xd5df20);
//		Convolution.blurBasic("Circle", "jpg", "BLUR_Circle");
//		Blur blur = new Blur();
//		blur.boxBlur("Circle", "jpg", "CIRCLE_LVL_7", 7);
//		DensityF.sqrt(400569, 15);
//		Kernel.density(0.84089642f, -2, -2);
//		Kernel kernel = new Kernel();
//		Kernel.newKernel(1.0f, 2); 
//		kernel.newKernelArray(1.0f, 2); 
//		GaussianBlur g = new GaussianBlur();
//		g.blurGaussian("GaussianBlurExperiment", "jpg", "GAUSSIAN_Circle", 1.0f, 3);
//		g.gaussianBlur(1.0f, 2, "1.-36-638dc52adb219__700_img_63a3927dde666", "png", "1.-36-638dc52adb219__700_img_63a3927dde666_GAUSS");
//		g.kernel = kernel;
//		g.kernel.newKernelArray(1.0f, 2);
		Gauss gauss = new Gauss();
		gauss.gaussBlur2(1.8, 5, "1BCB2BDE-21FF-49D7-B049-161900661609", "jpeg", "1BCB2BDE-21FF-49D7-B049-161900661609_Gauss_dev1.8_rad11x11");
//		System.out.println(Statistics.factorial(0));
//		System.out.println(Statistics.factorial(5));
//		Statistics.erf(2);
//		Statistics.fromInfToX(2);
//		Statistics.fromInfToX(0);
//		Statistics.fromVal1ToVal2(-1, 1);
//		Statistics.probabilityCDF(40, 60, 50, 5);
//		Sobel s = new Sobel();
	//	s.sobelEdges("7A589FD0-0DB7-4A5A-9DC5-A7DFBE6EDE03", "jpeg", "SOBEL");
//		int[] arr = Gauss.kernel(0.84089642, 5);
//		System.out.println(Arrays.toString(arr));
		long mill2 = clock.millis();
		System.out.println("Time  = " + (mill2 - mill1));
		

		
		

		

		

	}

}
