import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class HITSExample {

	static String[] names = new String[300];
	// static String[] names = {"K", "A", "B", "C"};
	// static String[] names = {"M1", "M2", "A", "B", "C"};
	static boolean printIterations = false;
	static double epsilon = 0.000000001d;
	static double c = 0.5d;

	public static void main(String[] args) throws FileNotFoundException,
			InterruptedException, BrokenBarrierException {

		File f = new File("matrix11movies.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		Scanner scan = null;
		int NumberOfActors = 0;

		try {
			scan = new Scanner(br);
			// get the actors IDs and the number of actors
			if (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				String[] s = line.split(" ");
				NumberOfActors = s.length;
				names = new String[NumberOfActors];

				for (int i = 0; i < s.length; i++) {
					names[i] = s[i];
				}
			}
			double[][] L = populateMatrix(scan, NumberOfActors);

			// double[][] L = {{0, 1, 1, 0},
			// {1, 0, 1, 0},
			// {1, 1, 0, 1},
			// {0, 0, 1, 0}};
			// double[][] L = {{0, 0, 1, 1, 0},
			// {0, 0, 0, 1, 1},
			// {0,0,0,0,0},
			// {0, 0, 0, 0,0},
			// {0, 0, 0, 0,0}};
			// double[][] Lt = transpose(L);
			// printMatrix(L);
			// System.out.println();
			// printMatrix(Lt);
			double[][] Lt = L.clone(); // because it is symmetric

			// double[] h = {1, 1, 1, 1};
			double[] h = new double[NumberOfActors];
			for (int i = 0; i < h.length; i++) {
				h[i] = 1.0;
			}
			// double[] h = {1, 1, 1, 1, 1};
			double[] a = new double[h.length];
			// double[] delta = {1, 1, 1, 1};

			double[] delta = new double[NumberOfActors];
			for (int i = 0; i < h.length; i++) {
				h[i] = 1.0;
			}

			// double[] delta = {1, 1, 1, 1, 1};

			// double[] U = {1, 0,0,0}; //bacon
			double[] U = new double[NumberOfActors];
			for (int i = 0; i < names.length; i++) {
//				if (names[i].equals("" + 22591)) {
//					U[i] = 1;
//				}
				U[i] = 1;
			}

			// double[] U = {1, 1, 1, 1, 1};
			double[] cU = multiplyVectorByNumber(U, c);
			int count = 1;
			long start = System.currentTimeMillis();
			do {
//				System.out
//						.println("-------------------------------------------------------------");
				double[] a1 = vectorAdditon(
						multiplyVectorByNumber(
								normalizeVector(multiplyMatrixByVector(Lt, h)),
								c), cU);
				double[] h1 = vectorAdditon(
						multiplyVectorByNumber(
								normalizeVector(multiplyMatrixByVector(L, a1)),
								c), cU);
				delta = vectorAbsoluteSubtract(a1, a);
				a = a1;
				h = h1;
//				System.out.println("Rank: Authority");
//				printIteration(a, count);
//				System.out.println("Rank: Hub");
//				printIteration(h, count++);
				count++;
			} while (!vectorSmallerThan(delta, epsilon));
			System.out.println("Time to complete " + count + " rounds: "
					+ (System.currentTimeMillis() - start) + " ms");
			printVector(a);
		} finally {

		}
	}

	private static double[] vectorAbsoluteSubtract(double[] v1, double[] v2) {
		double[] r = new double[v1.length];
		for (int i = 0; i < v1.length; i++) {
			r[i] = Math.abs(v1[i] - v2[i]);
		}
		return r;
	}

	private static boolean vectorSmallerThan(double[] v, double e) {

		boolean result = true;
		for (int i = 0; i < v.length; i++) {
			result = result && v[i] < e;
		}
		return result;
	}

	private static double[][] populateMatrix(Scanner in, int NumberOfActors) {
		String line = "";
		double[][] matrix = new double[NumberOfActors][NumberOfActors];
		int row = 0;
		while (in.hasNextLine()) {
			line = in.nextLine().trim();
			String[] s = line.split(" ");
			for (int j = 0; j < s.length; j++) {
				if (Double.parseDouble(s[j]) != 0) {
					// matrix[row][j] = Double.parseDouble(s[j]);
					matrix[row][j] = 1;
				}
			}
			row++;
		}
		return matrix;
	}

	private static double[][] matrixMultiplication(double[][] A, double[][] B) {

		double[][] C = new double[A.length][A.length];
		double sumProduct = 0;
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < B[0].length; j++) {
				sumProduct = 0.0;
				for (int k = 0; k < A[0].length; k++) {
					sumProduct += A[i][k] * B[k][j];
				}
				C[i][j] = sumProduct;
			}

		}
		return C;
	}

	private static double[] normalizeVector(double[] v) {
		double[] v1 = v.clone();
		double[] r = new double[v.length];
		Arrays.sort(v1);
		double maxValue = v1[v.length - 1];
		for (int i = 0; i < r.length; i++) {
			r[i] = v[i] / maxValue;
		}
		return r;
	}

	private static double[][] transpose(double[][] L) {
		double[][] Lt = new double[L[0].length][L.length];

		for (int i = 0; i < L.length; i++) {
			for (int j = 0; j < L.length; j++) {
				Lt[i][j] = L[j][i];
			}
		}

		return Lt;

	}

	private static void printIteration(double[] vs0, int i) {
		if (printIterations) {
			System.out.println("iteration: " + i);
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

	private static double[] vectorAdditon(double[] v1, double[] v2) {
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
				result[i] += M[i][j] * v[j];
			}
		}
		return result;
	}

	private static double[][] multiplyMatrixByNumber(double[][] M, double number) {
		double[][] result = new double[M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				result[i][j] = M[i][j] * number;
			}
		}
		return result;
	}

	private static void printMatrix(double[][] M) {
		for (int i = 0; i < M.length; i++) {
			String l = "";
			for (int j = 0; j < M[0].length; j++) {
				l += M[i][j] + "\t";
			}
			System.out.println(l);
		}
	}

	private static void printVector(double[] result) {
		double sum = 0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
			System.out.println(names[i] + "\t" + result[i]);
		}
		System.out.println("\t\tSum " + sum);
	}

}
