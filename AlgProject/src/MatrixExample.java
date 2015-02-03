import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;


public class MatrixExample {
	static double epsilon = 0.000000001d;
	static double d = 0.5d;
	final static int Bacon = 1;
	
	static boolean printIterations = false;
	static int NumberOfActors = 0;
	static String[] names = null;
	static double[][] M = null;
	
	public static void main(String[] args)  throws FileNotFoundException, InterruptedException, BrokenBarrierException {
		File f = new File("matrix100movies.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		int count  = 0;
		try{
			scan = new Scanner(br);
			
			//get the actors IDs and the number of actors 
			if (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
		    	String[] s = line.split(" ");
		    	NumberOfActors = s.length;
		    	names = new String[NumberOfActors];
		    	
		    	for (int i = 0; i < s.length; i++) {
					names[i] = s[i];
				}
			}
			
			M = populateMatrix(scan,NumberOfActors);
			
		}finally{
			scan.close();
		}
//		names = new String[]{"KB", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
//		NumberOfActors = names.length;
//		double[][] M = {{0,		1/2d, 	1/3d, 	1/2d},
//						{1/3d, 	0, 		1/3d, 	0},
//						{1/3d, 	1/2d, 	0, 		1/2d},
//						{1/3d, 	0, 		1/3d, 	0}};
//		double[][] M = {{0.00,0.50,0.33,0.00,0.00,0.33,0.00,0.00,0.00,0.33,0.00},
//				{0.25,0.00,0.33,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00},
//				{0.25,0.50,0.00,0.33,0.00,0.00,0.00,0.00,0.00,0.00,0.00},
//				{0.00,0.00,0.33,0.00,0.33,0.00,0.00,0.00,0.00,0.00,1.00},
//				{0.00,0.00,0.00,0.33,0.00,0.33,0.50,0.00,0.00,0.00,0.00},
//				{0.25,0.00,0.00,0.00,0.33,0.00,0.50,0.00,0.00,0.00,0.00},
//				{0.00,0.00,0.00,0.00,0.33,0.33,0.00,0.00,0.00,0.00,0.00},
//				{0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.33,0.00},
//				{0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.33,0.00},
//				{0.25,0.00,0.00,0.00,0.00,0.00,0.00,1.00,1.00,0.00,0.00},
//				{0.00,0.00,0.00,0.33,0.00,0.00,0.00,0.00,0.00,0.00,0.00}};
		
		double[] v0 = new double[NumberOfActors];
		for (int i = 0; i < v0.length; i++) {
			v0[i]=((double)1/(double)NumberOfActors);
		}
		
		double[] E = new double[NumberOfActors];
		for (int i = 0; i < names.length; i++) {
			if(names[i].equals(""+22591)){
				E[i] = Bacon;
			}
//			E[i] = (double)1/(double)NumberOfActors;
		}
		
//		double[] E = {1, 0, 0, 0};
//		double[] E = {1, 0, 0, 0,0,0,0,0,0,0,0};

		System.out.println(".....................................................");

		double[] delta = new double[NumberOfActors];
		initializeDelta(delta);
	
		long start = System.currentTimeMillis();
		do{
            double[] vsi=vectorAdditon(multiplyMatrixByVector(multiplyMatrixByNumber(M, d), v0),
			multiplyVectorByNumber(E, (1-d)));
            
            delta=vectorAbsoluteSubtract(vsi, v0);
            //System.out.println("Delta");
            //printVector(delta);
//            System.out.println("Rank");
//            printIteration(vsi, count++);
            count++;
            v0=vsi;
        
        }while (!vectorSmallerThan(delta,epsilon));

		System.out.println("Time to complete "+count+" rounds: "+(System.currentTimeMillis()-start)+" ms");
		printVector(v0, names);
		
//		
//		printMatrix(M);
//		printVector(normalizeVector(v0), names);
	}

	private static double[] vectorAbsoluteSubtract(double[] v1,
			double[] v2) {
		double[] r = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			r[i] = Math.abs(v1[i] - v2[i]);
		}
		return r;
	}
	private static boolean vectorSmallerThan(double[] v, double e){
        
        boolean result = true;
        for (int i = 0; i < v.length; i++) {
            result=result && v[i]<e;
	}
        return result;
    }
	private static void initializeDelta(double[] delta) {
		for (int i = 0; i < delta.length; i++) {
			delta[i] = 1;
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
		int row = 0;
		while (in.hasNextLine()) {
			line = in.nextLine().trim();
	    	String[] s = line.split(" ");
		        for (int j = 0; j < s.length; j++) {
		        	if(Double.parseDouble(s[j]) != 0){
		        		matrix[row][j] = Double.parseDouble(s[j]);
//		        		matrix[row][j] = 1;
		        	}
		        }
		        row++;
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
			System.out.println(names[i]+"\t"+result[i]);
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
