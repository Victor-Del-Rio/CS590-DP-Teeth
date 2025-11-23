import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.StringTokenizer;

public class Teeth {
    static List<Integer> top;
    static List<Integer> bottom;

    static int[] outputA;
    static int[] outputB;


    // move up or i-1 or repeat b ; move left or j-1 or repeat a; move diagonal or i-1, j-1 or just go next
    enum arrivalPath {
        REPEAT_B, REPEAT_A, SKIP_REPEATS, NONE
    }

    static class ToothColumn {
        int height;
        arrivalPath path;

        ToothColumn(int height, arrivalPath path){
            this.height = height;
            this.path = path;
        }
    }

    public static int recursiveTopDown(List<Integer> a, List<Integer> b) {
        // n - i - a
        int n = a.size();
        int m = b.size();

        // Initialize the 2D array to store memoized calls
        ToothColumn[][] memo = new ToothColumn[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                memo[i][j] = new ToothColumn(-1, arrivalPath.NONE);
            }
            // Arrays.fill(memo[i], new ToothColumn(-1, arrivalPath.NONE)); // No good - same object referenced across the entire row
        }

        int result = minMaxHeight(n - 1, m - 1, a, b, memo);
        constructPath(memo, n-1, m-1);

        return result;
    }
    

    private static int minMaxHeight(int i, int j, List<Integer> a, List<Integer> b, ToothColumn[][] memo) {
        if (memo[i][j].height != -1) {
            return memo[i][j].height;
        }

        int currHeight = (int) a.get(i) + b.get(j);

        if (i == 0 && j == 0) {
            memo[i][j].height = currHeight;
            // No need to override arrivalPath.NONE
            return currHeight;
        }

        int bestPrevHeight = Integer.MAX_VALUE;
        arrivalPath bestPath = arrivalPath.NONE;

        // Check no-repeat option first to avoid bestPath taking the long, but equally 'high' route
        if (i > 0 && j > 0) {  // advance both
            int pathOption = minMaxHeight(i - 1, j - 1, a, b, memo);
            if (pathOption < bestPrevHeight) {
                bestPrevHeight = pathOption;
                bestPath = arrivalPath.SKIP_REPEATS;
            }
        }
        if (i > 0) {  // repeat b[j]
            int pathOption = minMaxHeight(i - 1, j, a, b, memo);
            if (pathOption < bestPrevHeight) {
                bestPrevHeight = pathOption;
                bestPath = arrivalPath.REPEAT_B;
            }
        }
        if (j > 0) {   // repeat a[i]
            int pathOption = minMaxHeight(i, j - 1, a, b, memo);
            if (pathOption < bestPrevHeight) {
                bestPrevHeight = pathOption;
                bestPath = arrivalPath.REPEAT_A;
            }
        }

        memo[i][j].height = Math.max(currHeight, bestPrevHeight);
        memo[i][j].path = bestPath;
        return memo[i][j].height;
    }

    static void constructPath(ToothColumn[][] memo, int i, int j) {
        List<Integer> pathI = new ArrayList<>();
        List<Integer> pathJ = new ArrayList<>();

        // Walk backwards from (n-1, m-1) to (0,0)
        while (true) {
            pathI.add(i);
            pathJ.add(j);

            if (i == 0 && j == 0) {
                break;
            }

            arrivalPath path = memo[i][j].path;
            if (path == null || path == arrivalPath.NONE) {
                // avoid spinning forever
                throw new IllegalStateException("Code is broken, move should have been stored for (" + i + "," + j + ")");
            }

            switch (path) {
                case REPEAT_B:      // came from (i-1, j)
                    i = i - 1;
                    break;
                case REPEAT_A:      // came from (i, j-1)
                    j = j - 1;
                    break;
                case SKIP_REPEATS:  // came from (i-1, j-1)
                    i = i - 1;
                    j = j - 1;
                    break;
            }
        }

        // Currently path is from end -> start; reverse into arrays start -> end
        int len = pathI.size();
        outputA = new int[len];
        outputB = new int[len];

        for (int k = 0; k < len; k++) {
            outputA[k] = top.get(pathI.get(len - 1 - k));
            outputB[k] = bottom.get(pathJ.get(len - 1 - k));
        }
    }


    static void parseInput(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = br.readLine();
        if (line == null) {
            br.close();
            throw new IOException("Input file is empty");
        }

        StringTokenizer st = new StringTokenizer(line);
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        top = new ArrayList<>(n);
        bottom = new ArrayList<>(m);

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < n; i++) {
            top.add(Integer.parseInt(st.nextToken()));
        }
        System.out.println(top);

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < m; i++) {
            bottom.add(Integer.parseInt(st.nextToken()));
        }
        System.out.println(bottom);
        
        br.close();
    }


    static void writeOutput(String path, int maxHeight, int[] top, int[] bottom) throws IOException {
        if (top.length != bottom.length) {
            throw new IllegalArgumentException("We messed up");
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        out.println(maxHeight);
        for (int i = 0; i < top.length; i++) {
            out.println(top[i] + " " + bottom[i]);
        }

        out.flush();
        out.close();
    }

    public static void main(String[] args) throws IOException {

        // TODO change this before JAR
        String cwd = System.getProperty("user.dir");
        String inputPath  = String.valueOf(Path.of(cwd, "input.txt"));
        String outputPath = String.valueOf(Path.of(cwd, "output.txt"));
        System.out.println(inputPath); // TODO remove

        parseInput(inputPath);
        int maxHeight = recursiveTopDown(top, bottom);
        System.out.println(maxHeight);
        writeOutput(outputPath, maxHeight, outputA, outputB);



    }
}




//8 8
//6 4 1 3 5 4 7 2
//2 7 4 5 3 1 4 6

//3 3
//1 3 2
//4 5 2
