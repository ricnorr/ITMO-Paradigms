import java.io.*;
import java.nio.file.CopyOption;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.min;
import static java.lang.Math.*;

public class ReverseRmq {
    static int n;
    static int mxx = 200000;
    static int mxy = 200000;

    public static void maxs(Node a, Node b, Node c) {
        if (a.data >= b.data) {
            c.data = a.data;
            c.x = a.x;
            c.y = a.y;
        } else {
            c.data = b.data;
            c.x = b.x;
            c.y = b.y;
        }
    }

    static class Node {
        int data;
        int l;
        int r;
        int upd = 0;
        int y;
        int x;
        public Node(int d, int l, int r) {
            this.data = d;
            this.l = l;
            this.r = r;
            y = r;
        }
    }

    static int leaveNum;
    static Node[] tree;
    static int mx;
    static info[] arr;

    public static void push(int i, int x) {
        if (i < leaveNum) {
            tree[i*2+1].upd += tree[i].upd;
            tree[i*2+2].upd += tree[i].upd;
        }
        tree[i].data += tree[i].upd;
        if (tree[i].upd > 0) {
            tree[i].x = x;
        }
        tree[i].upd = 0;
    }

    public static class info implements Comparable {
        int l;
        int y1;
        int y2;
        int isOpen;
        public info(int l, int y1, int y2, int isOpen) {
            this.l = l;
            this.y1 = y1;
            this.y2 = y2;
            this.isOpen = isOpen;
        }

        @Override
        public int compareTo(Object o) {
            info k = (info) o;
            if (l < k.l) {
                return -1;
            }
            else if (l > k.l) {
                return 1;
            }
            if (isOpen == -1) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public static void update(int i, int c, int l, int r, int x) {
        push(i, x);
        if (tree[i].r < l || tree[i].l > r){
            return;
        }
        if (l <= tree[i].l && tree[i].r <=r){
            tree[i].upd += c;
            push(i, x);
            return;
        }
        update(i*2 + 1, c, l, r, x);
        update(i*2 + 2, c, l, r, x);
        maxs(tree[i * 2 + 1], tree[i * 2 + 2], tree[i]);
    }

    public static void build(int i, int l, int r) {
        if (i >= leaveNum) {
            tree[i] = new Node(0, l, r);
            tree[i].y = r;
            tree[i].x = 0;
        } else {
            build(i*2+1, l, (r + l) / 2);
            build(i*2 + 2, ((r+l) / 2) + 1, r);
            tree[i] = new Node(0, l, r);
            tree[i].y = r;
            tree[i].x = 0;
        }
    }




    public static void main(String[] args) {
        try {
            n = 2 * 200000;
            int k = (int) ceil(log(n) / log(2)) + 1;
            leaveNum = (int) (pow(2, k - 1)) - 1;
            tree = new Node[(int) pow(2, k) - 1];
            build(0, 0, (int) pow(2, k - 1) - 1);
            StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
            in.nextToken();
            int m = (int)in.nval;
            arr = new info[2 * m];
            for (int i = 0; i < 2 * m; i+=2) {
                in.nextToken();
                int x1 = (int)in.nval;
                in.nextToken();
                int y1 = (int)in.nval;
                in.nextToken();
                int x2 = (int)in.nval;
                in.nextToken();
                int y2 = (int)in.nval;
                arr[i] = new info(x1, y1, y2, 1);
                arr[i + 1] = new info(x2 + 1, y1, y2, -1);
            }
            Arrays.sort(arr);
            for (int i = 0; i < 2 * m; i++) {
                update(0, arr[i].isOpen, arr[i].y1 + 200000 , arr[i].y2 + 200000 , arr[i].l + 200000 );
                if (tree[0].data > mx) {
                    mxx = tree[0].x;
                    mxy = tree[0].y;
                    mx = tree[0].data;
                }
            }
            System.out.println(mx);
            System.out.println((mxx - 200000) + " " + (mxy - 200000));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}