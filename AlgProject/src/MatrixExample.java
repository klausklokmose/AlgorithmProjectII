import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class MatrixExample {

	
	static boolean printIterations = false;
	static int iterations = 1000;
	
	public static void main(String[] args)  throws FileNotFoundException, InterruptedException, BrokenBarrierException {
		
		long start = System.currentTimeMillis();
		File f = new File("matrix.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		int NumberOfActors = 278;
		try{
			scan = new Scanner(br);
			double[][] M = populateMatrix(scan,NumberOfActors);
			String[] names = new String[NumberOfActors];
			for (int i = 0; i < names.length; i++) {
				names[i]=String.valueOf(i);
			}
		
//		double[][] M = {{0, 1/2d, 1/3d, 0},
//						{1/2d, 0, 1/3d, 0},
//						{1/2d, 1/2d, 0, 1},
//						{0, 0, 1/3d, 0}};
//		double[][] M2 = {{1/3d, 1/2d, 1/3d, 0},
//						{1/3d, 0, 1/3d, 0},
//						{1/3d, 1/2d, 0, 1},
//						{0, 0, 1/3d, 0}};
//		double[] v0 = {1/4d,1/4d,1/4d,1/4d};
//		double[] v0 = {0, 0, 0, 0};
		
		double[] v0 = new double[NumberOfActors];
//		for (int i = 0; i < v0.length; i++) {
//			v0[i]=1/278d;
//		}
		
		double[] E = new double[NumberOfActors];
		E[4]=1;
		
//		double[] E = {1, 0, 0, 0};
//		double[] E = {1/4d, 1/4d, 1/4d, 1/4d};
		double d = 0.05d;

//		double[] vs0 = {1, 0, 0, 0};
//		double[] df = {(1-d), (1-d), (1-d), (1-d)};
//		
		double[] df = new double[NumberOfActors];
		for (int i = 0; i < df.length; i++) {
			df[i]=(1-d);
		}
//		for (int i = 1; i < iterations; i++) {
//			v0 = vectorAdditon(df, multiplyVectorByNumber(multiplyMatrixByVector(M, v0), d));
//			printIteration(v0, i,names);
//		}
//		printVector(v0,names);
//		printVector(multiplyVectorByNumber(v0, 1/278d),names);		
		System.out.println(".....................................................");
//		
		for (int i = 1; i < iterations; i++) {
			v0 = vectorAdditon(multiplyMatrixByVector(multiplyMatrixByNumber(M, d), v0),
					multiplyVectorByNumber(E, (1-d)));
			printIteration(v0, i,names);
		}
		printVector(v0,names);
//		
//		printMatrix(M);
		}finally{
			scan.close();
		}
	}

	private static void printIteration(double[] vs0, int i, String[] names) {
		if(printIterations){
			System.out.println("vs"+i);
			System.out.println();
			printVector(vs0,names);
		}
	}

	private static double[] multiplyVectorByNumber(double[] v1, double d) {
		double[] r = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			r[i] = v1[i] * d;
		}
		return r;
	}

	private static double[][] populateMatrix(Scanner in,int NumberOfActors){
		String line = "";
		double[][] matrix = new double[NumberOfActors][NumberOfActors];
		
		while (in.hasNextLine()) {
			line = in.nextLine();
	    	String[] s = line.split(" ");
		    
	    	for (int j = 0; j < matrix.length; j++) {
		        for (int i = 0; i < s.length; i++) {
		            matrix[j][i] = Double.parseDouble(s[i]);
		        }
		    }    	
		}
		return matrix;
	}
	
	private static double[] vectorAdditon(double[] v1,
			double[] v2) {
		double[] r = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			r[i] = v1[i] + v2[i];
		}
		return r;
	}

	private static double[] multiplyMatrixByVector(double[][] M, double[] v) {
		double[] result = new double[M.length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				result[i] += M[i][j]*v[j];
			}
		}
		return result;
	}
	
	private static double[][] multiplyMatrixByNumber(double[][] M, double number) {
		double[][] result = new double[M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				result[i][j] = M[i][j]*number;
			}
		}
		return result;
	}

	private static void printMatrix(double[][] M) {
		for (int i = 0; i < M.length; i++) {
			String l = "";
			for (int j = 0; j < M.length; j++) {
				l += M[i][j]+"\t";
			}
			System.out.println(l);
		}
	}

	private static void printVector(double[] result, String[] names) {
		double sum = 0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
			System.out.println(names[i]+" "+result[i]);
		}
		System.out.println("Sum "+sum);
	}

	private static double[] normalizeVector(double[] v){
		double[] v1 = v.clone();
		double[] r = new double[v.length];
		Arrays.sort(v1);
		double maxValue = v1[v.length-1];
		for (int i = 0; i < r.length; i++) {
			r[i] = v[i]/maxValue;
		}
		return r;
	}

}
