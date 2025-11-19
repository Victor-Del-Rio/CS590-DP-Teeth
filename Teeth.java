

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Teeth {

    public static int recursive(List<Integer> a, List<Integer> b) {
        int n = a.size();
        int m = b.size();

        int[][] memo = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(memo[i], -1);
        }

        return minMaxHeight(n - 1, m - 1, a, b, memo);
    }
    

    private static int minMaxHeight(int i, int j,
                                     List<Integer> a,
                                     List<Integer> b,
                                     int[][] memo) {
        if (memo[i][j] != -1L) {
            return memo[i][j];
        }

        int currHeight = (int) a.get(i) + b.get(j);

        if (i == 0 && j == 0) {
            memo[i][j] = currHeight;
            return currHeight;
        }

        int bestPrev = Integer.MAX_VALUE;

        if (i > 0) { // repeat b[j]
            bestPrev = Math.min(bestPrev, minMaxHeight(i - 1, j, a, b, memo));
        }
        if (j > 0) { // repeat a[i]
            bestPrev = Math.min(bestPrev, minMaxHeight(i, j - 1, a, b, memo));
        }
        if (i > 0 && j > 0) { // advance both
            bestPrev = Math.min(bestPrev, minMaxHeight(i - 1, j - 1, a, b, memo));
        }

        memo[i][j] = Math.max(currHeight, bestPrev);
        return memo[i][j];
    }

    // Optional: convenience overload for arrays
//    public static int recursive(int[] aArr, int[] bArr) {
//        List<Integer> a = new ArrayList<>(aArr.length);
//        List<Integer> b = new ArrayList<>(bArr.length);
//        for (int x : aArr) a.add(x);
//        for (int x : bArr) b.add(x);
//        return recursive(a, b);
//    }


    static TeethInstance readTeethInput(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = br.readLine();
        if (line == null) {
            br.close();
            throw new IOException("Input file is empty");
        }

        StringTokenizer st = new StringTokenizer(line);
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        List<Integer> top = new ArrayList<>(n);
        List<Integer> bottom = new ArrayList<>(m);

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < n; i++) {
            top.add(Integer.parseInt(st.nextToken()));
        }

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < m; i++) {
            bottom.add(Integer.parseInt(st.nextToken()));
        }

        br.close();
        return new TeethInstance(top, bottom);
    }


    public void writeAlignmentToFile(String path, int height, int[] top, int[] bottom) throws IOException {
        if (top.length != bottom.length) {
            throw new IllegalArgumentException("We messed up");
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        out.println(height);
        for (int i = 0; i < top.length; i++) {
            out.println(top[i] + " " + bottom[i]);
        }

        out.flush();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        List<Integer> a = new ArrayList<>(Arrays.asList(2, 7, 4, 5, 3, 1, 4, 6));
        List<Integer> b = new ArrayList<>(Arrays.asList(6, 4, 1, 3, 5, 4, 7, 2));

        List<Integer> c = new ArrayList<>(Arrays.asList(2, 3));
        List<Integer> d = new ArrayList<>(Arrays.asList(1, 4, 1));

        List<Integer> e = new ArrayList<>(Arrays.asList(1, 3, 2));
        List<Integer> f = new ArrayList<>(Arrays.asList(4, 5, 2));


        if (args.length < 1) {
            System.out.println("Usage: java ReadInput <input.txt>");
            return;
        }
        String inputFile = args[0];
        System.out.println(inputFile);
//        TeethInstance readTeethInput(String path) throws IOException {
        
        TeethInstance teeth = readTeethInput(inputFile);
        System.out.println(recursive(teeth.top, teeth.bottom));
//

//        String contents = new String(Files.readAllBytes(Paths.get(inputFile)));
//        System.out.println(contents);


//        TeethInstance inst = readTeethInput(inputFile);


        System.out.println(recursive(a, b));
        System.out.println(recursive(c, d));
        System.out.println(recursive(e, f));

    }
}



final class TeethInstance {
    public final List<Integer> top;
    public final List<Integer> bottom;

    public TeethInstance(List<Integer> top, List<Integer> bottom) {
        this.top = top;
        this.bottom = bottom;
    }
}



