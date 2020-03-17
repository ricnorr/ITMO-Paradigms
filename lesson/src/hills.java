import java.math.BigInteger;
import java.util.Scanner;

import static java.lang.Math.*;

public class hills {

    static int beg;
    static int leaveNum;
    static long[] upd;
    static long[] ind;
    //static int[] tree;

    public static void build(long l, long r, int i, long[] tree, long[] lefts, long[] rights, long[] a) {
        if (i >= leaveNum) {
            lefts[i] = beg - 1;
            rights[i] = beg;
            tree[i] = 0;
            ind[i] = i - leaveNum;
            beg += 1;
        } else {
            build(l, (l + r) / 2, i * 2 + 1, tree, lefts, rights, a);
            build((l + r) / 2, r, i * 2 + 2, tree, lefts, rights, a);
            lefts[i] = l;
            rights[i] = r;
            if (tree[i * 2 + 1] > tree[i * 2 + 2]) {
                tree[i] = tree[i*2 + 1];
                ind[i] = ind[i*2 + 1];
            } else {
                tree[i] = tree[i*2 + 2];
                ind[i] = ind[i*2 + 2];
            }
        }
        upd[i] = 0L;
    }

    public static void update(int i,int l,int r, long x, long tree[], long lefts[], long[] rights, int mark) {
        push(tree, i);
        if (rights[i] <= l || lefts[i] >=r) {
            return;
        }
        else if (l <= lefts[i] && rights[i] <=r){
            upd[i] += x;
            return;
        }
        else {
            update(i * 2 + 1, l, r, x, tree, lefts, rights, mark);
            update(i * 2 + 2, l, r, x, tree, lefts, rights, mark);
            if (tree[i*2+1] + upd[i*2+1] > tree[i*2+2] + upd[i*2 + 2]) {
                tree[i] = tree[i*2 + 1] + upd[i*2 + 1];
                ind[i] = ind[i*2 + 1];
            } else {
                tree[i] = tree[i*2 + 2] + upd[i*2 + 2];
                ind[i] = ind[i*2 + 2];
            }
        }
    }

    public static class Pair {
        long a;
        long b;

        public Pair(long a, long b) {
            this.a = a;
            this.b = b;
        }
    }
    public static Pair find(long l, long r, int i, long[] rights, long[] lefts, long[] tree) {
        push(tree, i);
        if (rights[i] <= l || lefts[i] >=r){
            return new Pair(Long.MIN_VALUE, 0);
        }
        else if (l <= lefts[i] && rights[i] <=r){
            return new Pair(tree[i], ind[i]);
        }
        else{
            Pair left = find(l, r, i * 2 + 1, rights, lefts, tree);
            Pair right = find(l, r, i * 2 + 2, rights, lefts, tree);
            if (left.a > left.a) {
                return left;
            } else {
                return right;
            }
        }
    }

    public static void push(long[] tree ,int i) {
        if (upd[i] == 0) {
            return;
        }
        tree[i] += upd[i];
        if (i >= leaveNum) {
            upd[i] = 0L;
            return;
        }
        upd[i*2+1] += upd[i];
        upd[i*2 + 2] += upd[i];
        upd[i] = 0L;
    }

    public static void main(String[] args) {
        int n;
        Scanner x = new Scanner(System.in);
        n = x.nextInt();
        int k = (int)ceil(log(n) / log(2)) + 1;
        long[] a = new long[(int) (pow(2, k-1))];
        upd = new long[(int)(pow(2, k)) - 1];
        long[] tree = new long[ (int) pow(2, k) - 1];
        long[] lefts = new long[ (int)(pow(2, k)) - 1];
        long[] rights = new long[ (int)(pow(2, k)) - 1];
        leaveNum = (int)(pow(2, k - 1)) - 1;
        beg = 0;
        build(-1, a.length, 0, tree, lefts, rights, a);
        //builded
        String line;
        while (true) {
            String s = x.next();
            if (s.equals("E")) {
                break;
            }
            if (s.equals("Q")) {

            }
            if (s.equals("I")) {
                int j = x.nextInt();
                Pair p = find(-1, n-1, 0, rights, lefts, tree);
                // все остальное норм
            }
        }
    }
}
