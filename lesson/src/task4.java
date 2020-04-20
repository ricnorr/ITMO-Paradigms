import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class task4 {

    private static class Node {
        Node l;
        Node r;
        int data;
        int y;
        long sum;

        public Node(Node l, Node r, int data,  int y) {
            this.l = l;
            this.y = y;
            this.r = r;
            this.data = data;
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
            recount(b);
            return b;
        }
        if (b == null)
        {
            recount(a);
            return a;
        }
        if (a.y >= b.y) {
            a.r = merge(a.r, b);
            recount(a);
            return a;
        }
        b.l = merge(a, b.l);
        recount(b);
        return b;
    }

    static public Pair split(int k, Node a) {
        if (a == null) {
            return new Pair(null, null);
        }

        if (k > a.data) {
            Pair res = split(k, a.r);
            a.r = res.a;
            recount(a);
            return new Pair(a, res.b);
        }
        Pair res = split(k, a.l);
        a.l = res.b;
        recount(a);
        return new Pair(res.a, a);
    }

    static long reso = 0;

    static Node sum (Node a,int l, int r) {
        Pair res = split(l, a);
        Pair res1 = split(r + 1, res.b);
        long ans = 0;
        if (res1.a != null) {
            ans = res1.a.sum;
        }
        a = merge(res.a, res1.a);
        a = merge(a, res1.b);
        System.out.println(ans);
        reso = ans;
        return a;
    }

    static public void recount(Node a) {
        if (a == null) {
            return;
        }
        a.sum = a.data;
        if (a.l != null) {
            a.sum += a.l.sum;
        }
        if (a.r != null) {
            a.sum  += a.r.sum;
        }
    }


    public static Node min(Node a) {
        if (a== null) {
            return null;
        }
        while (a.l != null) {
            a = a.l;
        }
        return a;
    }

    static public Node inserts(Node a, Node b) {
        recount(a);
        recount(b);
        if (a == null) {
            return b;
        }
        if (a.data == b.data) {
            return a;
        }
        if (a.y < b.y) {
            Pair p = split(b.data, a);
            Node cur = min(p.b);
            if (cur != null && cur.data == b.data) {
                return merge(p.a, p.b);
            }
            Node res =  new Node(p.a, p.b, b.data, b.y);
            recount(res);
            return res;
        }
        if (b.data < a.data) {
            Node res =  new Node(inserts(a.l, b), a.r, a.data, a.y);
            recount(res);
            return res;
        } else {
            Node res =  new Node(a.l, inserts(a.r, b), a.data, a.y);
            recount(res);
            return res;
        }
    }

    static public Node insert(Node a, Node b) {
        recount(b);
        Pair tmp = split(b.data, a);
        Node cur = min(tmp.b);
        if (cur != null && cur.data == b.data) {
            return merge(tmp.a, tmp.b);
        } else {
            return merge(merge(tmp.a, b), tmp.b);
        }
    }

    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int n = sc.nextInt();
        Node x = null;
        String prev = null;
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            switch (s) {
                case ("+"):
                    if (prev != null && prev.equals("?") ) {
                        x = inserts(x, new Node(null, null, (int) ((sc.nextInt() + reso) % 1000000000) , random.nextInt()));
                        break;
                    }
                    x = inserts(x, new Node(null, null, sc.nextInt(), random.nextInt()));
                    break;
                case ("?"):
                    x =  sum(x, sc.nextInt(), sc.nextInt());
                    break;
            }
            prev = s;
        }
    }
}

