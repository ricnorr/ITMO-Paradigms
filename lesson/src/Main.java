import java.util.Scanner;

import static java.lang.Integer.min;
import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) {
        int n, x, y, a0, m, z, t, b0;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        x = sc.nextInt();
        y = sc.nextInt();
        a0 = sc.nextInt();
        m = sc.nextInt();
        z = sc.nextInt();
        t = sc.nextInt();
        b0 = sc.nextInt();
        long[] sums = new long[n + 1];
        int co = 2 << 29;
        int co16 = 2 << 15;
        sums[0] = a0;
        for (int i = 1; i < n; i++)
        {
            a0 = ((x * a0) % co16 + y) % co16;
            sums[i] = sums[i-1] + a0;
        }
        int[] c = new int[2];
        int c0;
        int c1;
        if (m != 0) {
            c0 = b0 % n;
        }
        long sum = 0;
        for (int i = 0; i < 2 * m; i += 2) {
            c0 = (b0 % n < 0) ? (b0 % n) + n: (b0 % n) ;
            b0 = (b0 * z % co) + t < 0 ? co + ((b0 * z) % co + t): (((b0 * z) % co + t) %co) % co;
            c1 = (b0 % n < 0) ? (b0 % n) + n: (b0 % n) ;
            b0 = (b0 * z % co) + t < 0 ? co + ((b0 * z) % co + t): (((b0 * z) % co + t) %co) % co;
            if (min(c0, c1) == 0) {
                sum += sums[max(c0, c1)];
            } else {
                sum += sums[max(c0, c1)] - sums[min(c0, c1) - 1];
            }
        }
        System.out.println(sum);
    }
}


