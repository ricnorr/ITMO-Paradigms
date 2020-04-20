/* import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class task5 {

    private static class Node {
        Node l;
        Node r;
        Node prev;
        int y;
        int cnt;
        int data;

        public Node(Node l, Node r, int data, int cnt, int y) {
            this.l = l;
            this.r = r;
            this.y = y;
            this.cnt = cnt;
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
            recount(a.r);
            recount(a);
            return a;
        }
        b.l = merge(a, b.l);
        recount(b.l);
        recount(b);
        return b;
    }

    static public Pair split(int k, Node a) {
        if (a == null) {
            return new Pair(null, null);
        }
        int left = 0;
        if (a.l != null) {
            recount(a.l);
            left = a.l.cnt;
        }
        if (left >= k) {
            Pair tmp = split(k, a.l);
            a.l = tmp.b;
            //recount(a.l);
            recount(a);
            return new Pair(tmp.a, a);
        }
        Pair tmp = split(k - left - 1, a.r);
        a.r = tmp.a;
        //recount(a.r);
        recount(a);
        return new Pair(a, tmp.b);
    }

    static public Node insert(Node a, int k, Node b) {
        Pair tmp = split(k, a);
        return merge(merge(tmp.a, b), tmp.b);
    }

    static public Node delete(Node a, int k) {
        Pair tmp = split(k, a);
        Pair tmp2 = split(1, tmp.b);
        return merge(tmp.a, tmp2.b);
    }

    static public void recount(Node a) {
        if (a == null) {
            return;
        }
        a.cnt = 1;
        if (a.l != null) {
            a.cnt += a.l.cnt;
        }
        if (a.r != null) {
            a.cnt += a.r.cnt;
        }
    }

    public static Node insert(Node a, Node b) {
        return merge(a, b);
    }

    public static Node buildTree(int n, Scanner sc) { // багает
        Node root = null;
        Node mx = null;
        Random random = new Random();


        for (int i = 1; i <= n; i++) {
            int y = random.nextInt();
            Node cur = mx;
            Node prev = cur;
            while (cur != null && cur.y < y) {
                prev = cur;
                cur = cur.prev;
            }
            if (cur == null) {
                root = new Node(prev, null, sc.nextInt(), 1, y);
                recount(prev);
                recount(root);
                mx = root;
            } else {
                cur.r = new Node(cur.r, null, sc.nextInt(), 1, y);
                cur.r.prev = cur;
                recount(cur.r);
                recount(cur);
                mx = cur.r;
            }
        }
        return root;
    }

    public static void printTree(Node a) {
        if (a == null) {
            return;
        }
        printTree(a.l);
        System.out.print(a.data + " ");
        printTree(a.r);
    }

    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int n = sc.nextInt();
        int m = sc.nextInt();
        int cnt = n;
        //Node x = buildTree(n, sc);
        Node x = null;
        for (int i = 0; i < n; i++) {
            x = insert(x, new Node(null, null, sc.nextInt(), 1, random.nextInt()));
        }

        //printTree(x);
        //sc =  new Scanner(System.in);
        for (int i = 0; i < m; i++) {
            String s = sc.next();
            switch (s) {
                case ("del"):
                    cnt--;
                    x = delete(x, sc.nextInt() - 1);
                    break;
                case("add"):
                    cnt++;
                    x = insert(x, sc.nextInt(), new Node(null,null, sc.nextInt(), 1, random.nextInt() ) );
                    break;
            }
            //System.out.println("");
            //printTree(x);
        }
        System.out.println(cnt);
        printTree(x);
    }
} */ //Task F
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class task5 {

    private static class Node {
        Node l;
        Node r;
        Node prev;
        int y;
        int upd = 1;
        int cnt;
        int data;

        public Node(Node l, Node r, int data, int cnt, int y) {
            this.l = l;
            this.r = r;
            this.y = y;
            this.cnt = cnt;
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

    static public void push(Node a) {
           if (a == null) {
               return;
           }
           if (a.upd == -1) {
               if (a.l != null) {
                   a.l.upd *= -1;
               }
               if (a.r != null) {
                   a.r.upd *= -1;
               }
               Node tmp = a.r;
               a.r = a.l;
               a.l = tmp;
               a.upd *= -1;
           }
    }

    static public Node merge(Node a, Node b) {
        push(a);
        push(b);
        if (a == null) {
            recount(b);
            return b;
        }
        if (b == null) {
            recount(a);
            return a;
        }
        if (a.y >= b.y) {
            a.r = merge(a.r, b);
            recount(a.r);
            recount(a);
            return a;
        }
        b.l = merge(a, b.l);
        recount(b.l);
        recount(b);
        return b;
    }

    static public Pair split(int k, Node a) {
        push(a);
        if (a == null) {
            return new Pair(null, null);
        }
        int left = 0;
        if (a.l != null) {
            left = a.l.cnt;
        }
        if (left >= k) {
            Pair tmp = split(k, a.l);
            a.l = tmp.b;
            recount(a);
            return new Pair(tmp.a, a);
        }
        Pair tmp = split(k - left - 1, a.r);
        a.r = tmp.a;
        recount(a);
        return new Pair(a, tmp.b);
    }


    static public void recount(Node a) {
        if (a == null) {
            return;
        }
        push(a);
        a.cnt = 1;
        if (a.l != null) {
            a.cnt += a.l.cnt;
        }
        if (a.r != null) {
            a.cnt += a.r.cnt;
        }
    }



    public static void printTree(Node a) {
        push(a);
        if (a == null) {
            return;
        }
        printTree(a.l);
        System.out.print(a.data + " ");
        printTree(a.r);
    }

    public static Node putInFirst(Node a, int l, int r) {
        Pair res = split(l, a);
        Pair res2 = split(r - l + 1, res.b);
        res2.a.upd *= -1;
        return merge(merge(res.a, res2.a), res2.b);
    }

    public static Node buildTree(int n) {
        Node root = null;
        Node mx = null;
        Random random = new Random();


        for (int i = 1; i <= n; i++) {
            int y = random.nextInt();
            Node cur = mx;
            Node prev = cur;
            while (cur != null && cur.y > y) {
                prev = cur;
                cur = cur.prev;
            }
            if (cur == null) {
                root = new Node(prev, null, i, 1, y);
                recount(prev);
                recount(root);
                mx = root;
            } else {
                Node thisa = new Node(cur, null, i, 1, y);
                thisa.prev = cur.prev;
                cur.prev = thisa;
                if (thisa.prev != null) {
                    thisa.prev.r = thisa;
                }
                recount(thisa.l);
                recount(thisa);
                recount(thisa.prev);
                mx = thisa;
            }
        }
        Node tmp = mx;
        while (tmp != null && tmp.prev != null) {
            tmp = tmp.prev;
        }
        return tmp;
    }


    public static Node insert(Node x, Node b) {
        if (x == null) {
            recount(b);
            return b;
        }
        if (b.y > x.y) {
            b.l = x;
            recount(b);
            return b;
        } else {
            x.r = insert(x.r, b);
            recount(x);
            return x;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        int n = sc.nextInt();
        int m = sc.nextInt();
        int cnt = n;
        Node x =  null;
        x = buildTree(n);
        //printTree(x);
        for (int i = 0; i < m; i++) {
            x = putInFirst(x, sc.nextInt() - 1, sc.nextInt() - 1);
            //System.out.println("");
            //printTree(x);
        }


        //System.out.println("");
        printTree(x);

    }
}