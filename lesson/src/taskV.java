import java.math.BigInteger;
import java.util.Scanner;

import static java.lang.Math.*;

public class taskV {
    static int beg;
    static int leaveNum;
    static int a0;
    static int n;
    //static int[] tree;

    public static void build(int l, int r, int i, int[] tree, int[] lefts, int[] rights) {
        if (i >= leaveNum + n) {
            return;
        }
        if (i >= leaveNum)  {
            lefts[i] = beg - 1;
            rights[i] = beg;
            tree[i] = a0;
            a0 = (23* a0 + 21563) % 16714589;
            beg += 1;
        } else {
            build(l, (l + r) / 2, i * 2 + 1, tree, lefts, rights);
            build((l + r) / 2, r, i * 2 + 2, tree, lefts, rights);
            lefts[i] = l;
            rights[i] = r;
            tree[i] = min(tree[i * 2 + 1], tree[i * 2 + 2]);
        }
    }

    public static int find(long l, long r, int i, int[] rights, int[] lefts, int[] tree) {
        if (i >= leaveNum + n) {
            return Integer.MAX_VALUE;
        }
        if (rights[i] <= l || lefts[i] >=r){
            return Integer.MAX_VALUE;
        }
        else if (l <= lefts[i] && rights[i] <=r){
            return tree[i];
        }
        else{
            return min(find(l, r, i * 2 + 1, rights, lefts, tree) , find(l, r, i * 2 + 2, rights, lefts, tree));
        }
    }



    public static void main(String[] args) {
        //int n;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        int k = (int)ceil(log(n) / log(2)) + 1;
        int size = (int) (pow(2, k-1));
        int[] tree = new int[ (int) pow(2, k) - 1];
        int[] lefts = new int[ (int)(pow(2, k)) - 1];
        int[] rights = new int[ (int)(pow(2, k)) - 1];
        leaveNum = (int)(pow(2, k - 1)) - 1;
        beg = 0;
        int m = sc.nextInt();
        a0 = sc.nextInt();
        build(-1, n-1, 0, tree, lefts, rights);
        //builded
        long u1 = sc.nextLong();
        long v1 = sc.nextLong();
        for (int i = 1; i < m + 1; i++) {
            int t = find(min(u1,v1) - 2, max(u1,v1) - 1,0, rights, lefts, tree);
            if (i == m) {
                System.out.println(u1 + " " + v1 + " " + t);
            } else {
                u1 = ((17 * u1 + 751 + t + 2 * i) % n) + 1;
                v1 = ((13 * v1 + 593 + t + 5 * i) % n) + 1;
            }

        }
    }
}