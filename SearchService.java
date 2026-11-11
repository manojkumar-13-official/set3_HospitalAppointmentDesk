
public class SearchService {

    public static int linearSearch(Patient[] a, int id, Stats s) {
        for (int i = 0; i < a.length; i++) {
            s.comparisons++;
            if (a[i].id == id) return i;
        }
        return -1;
    }

    public static int binarySearch(Patient[] a, int id, Stats s) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            s.comparisons++;
            if (a[mid].id == id) return mid;
            if (a[mid].id < id) lo = mid + 1; else hi = mid - 1;
        }
        return -1;
    }
}
