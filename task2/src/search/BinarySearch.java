package search;
public class BinarySearch {
    // Pre: args.length > 1 && (int)args[i - 1] >= (int)args[i] ^ 1 <= i < args.length - 1
    // Post: min(i), args[0].parseInt() >= a[i]
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        if (a.length == 0) {
            System.out.println(0);
        } else {
            // a[i - 1] >= a[i] ^ 0 <= i < a.length ^ a[-1] == +inf ^ a[a.len] == -inf
            System.out.println(loopSearch(a, x));
        }
    }

    // Pre: a[i - 1] >= a[i] ^ 0 <= i < a.length ^ a[-1] == +inf ^ a[a.len] == -inf
    // Post: R = r ^ 0 <= r < a.length ^ a[r-1] > x >= a[r]
    private static int loopSearch(int[] a, int x) {
        int l = -1;
        int r = a.length;
        // l == -1 ^ r = a.length - 1

        // Inv: -1 <= i <= l < r <= j <= a.length ^ a[i] > x >= a[j]
        while (r - l > 1) {
            // Inv ^ r - l > 1
            int m = l + (r - l) / 2;
            // Inv ^ r - l > 1 ^ l < m < r
            if (x < a[m]) {
                // Inv ^ r - l > 1 ^ l < m < r ^ x < a[m]
                l = m;
                // l' < r ^ a[l'] > x >= a[r] =>
                // Inv
            } else {
                // Inv ^ r - l > 1 ^ l < m < r ^ x >= a[m]
                r = m;
                // l < r' ^ a[l] > x >= a[r'] =>
                // Inv
            }
            // Inv
        }
        // Inv ^ r - l == 1 ^ a[l] > x >= a[r]

        // Inv ^ r - l  == 1 => l = r - 1 => R = r ^ a[r-1] > x >= a[r]
        return r;
    }

   // Pre: -1 <= i <= a.length ^ a[i - 1] >= a[i] ^ a[-1] == +inf ^ a[a.len] == -inf;
   // Post: R = r ^ a[r-1] > x >= a[r]
    private static int recursiveSpan(int[] a, int x) {
        return recursiveSearch(a, x, -1, a.length);
    }


    // Pre: -1 <= l < i <= r <= a.length ^ a[i - 1] >= a[i] ^ a[-1] == +inf ^ a[a.inf] == -inf
    // Post: R = r ^ a[r-1] > x >= a[r]
    // Inv: a[i] > x >= a[j] ^ -1 <= i <= l < r <= j <= a.length ^ a unchangeable
    private static int recursiveSearch(int[] a, int x, int l , int r) {
        if (r - l <= 1) {
            // Post: Inv ^ r - l == 1 => l = r - 1 => R = r ^ a[r-1] > x >= a[r]
            return r;
        }
        // Inv ^ r - l > 1
        int m = l + (r - l) / 2;
        // Inv ^ r - l > 1 ^ l < m < r
        if (x < a[m]) {
            // Inv ^ r - l > 1 ^ l < m < r ^ x < a[m] =>
            // Inv ^ m < r ^ a[m] > x >= a[r]
            return recursiveSearch(a, x, m, r);
        } else {
            // Inv ^ r - l > 1 ^ x >= a[m] =>
            // Inv ^ m < r ^ a[l] > x >= a[m]
            return recursiveSearch(a, x, l, m);
        }
    }
}
