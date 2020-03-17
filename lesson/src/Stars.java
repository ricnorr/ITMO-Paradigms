import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Scanner;

import static java.lang.Math.*;

public class Stars {

    static int [][][] tree;
    static int n;

    public static void update(int x,int y,int z, int d) {
        for (int i = x; i < n; i = i | (i+1)) {
            for (int j = y; j < n; j = j | (j+1)) {
                for (int k = z; k < n; k = k | (k + 1)) {
                    tree[i][j][k] += d;
                }
            }
        }
    }

    public static int get(int x, int y, int z) {
        int sum = 0;
        for (int i = x; i > - 1; i = (i & (i+1)) - 1) {
            for (int j = y; j > - 1; j = (j & (j+1)) - 1) {
                for (int k = z; k > - 1; k = (k & (k + 1)) - 1) {
                    sum += tree[i][j][k];
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        Scanner x = new Scanner(System.in);
        n = x.nextInt();
        tree = new int[n][n][n];
        while (true) {
            int x1 = x.nextInt();
            if (x1 == 3) {
                break;
            }
            if (x1 == 2) {
                x1 = x.nextInt();
                int y1 = x.nextInt();
                int z1 = x.nextInt();
                int x2 = x.nextInt();
                int y2 = x.nextInt();
                int z2 = x.nextInt();
                System.out.println(get(x2, y2, z2) - get(x1 -1, y2, z2) - get(x2, y1 - 1, z2) + get(x1 - 1, y1 - 1, z2) - get(x2, y2, z1-1) + get(x1 -1, y2, z1 - 1) + get(x2, y1 -1, z1 - 1) - get(x1 -1, y1 -1, z1 - 1));
                continue;
            }
            if (x1 == 1) {
                update(x.nextInt(), x.nextInt(), x.nextInt(), x.nextInt());
            }
        }
    }
}
