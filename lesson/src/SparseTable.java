import java.util.Scanner;

import static java.lang.Integer.max;
import static java.lang.Math.log;
import static java.lang.Math.min;

public class SparseTable {
    public static int[] precalc;
    public static int n;
    public static int[][] st;

    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        n = x.nextInt();
        precalc = new int[n+1];
        st = new int[n][(int)(Math.floor(log(n)/log(2))) + 1];
        int m = x.nextInt();
        int a0 = x.nextInt();
        int u1 = x.nextInt();
        int v1 = x.nextInt();
        precal(n);
        for (int i = 0; i < n; i++) {
            st[i][0] = a0;
            a0 = (23 * a0+21563) % 16714589;
        }
        for (int j =1; (1 << j) - 1 <= n - 1; j++) {
            for (int i = 0; i + (1 << j) - 1 <= n - 1; i++) {
                st[i][j] = Integer.MAX_VALUE;
                int v = i + (1 << (j - 1)) - 1;
                int c = st[i][j-1];
                int d = st[i + (1 << (j - 1)) - 1][j-1];
                st[i][j] = min(st[i][j-1], st[i + (1 << (j - 1))][j-1]);
            }
        }
        int u0 = u1;
        int v0 = v1;
        int r = 0;
        for (int i =0 ; i < m; i++) {
            r = mins(min(u1,v1) - 1, max(u1,v1) - 1);
            u0 = u1;
            v0 = v1;
            u1 = ((17 * u1 + 751 + r + 2 * (i+1)) % n) + 1;
            v1 = ((13 * v1 + 593 + r + 5 * (i+1)) % n) + 1;
        }
        System.out.println(u0 + " " + v0 + " " + r);
    }

    static int mins(int l, int r) {
        int x = precalc[r - l + 1];
        int y = r + 1 - (1 << precalc[r - l+1]);
        int z = st[l][precalc[r-l + 1]];
        return min(st[l][precalc[r-l + 1]], st[r + 1 - (1 << precalc[r - l + 1])][precalc[r - l + 1]]);
    }

    static void precal(int x) {
        precalc[0] = 0;
        for (int i = 1; i < n; i++) {
            while ( (1 << (precalc[i] + 1)) <= i) {
                precalc[i]++;
            }
        }
    }
}
