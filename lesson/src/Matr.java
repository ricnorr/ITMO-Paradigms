import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.min;
import static java.lang.Math.*;

public class Matr {
    static int n;
    static class Node {
        int data;
        int upd;
        int l;
        int r;
        int i;
        public Node(int d, int l, int r) {
            this.data = d;
            this.l = l;
            this.i = r;
            this.r = r;
            upd = - 100;
        }
    }

    static int beg;
    static int leaveNum;
    static Node[] tree;

    static class Pair {
        int a1;
        int a2;
        public Pair(int a1, int a2) {
            this.a1 = a1;
            this.a2 = a2;
        }
    }

    public static void push(int i) {
        if (tree[i].upd == -100) {return;}
        if (tree[i].data < tree[i].upd) {
            tree[i].data = tree[i].upd;
        }
        if (i < leaveNum) {
            tree[i*2+1].upd = max(tree[i*2+1].upd, (tree[i].upd));
            tree[i*2+2].upd = max(tree[i*2+2].upd, (tree[i].upd));
        }
        tree[i].upd = -100;
    }

    public static void update(int i, int c, int l, int r) {
        push(i);
        if (tree[i].r <= l || tree[i].l >=r){
            return;
        }
        if (l <= tree[i].l && tree[i].r <=r){
            tree[i].upd = max(tree[i].upd, c);
            return;
        }
        update(i*2 + 1, c, l, r);
        update(i*2 + 2, c, l, r);
        int left;
        int right;
        left = tree[i*2 + 1].upd == -100 ? tree[i*2 + 1].data : tree[i*2 + 1].upd;
        if (tree[i*2 + 1].upd > -1 && tree[i * 2 + 1].upd < tree[i * 2 + 1].data) {
            left = tree[i * 2 + 1].data;
        }
        right = tree[i*2 + 2].upd == -100 ? tree[i*2 + 2].data : tree[i*2 + 2].upd;
        if (tree[i*2 + 2].upd > -1 && tree[i * 2 + 2].upd < tree[i * 2 + 2].data) {
            right = tree[i * 2 + 2].data;
        }
        if (left < right) {
            tree[i].data = left;
            tree[i].i = tree[i*2+1].i;
        } else {
            tree[i].data = right;
            tree[i].i = tree[i*2+2].i;
        }
    }

    public static void build(int i, int l, int r) {
        if (i >= leaveNum) {
            tree[i] = new Node(0, l, r);
        } else {
            build(i*2+1, l, (r + l) / 2);
            build(i*2 + 2, (r+l) / 2, r);
                tree[i] = new Node(tree[i*2 + 2].data, l, r);
                tree[i].i = tree[i*2+2].i;
        }
    }


    public static Pair find(int l, int r, int i) {
        push(i);
        if (tree[i].r <= l || tree[i].l >=r){
            return new Pair(Integer.MAX_VALUE,0);
        }
        if (l <= tree[i].l && tree[i].r <=r){
            return new Pair(tree[i].data, tree[i].i);
        }
        Pair a1 = find(l,r,i*2+1);
        Pair a2 = find(l,r, i*2+2);
        if (a1.a1 < a2.a1) {
            return a1;
        } else {
            return a2;
        }

    }

    public static void main(String[] args) {
        try {
            StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
            in.nextToken();
            n = (int)in.nval;
            in.nextToken();
            int m = (int)in.nval;
            int k = (int) ceil(log(n) / log(2)) + 1;
            leaveNum = (int) (pow(2, k - 1)) - 1;
            beg = 0;
            tree = new Node[(int) pow(2, k) - 1];
            beg = 0;
            build(0, -1, (int)Math.pow(2, k - 1) - 1);
            for (int i = 0; i < m; i++) {
                in.nextToken();
                String s = in.sval;
                if (s.equals("defend")) {
                    in.nextToken();
                    int a = (int) in.nval;
                    in.nextToken();
                    int b = (int) in.nval;
                    in.nextToken();
                    int c = (int) in.nval;
                    update(0, c, a - 2, b - 1);
                } else {
                    in.nextToken();
                    int a = (int) in.nval;
                    in.nextToken();
                    int b = (int) in.nval;
                    Pair res = find(a - 2, b - 1, 0);
                    System.out.println(res.a1 + " " + (res.a2 + 1));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}