package search;

public class BinarySearchMissing {
    // Pre: args.length > 0 ^ (int)args[i - 1] >= (int)args[i] ^ 1 <= i < args.len - 1
    // Post: void
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            a[i - 1] = Integer.parseInt(args[i]);
        }
        // a[-1] == +inf ^ a[a.len] == -inf ^ 0 <= i <= a.len ^ a[i - 1] >= a[i]
        System.out.println(loopSearch(a, x));
    }

    // Pre:  0 <= i < a.len ^ a[i - 1] >= a[i] ^ a[-1] == +inf ^ a[a.len] == -inf
    // Post: (0 <= R <= a.len ^ a[R-1] > x >= a[R]) ^ (R = r ^ r < a.len ^ x == a[r] || R = -r - 1)
    private static int loopSearch(int[] a, int x) {
        int l = -1;
        int r = a.length;
        // l == -1 ^ r = a.len

        // Inv: -1 <= i <= l < r <= j <= a.len ^ a[i] > x >= a[j]
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

        // Inv ^ r - l  == 1 => l = r - 1 => a[r-1] > x >= a[r]
        // => (R = r ^ 0 <= r < a.len ^ a[r-1] > x == a[r]) || (R = -r - 1 ^ 0 <= r <= a.len ^ a[r-1] > x > a[r])
        return (r < a.length && x == a[r]) ? r : -r - 1;
    }

    // Pre: 0 <= i < a.len ^ a[i - 1] >= a[i] ^ a[-1] == +inf ^ a[a.len] == -inf
    // Post: (0 <= r <= a.len ^ a[r-1] > x >= a[r]) ^ (R = r ^ r < a.len ^ x == a[r] || R = -r - 1)
    private static int recursiveSpan(int[] a, int x) {
        return recursiveSearch(a, x, -1, a.length);
    }


    // Pre: 0 <= i < a.len ^ a[i - 1] >= a[i] ^ -1 <= l < r <= a.len ^ a[-1] == +inf ^ a[a.len] == -inf
    // Post: (0 <= r <= a.len ^ a[r-1] > x >= a[r]) ^ (R = r ^ r < a.len ^ x == a[r] || R = -r - 1)
    // Inv: a[i] > x >= a[j] ^ -1 <= i <= l < r <= j <= a.len ^ a unchangeable
    private static int recursiveSearch(int[] a, int x, int l , int r) {
        if (r - l <= 1) {
            // Inv ^ r - l == 1 => l = r - 1 => (-1 <= r - 1 < r <= a.len) ^ (a[r-1] > x >= a[r]) =>
            // (R = r ^ r < a.len ^ x == a[r]) || (R = -r - 1)
            return (r < a.length && x == a[r]) ? r : -r - 1;
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
