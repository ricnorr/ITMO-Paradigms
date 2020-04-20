import java.util.Scanner;

public class BasicTree {
    public static class Node {
        Node l;
        Node r;
        Node pr;
        int d;

        public Node(Node l, Node r,  int d) {
            this.l = l;
            this.r = r;
            this.d = d;
        }
    }

        public static Node insert(int k, Node x) {
            if (x == null) {
                return new Node(null, null, k);
            }
            if (k == x.d) {
                return x;
            }
            if (k < x.d) {
                Node ans = insert(k, x.l);
                x.l = ans;
                return x;
            }
            Node ans = insert(k, x.r);
            x.r = ans;
            return x;
        }

        public static Node find(int k, Node x) {
            if (x == null) {
                return null;
            }
            if (k == x.d) {
                return x;
            }
            if (k < x.d) {
                return find(k, x.l);
            }
            return find(k, x.r);
        }

        public static Node max(Node x) {
            while (x != null && x.r != null) {
                x = x.r;
            }
            return x;
        }


        public static Node next(int k, Node x) {
            if (x == null) {
                return null;
            }
            if (k >= x.d) {
                return next(k, x.r);
            }
            else {
                Node tmp = next(k, x.l);
                if (tmp == null) {
                    return x;
                }
                return tmp;
            }
        }

        public static Node prev(int k, Node x) {
            if (x == null) {
                return null;
            }
            if (x.d < k) {
                Node tmp = prev(k, x.r);
                if (tmp == null) {
                    return x;
                }
                return tmp;
            } else {
                return prev(k, x.l);
            }
        }

        public static Node delete(int k, Node x) {
            if (x == null) {
                return null;
            }
            if (k < x.d) {
                x.l = delete(k, x.l);
                return x;
            }
            if (k > x.d) {
                x.r = delete(k, x.r);
                return x;
            }
            else {
                if (x.l == null && x.r == null) {
                    return null;
                }
                if (x.l == null) {
                    return x.r;
                }
                if (x.r == null) {
                    return x.l;
                }
                Node tmp = x.l;
                Node prev = x.l;
                while (tmp.r != null) {
                    prev = tmp;
                    tmp = tmp.r;
                }
                prev.r = null;
                return new Node(x.l, x.r, tmp.d);
            }
        }

  public static void main(String[] args) {
          Node x = null;
          Scanner sc = new Scanner(System.in);
          Node tmp;
          while (sc.hasNext()) {
              String v = sc.next();
              switch (v) {
                  case ("insert"):
                      x = insert(sc.nextInt(), x);
                      break;
                  case ("exists"):
                      System.out.println(find(sc.nextInt(), x) != null);
                      break;
                  case ("delete"):
                      x = delete(sc.nextInt(), x);
                      break;
                  case ("next"): ;
                      tmp = next(sc.nextInt(), x);
                      if (tmp == null) {
                          System.out.println("none");
                      } else {
                          System.out.println(tmp.d);
                      }
                      break;
                  case ("prev"):
                      tmp = prev(sc.nextInt(), x);
                      if (tmp == null) {
                          System.out.println("none");
                      } else {
                          System.out.println(tmp.d);
                      }
                      break;
              }
          }
  }
}
