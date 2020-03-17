import java.io.*;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.*;

public class fenwick2 {
    static class Node {
        int mx;
        int x;
        int y;
        int l;
        int r;
        int upd;
        Node leftChild;
        Node rightChild;

        public Node(Node leftChild, Node rightChild, int x, int y, int l, int r) {
            this.mx = 0;
            this.x = x;
            this.y = y;
            this.upd = upd;
            this.l = l;
            this.r = r;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    static Node[] arr;
    static int maxX = Integer.MIN_VALUE;
    static int maxY = Integer.MIN_VALUE;
    static int minX = Integer.MAX_VALUE;
    static int minY = Integer.MAX_VALUE;
    static int kx;
    static Node ans = new Node(null,null,Integer.MIN_VALUE,0,0,0);

    public static Node build(int l, int r,int k) {
        if (l == r - 1) {
            return new Node(null, null, r, k, l, r);
        } else {
            return new Node(build(l, (l + r) / 2, k), build((l + r) / 2, r, k), r, k, l, r);
        }
    }

    public static void update(int x, int y, int a, int b) {
        for (int i = y; i < b + 1; i++) {
            if (arr[i] == null) {
                arr[i] = build(-1, kx, i);
            }
            update(arr[i], x - 1, a);
            push(arr[i]);
            if (arr[i].mx + arr[i].upd > ans.mx) {
                ans = arr[i];
            }
        }
    }

    public static void update(Node a, int l, int r) {
        if (a == null) {
            return;
        }
        push(a);
        if (a.r <= l || a.l >= r) {
            if (a.mx + a.upd > ans.mx) {
                ans = a;
            }
            return;
        }
        if (l <= a.l && a.r  <= r) {
            a.upd += 1;
            if (a.mx + a.upd > ans.mx) {
                ans = a;
            }
            return;
        }
        update(a.leftChild, l, r);
        update(a.rightChild, l, r);
        if (a.leftChild.mx + a.leftChild.upd > a.rightChild.mx + a.rightChild.upd) {
            a.mx = a.leftChild.mx + a.leftChild.upd;
            a.x = a.leftChild.x;
            a.y = a.leftChild.y;
        } else {
            a.mx = a.rightChild.mx + a.rightChild.upd;
            a.x = a.rightChild.x;
            a.y = a.rightChild.y;
        }
        if (a.mx + a.upd > ans.mx) {
            ans = a;
        }
    }

    public static void push(Node a) {
        a.mx += a.upd;
        if (a.leftChild != null) {
            a.leftChild.upd += a.upd;
        }
        if (a.rightChild != null) {
            a.rightChild.upd += a.upd;
        }
        a.upd = 0;
    }

    public static class Triple {
        int a;
        int b;
        int c;
        public Triple(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    public static Triple find() {
        int mx = Integer.MIN_VALUE;
        int j = 0;
        for (int i = 0; i < maxY + 1; i++) {
            if (arr[i] == null) {
                continue;
            }
            push(arr[i]);
            if (arr[i].mx > mx) {
                j = i;
                mx = arr[i].mx;
            }
        }
        return new Triple(arr[j].mx, arr[j].x, arr[j].y);
    }

    public static void main(String[] args) {
        try {
            StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
            in.nextToken();
            int n = (int)in.nval;
            int co = (int) pow(10,5);
            int[] zapr = new int[4 * n];
            for (int i = 0; i < n; i++) {
                in.nextToken();
                zapr[i * 4] = (int)in.nval;
                in.nextToken();
                zapr[i*4 + 1] = (int)in.nval;
                in.nextToken();
                zapr[i*4 + 2] = (int)in.nval;
                in.nextToken();
                zapr[i*4 + 3] = (int)in.nval;
                maxY = max(maxY, zapr[i*4 + 3]);
                minY = min(minY , zapr[i * 4 + 1]);
                maxX = max(maxX, zapr[i*4 + 2]);
                minX = min(minX, zapr[i*4]);
                //update(x1,y1,x2,y2);
            }
            if (minY < 0) {
                minY *= -1;
            } else {
                minY = 0;
            }
            int dx;
            int dy;
            if (minX < 0) {
                minX *= -1;
            } else {
                minX = 0;
            }
            maxX = abs(maxX) + minX ;
            maxY = abs(maxY) + minY ;

            kx = (int) ceil(log(maxX + 1) / log(2)) + 1;
            kx = (int) pow(2, kx-1);
            arr = new Node[maxY + 1];
            for (int i = 0; i < n; i++) {
                update(zapr[i * 4] + minX, zapr[i * 4 + 1] + minY, zapr[i* 4 + 2] + minX, zapr[i * 4 + 3] + minY);
            }
            //Triple k = find();
            //System.out.println(k.a + "\n" + (k.b - 200000) + " " + (k.c - 200000));
            System.out.println(ans.mx + "\n" + (ans.x - minX) +" " +  (ans.y - minY));
        } catch (IOException e) {
            //throw new RuntimeException();
        }
    }
}
