import java.io.*;

import static java.lang.Integer.min;
import static java.lang.Math.*;
import static java.lang.Math.log;

public class Cars {

    static class Node {
        int d;
        int i;
        int l;
        int r;
        public Node(int d, int i, int l, int r) {
            this.d = d;
            this.i = i;
            this.l = l;
            this.r = r;
        }
    }

    static int leaveNum;
    static Node[] tree;
    static int n;

    public static void build(int i, int l, int r) {
        if (i >= leaveNum + n) {
            tree[i] = new Node(Integer.MAX_VALUE, Integer.MAX_VALUE, l, r);
        }
        if (i >= leaveNum) {
            tree[i] = new Node(0, i - leaveNum, i - leaveNum - 1, i - leaveNum);
        } else {
            build(i*2+1, l, (r + l) / 2);
            build(i*2 + 2, (r+l) / 2, r);
            tree[i] = new Node(min(tree[i*2+1].d, tree[i*2+2].d), min(tree[i*2+1].i,tree[i*2 + 2].i), l, r);
        }
    }

    public static void update(int i, int x) {
        tree[i].d = x;
        tree[i].i = x == 0 ? tree[i].l + 1 : Integer.MAX_VALUE;
        while (i > 0) {
            i = (i-1) / 2;
            tree[i].i = min(tree[i*2 + 1].i, tree[i*2 + 2].i);
        }
    }

    public static int find(int l, int r, int i) {
        if (tree[i].r <= l || tree[i].l >=r){
            return Integer.MAX_VALUE;
        }
        if (l <= tree[i].l && tree[i].r <=r){
            return tree[i].i;
        }
        return min(find(l, r, i * 2 + 1) , find(l, r, i * 2 + 2));
    }

    public static void main(String[] args) {
        try {
            StreamTokenizer in = new StreamTokenizer(new BufferedReader(new FileReader("parking.in")));
            PrintWriter out = new PrintWriter("parking.out");
            in.nextToken();
            n = (int)in.nval;
            in.nextToken();
            int m = (int)in.nval;
            int k = (int) ceil(log(n) / log(2)) + 1;
            leaveNum = (int) (pow(2, k - 1)) - 1;
            tree = new Node[(int) pow(2, k) - 1];
            build(0, -1, (1 << (k - 1)) - 1);
            for (int i = 0; i < m ; i ++ ) {
                in.nextToken();
                String s = in.sval;
                if (s.equals("enter")) {
                    in.nextToken();
                    int x = (int)in.nval;
                    int r = x - 1;
                    if (tree[x - 1+ leaveNum].d == 0) {
                        update(x - 1 + leaveNum , 1);
                    } else {
                        r = find(x - 1, n-1 , 0);
                        if (r == Integer.MAX_VALUE) {
                            r = find(-1 , x - 2, 0);
                        }
                        update(r + leaveNum, 1);
                    }
                    out.println(r + 1);
                } else {
                    in.nextToken();
                    int x = (int)in.nval;
                    update(x - 1 + leaveNum, 0);
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
