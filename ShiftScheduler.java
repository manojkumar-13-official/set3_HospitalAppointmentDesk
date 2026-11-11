import java.util.Arrays;
import java.util.Comparator;

public class ShiftScheduler {

    private static Slot[] sortedByEnd(Slot[] s) {
        Slot[] c = s.clone();
        Arrays.sort(c, Comparator.comparingInt((Slot x) -> x.end).thenComparingInt(x -> x.start));
        return c;
    }

    public static int dpMax(Slot[] slots, boolean weighted) {
        Slot[] s = sortedByEnd(slots);
        int n = s.length;
        int[] dp = new int[n + 1];                 // dp[i] = best using the first i slots (sorted by end)
        for (int i = 1; i <= n; i++) {
            // p = how many of the first i-1 slots finish before slot i starts (binary search)
            int lo = 0, hi = i - 1;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (s[mid].end <= s[i - 1].start) lo = mid + 1; else hi = mid;
            }
            int gain = weighted ? s[i - 1].duration() : 1;
            dp[i] = Math.max(dp[i - 1], gain + dp[lo]);   // skip slot i  OR  take slot i
        }
        return dp[n];
    }

    public static int greedyMax(Slot[] slots) {
        Slot[] s = sortedByEnd(slots);
        int count = 0, lastEnd = Integer.MIN_VALUE;
        for (Slot x : s) if (x.start >= lastEnd) { count++; lastEnd = x.end; }
        return count;
    }
}
