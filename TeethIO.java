import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TeethIO {

    public static final class TeethInstance {
        public final int[] top;
        public final int[] bottom;

        public TeethInstance(int[] top, int[] bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }

    public static TeethInstance readTeethInput(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = br.readLine();
        if (line == null) {
            br.close();
            throw new IOException("Input file is empty");
        }

        StringTokenizer st = new StringTokenizer(line);
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int[] top = new int[n];
        int[] bottom = new int[m];

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < n; i++) {
            top[i] = Integer.parseInt(st.nextToken());
        }

        line = br.readLine();
        st = new StringTokenizer(line);
        for (int i = 0; i < m; i++) {
            bottom[i] = Integer.parseInt(st.nextToken());
        }

        br.close();
        return new TeethInstance(top, bottom);
    }

    public static int computeAlignedHeight(int[] top, int[] bottom) {
        int maxHeight = 0;
        for (int i = 0; i < top.length; i++) {
            int h = top[i] + bottom[i];
            if (h > maxHeight) {
                maxHeight = h;
            }
        }
        return maxHeight;
    }

    public static void writeAlignmentToFile(String path, int[] top, int[] bottom) throws IOException {
        if (top.length != bottom.length) {
            throw new IllegalArgumentException("We messed up");
        }

        int height = computeAlignedHeight(top, bottom);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));

        out.println(height);
        for (int i = 0; i < top.length; i++) {
            out.println(top[i] + " " + bottom[i]);
        }

        out.flush();
        out.close();
    }
}