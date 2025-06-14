package image;

import java.util.*;
import java.lang.StringBuilder;


public class Kernel {
	
//	public float deviation;
//	public int halfSize;
//	public int meanMu;
	public float[][] kernelValues;
	public int[][] kernelIntValues;
	public int sumOfIntValues;
	
	public Kernel() {
	} 
	
//	public Kernel(float deviation, int halfSize) {
//		this.deviation = deviation;
//		this.halfSize = halfSize;
//	}
	
	public static int abs(int a) {
		if(a < 0) {
			a *= (-1);
		}
		return a;
	}
	
	public static Kernel newKernel(float deviation, int halfSize) {
		StringBuilder krnl = new StringBuilder();
//		int sumOfValues = 0;
		int arrSize = (2 * halfSize + 1);
		Kernel kernel = new Kernel();
		kernel.kernelValues = new float[arrSize][arrSize];
		for(int y = -halfSize; y <= halfSize; y++) {
			for(int x = -halfSize; x <= halfSize; x++) {
				
				int y1 = abs(y);
				int x1 = abs(x);
				kernel.kernelValues[y1][x1] = density(deviation, x, y);
				
				String value = (x == halfSize) ?  String.valueOf(density(deviation, x, y) + "\n"): String.valueOf(density(deviation, x, y) + "  ");
				krnl.append(value);
				
			}
		}
		
		System.out.println(krnl.toString());
		return kernel;
	}
	
	
	public int[][] newKernelArray(float deviation, int halfSize) {
		StringBuilder krnl = new StringBuilder();
		int sum = 0;
		
		int arrSize = (2 * halfSize + 1);
		int[][] arr = new int[arrSize][arrSize];


		for(int y = -halfSize; y <= halfSize; y++) {
			for(int x = -halfSize; x <= halfSize; x++) {
				
				int y1 = abs(y);
				int x1 = abs(x);
				arr[y1][x1] = (int) (density(deviation, x, y) * 100);
				sum += arr[y1][x1];
				String value = (x == halfSize) ?  arr[y1][x1] + "\n": arr[y1][x1] + "  ";
				krnl.append(value);
				
			}
			
		}
		this.kernelIntValues = arr;
		this.sumOfIntValues = sum;
		
		System.out.println(this.sumOfIntValues);
		System.out.println(krnl.toString());
		return arr;
	}
	
	public static final float pi =(float) Math.PI;
	public static final float e =(float) Math.E;
	
	public static float sqrt(float a, int precision) {
		float result = 0;
		float guess = a/2;
		for(int i = 0; i < precision;i++ ) {
			float numerator = guess * guess - a;
			float denominator = guess * 2;
			result = guess - numerator/denominator;
			guess = result;
//			System.out.println(result);
		}
		return result;
	}
	
	
	public static float density(float deviation, float x, float y) {

		float coefficietNormalizer = 1 / (2 * pi * deviation * deviation);
//		System.out.println(result);
		float numerator = x * x + y * y;
		float denominator = 2 * deviation * deviation;
		float exponent = -1 * (numerator/denominator);
		float  fProb=(float) Math.exp(exponent);
		float result = coefficietNormalizer * fProb;
		
//		System.out.println(result);
		return result;
	}
	
}
