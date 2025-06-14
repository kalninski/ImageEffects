package image;
import java.util.Arrays;

public class Sort {
	
	public static void swap(int[] arr, int l, int r) {
//		arr[l] = arr[l] ^ arr[r];
//		arr[r] = arr[l] ^ arr[r];
//		arr[l] = arr[l] ^ arr[r];
		
		int temp = arr[l];
		arr[l] =  arr[r];
		arr[r] = temp;
	}
	
	public static int partition(int[] arr, int low, int high) {
		int i = low - 1;
		int j = low;
		
		while(j < high) {
			if(arr[j] <= arr[high]) {
				i += 1;
				swap(arr, i ,j);
		//		System.out.println(Arrays.toString(arr));
			}
			j ++;
		}
		
		swap(arr, i + 1, high);
		
		return i + 1;
		
	}
	
	public static void sort(int[] arr, int start, int end) {
		if(start < end) {
			int pivot = partition(arr, start, end);
			
	//		System.out.println(Arrays.toString(arr));
			sort(arr, start, pivot - 1);
			sort(arr, pivot + 1, end);
		}
//		return arr;
	}
	
	public static void sortArr(int[] arr) {
		int start = 0;
		int end = arr.length - 1;
		sort(arr, start, end);
	}
	
	

}
