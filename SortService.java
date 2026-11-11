import java.util.Arrays;

public class SortService {

    public static int cmp(Appointment a, Appointment b) {
        if (a.urgency != b.urgency) return Integer.compare(a.urgency, b.urgency);
        if (a.arrival != b.arrival) return Integer.compare(a.arrival, b.arrival);
        return Integer.compare(a.id, b.id);   // tie-break => deterministic result
    }

    private static void swap(Appointment[] a, int i, int j) {
        Appointment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void selectionSort(Appointment[] a, Stats s, boolean trace) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                s.comparisons++;
                if (cmp(a[j], a[min]) < 0) min = j;
            }
            if (min != i) { swap(a, i, min); s.swaps++; }
            if (trace) {
                System.out.println("  Pass " + (i + 1) + ": min at index " + min
                        + (min != i ? " -> swap(" + i + "," + min + ")" : " -> no swap")
                        + "  " + Arrays.toString(a));
            }
        }
    }

    public static void quickSort(Appointment[] a, int lo, int hi, Stats s) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;          // middle pivot avoids worst case on sorted input
        if (mid != hi) { swap(a, mid, hi); s.swaps++; }
        Appointment pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            s.comparisons++;
            if (cmp(a[j], pivot) < 0) {
                if (i != j) { swap(a, i, j); s.swaps++; }
                i++;
            }
        }
        if (i != hi) { swap(a, i, hi); s.swaps++; }
        quickSort(a, lo, i - 1, s);
        quickSort(a, i + 1, hi, s);
    }

    public static boolean isSorted(Appointment[] a) {
        for (int i = 1; i < a.length; i++) if (cmp(a[i - 1], a[i]) > 0) return false;
        return true;
    }
}
