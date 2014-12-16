
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixExample {

    static int n = 278;

    static String[] names = new String[n];// = {"K", "A", "B", "C"};

    static boolean printIterations = true;
    static int iterations = 1000;
    static double epsilon = 0.000000001d;

    public static void main(String[] args) throws FileNotFoundException {

        for (int i = 0; i < n; i++) {
            names[i] = "#" + i;
        }

        double[][] M = loadMatrix();

        double[] v0 = new double[n];
        for (int i = 0; i < n; i++) {
            v0[i] = 1 / 278d;
        }

        double[] E = new double[n];
        for (int i = 0; i < n; i++) {
            if (i == 4) {
                E[i] = 1;
            }
        }
//      double[] E = {1/4d, 1/4d, 1/4d, 1/4d};
        double d = 0.5d;

        double[] df = new double[n]; // = {(1 - d), (1 - d), (1 - d), (1 - d)};
        for (int i = 0; i < n; i++) {
            df[i] = (1 - d);
        }

        double[] delta = new double[n]; //{1, 1, 1, 1};
        for (int i = 0; i < n; i++) {
            delta[i] = 1;
        }

        double[] a = new double[n];

        //printVector(vs0);
        //printVector(multiplyVectorByNumber(vs0, 1 / 4d));
        System.out.println(".....................................................");

        //for (int i = 1; i < iterations; i++) {
        do {
            v0 = vectorAdditon(multiplyMatrixByVector(multiplyMatrixByNumber(M, d), v0),
                    multiplyVectorByNumber(E, (1 - d)));
            //printIteration(v0, i);
            delta = vectorAbsoluteSubtract(v0, a);
            a = v0;
        } while (!vectorSmallerThan(delta, epsilon));
        printVector(normalizeVector(v0));

        printMatrix(M);
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

    private static void printIteration(double[] vs0, int i) {
        if (printIterations) {
            System.out.println("Iteration " + i);
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
            for (int j = 0; j < M.length; j++) {
                l += M[i][j] + "\t";
            }
            System.out.println(l);
        }
    }

    private static void printVector(double[] result) {
        double sum = 0;
        for (int i = 0; i < result.length; i++) {
            sum += result[i];
            System.out.println(names[i] + " " + result[i]);
        }
        System.out.println("Sum " + sum);
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

    private static double[][] loadMatrix() throws FileNotFoundException {
        double[][] result = new double[n][n];

        File f = new File("matrix.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        Scanner scan = null;
        try {
            scan = new Scanner(br);
            String line;

            while (scan.hasNextLine()) {
                line = scan.nextLine().trim();

                String[] arr = line.split(" ");

                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr.length; j++) {
                        result[i][j] = Float.parseFloat(arr[j]);
                    }
                }
            }
        } finally {
            scan.close();
        }
        return result;
    }

}
