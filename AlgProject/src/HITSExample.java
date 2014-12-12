import java.util.Arrays;


public class HITSExample {

	static String[] names = {"K", "A", "B", "C"};
	static boolean printIterations = true;
	static int iterations = 1000;
	static double epsilon= 0.0001d;
	
	public static void main(String[] args) {

		double[][] L = {{0, 1, 1, 0},
						{1, 0, 1, 0},
						{1, 1, 0, 1},
						{0, 0, 1, 0}};
		double[][] Lt = L.clone(); //because it is symmetric
		
		double[] h = {1, 1, 1, 1};
		double[] a = new double[h.length];
		double[] delta = {1, 1, 1, 1};
		double c = 0.5d;
		double[] U = {1, 0,0,0};
		double[] cU = multiplyVectorByNumber(U, c);
		int count = 1;
		do{
			System.out.println("-------------------------------------------------------------");
			double[] ap = vectorAdditon(multiplyVectorByNumber(multiplyMatrixByVector(Lt, h), c), cU);
			double[] a1 = normalizeVector(ap);
			double[] hp = vectorAdditon(multiplyVectorByNumber(multiplyMatrixByVector(L, a1), c), cU);
			double[] h1= normalizeVector(hp);
			delta=vectorAbsoluteSubtract(a1, a);
			a = a1;
			h = h1;
			System.out.println("Rank: Authority");
			printIteration(a, count);
			System.out.println("Rank: Hub");
			printIteration(h, count++);
	    }while (!vectorSmallerThan(delta,epsilon));
		
/**
		do{
			System.out.println("-------------------------------------------------------------");
			double[] ap = multiplyMatrixByVector(Lt, h);
//			printVector(ap);
			double[] a1 = scaleVector(ap);
//			printVector(a1);
			double[] hp = multiplyMatrixByVector(L, a1);
//			System.out.println("hp:");
//			printVector(hp);
			double[] h1= scaleVector(hp);
			delta=vectorAbsoluteSubtract(a1, a);
			a = a1;
			h = h1;
			System.out.println("Rank: Authority");
			printIteration(a, count);
			System.out.println("Rank: Hub");
			printIteration(h, count++);
	    }while (!vectorSmallerThan(delta,epsilon));
//		printMatrix(L);
//		printVector(h);
 * 
 */
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
	
	private static double[][] matrixMultiplication(double[][] A,double[][] B)
	{

		double[][] C= new double[A.length][A.length];
		double sumProduct=0;
		for(int i= 0;i<A.length;i++){
			for(int j=0; j<B[0].length; j++)
			{
				sumProduct=0.0;
				for(int k=0; k< A[0].length;k++)
				{
					sumProduct += A[i][k]*B[k][j]; 
				}
				C[i][j]=sumProduct;
			}
			
		}
		return C;
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
	private static double[][] transpose(double[][] L){
		double[][] Lt= new double[L.length][L.length];
		
		for(int i=0; i<L.length; i++)
		{
			for(int j =0; j<L.length; j++)
			{
				Lt[i][j]=L[j][i];
			}
		}
		
		return Lt;
		
	}
	private static void printIteration(double[] vs0, int i) {
		if(printIterations){
			System.out.println("iteration: "+i);
			System.out.println();
			printVector(vs0);
		}
	}

	private static double[] multiplyVectorByNumber(double[] v1, double d) {
		double[] r = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			r[i] = v1[i] * d;
		}
		return r;
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

	private static void printVector(double[] result) {
		double sum = 0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
			System.out.println(names[i]+" "+result[i]);
		}
		System.out.println("Sum "+sum);
	}

}
