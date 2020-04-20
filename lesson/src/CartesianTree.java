/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CartesianTree {

    private static class Node {
        Node l;
        Node r;
        int x;
        int z;
        int y;

        public Node(Node l, Node r, int x, int y, int z) {
            this.l = l;
            this.r = r;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class Pair {
        Node a;
        Node b;

        public Pair(Node a, Node b) {
            this.a = a;
            this.b = b;
        }
    }

    static public Node merge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null)
        {
            return a;
        }
        if (a.y >= b.y) {
            a.r = merge(a.r, b);
            if (a != null) {
                array[a.x] = a;
            }
            return a;
        }
        b.l = merge(a, b.l);
        if (b != null) {
            array[b.x] = b;
        }
        return b;
    }

    static public Pair split(int k, Node a) {
        if (a == null) {
            return new Pair(null, null);
        }

        if (k > a.x) {
            Pair res = split(k, a.r);
            if (res.a != null) {
                array[res.a.x] = res.a;
            }
            if (res.b != null){
                array[res.b.x] = res.b;
            }
            a.r = res.a;
            if (a != null) {
                array[a.x] = a;
            }
            return new Pair(a, res.b);
        }
        Pair res = split(k, a.l);
        if (res.a != null) {
            array[res.a.x] = res.a;
        }
        if (res.b != null) {
            array[res.b.x] = res.b;
        }
        a.l = res.b;
        if (a != null) {
            array[a.x] = a;
        }
        return new Pair(res.a, a);
    }



    static int mx = 0;


    static Node[] array = new Node[10];

    static public Node inserts(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (a.y < b.y ) {
            Pair p = split(b.x, a);
            if (p.a != null) {
                array[p.a.x] = p.a;
            }
            if (p.b != null) {
                array[p.b.x] = p.b;
            }

            if (b != null) {
                array[b.x] = new Node(p.a, p.b, b.x, b.y, b.z);
            }

            return array[b.x];
        }

        if (b.x < a.x) {
            array[a.x] = new Node(inserts(a.l, b), a.r, a.x, a.y, a.z);
            return array[a.x];
        } else {
            array[a.x] = new Node(a.l, inserts(a.r, b), a.x, a.y, a.z);
            return array[a.x];
        }
    }



    static public Node insert(Node a, int k, int y) {
        Random random = new Random();
        mx = Integer.max(k, mx);
        Node f = array[k];
        if (f == null) {
            f = new Node(null, null, k, random.nextInt(), y);
            Node ans =  inserts(a, f);
            if (ans != null) {
                array[ans.x] = ans;
            }
            return ans;
        } else {
            int last = f.z;
            f.z = y;
            array[f.x] = f;
            a = insert(a, f.x + 1, last);
            if (a != null) {
                array[a.x] = a;
            }
            return a;
        }
    }

    static int next = 1;


    public static void main(String[] args)  {
        try {
            StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
            in.nextToken();
            int m = (int)in.nval;
            in.nextToken();
            int k = (int)in.nval;
            Node x = null;

            for (int i = 1; i < m + 1; i++) {
                in.nextToken();
                x = insert(x, (int)in.nval, i);
            }

            System.out.println(mx);
            for (int i = 1; i <= mx; i++) {
                if (array[i] != null) {
                    System.out.print(array[i].x + " ");
                } else {
                    System.out.print("0 ");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
*/
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CartesianTree {

    private static class Node {
        Node l;
        Node r;
        int x;
        int z;
        int y;

        public Node(Node l, Node r, int x, int y, int z) {
            this.l = l;
            this.r = r;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class Pair {
        Node a;
        Node b;

        public Pair(Node a, Node b) {
            this.a = a;
            this.b = b;
        }
    }

    static public Node merge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null)
        {
            return a;
        }
        if (a.y >= b.y) {
            a.r = merge(a.r, b);
            if (a != null) {
                array[a.x] = a;
            }
            return a;
        }
        b.l = merge(a, b.l);
        if (b != null) {
            array[b.x] = b;
        }
        return b;
    }

    static public Pair split(int k, Node a) {
        if (a == null) {
            return new Pair(null, null);
        }

        if (k > a.x) {
            Pair res = split(k, a.r);
            if (res.a != null) {
                array[res.a.x] = res.a;
            }
            if (res.b != null){
                array[res.b.x] = res.b;
            }
            a.r = res.a;
            if (a != null) {
                array[a.x] = a;
            }
            return new Pair(a, res.b);
        }
        Pair res = split(k, a.l);
        if (res.a != null) {
            array[res.a.x] = res.a;
        }
        if (res.b != null) {
            array[res.b.x] = res.b;
        }
        a.l = res.b;
        if (a != null) {
            array[a.x] = a;
        }
        return new Pair(res.a, a);
    }



    static int mx = 0;


    static Node[] array = new Node[131073];

    static public Node inserts(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (a.y < b.y ) {
            Pair p = split(b.x, a);
            if (p.a != null) {
                array[p.a.x] = p.a;
            }
            if (p.b != null) {
                array[p.b.x] = p.b;
            }

            if (b != null) {
                array[b.x] = new Node(p.a, p.b, b.x, b.y, b.z);
            }

            return array[b.x];
        }

        if (b.x < a.x) {
            array[a.x] = new Node(inserts(a.l, b), a.r, a.x, a.y, a.z);
            return array[a.x];
        } else {
            array[a.x] = new Node(a.l, inserts(a.r, b), a.x, a.y, a.z);
            return array[a.x];
        }
    }



    static public Node insert(Node a, int k, int y) {
        Random random = new Random();
        mx = Integer.max(k, mx);
        Node f = array[k];
        if (f == null) {
            f = new Node(null, null, k, random.nextInt(), y);
            array[k] = f;
            Node ans =  inserts(a, f);
            if (ans != null) {
                array[ans.x] = ans;
            }
            return ans;
        } else {
            int last = f.z;
            f.z = y;
            array[f.x] = f;
            Pair tmp = split(f.x, a);
            a = insert(a, f.x + 1, last);
            if (a != null) {
                array[a.x] = a;
            }
            return a;
        }
    }

    static int next = 1;

    static public void printTree(Node a) {
        if (a == null) {
            return;
        }
        if (next > mx) {
            return;
        }
        printTree(a.l);
        for (int i = next; i < a.x; i++) {
            System.out.print("0 ");
        }
        next = a.x + 1;
        System.out.print(a.z + " ");
        printTree(a.r);
    }


    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int k = sc.nextInt();
        Node x = null;

        for (int i = 1; i < m + 1; i++) {
            x = insert(x, sc.nextInt(), i);
        }
        System.out.println(mx);
        for (int i = 1; i <= mx; i++) {
            if (array[i] == null) {
                System.out.print("0 ");
            } else {
                System.out.print(array[i].z + " ");

            }
        }
    }
}