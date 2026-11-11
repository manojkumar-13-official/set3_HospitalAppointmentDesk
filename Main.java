import java.util.*;

public class Main {

    public static void main(String[] args) {
        task1();
        task2();
        task3();
        task4();
        task5();
    }

    static void task1() {
        System.out.println("\n=== TASK 1: Linear vs Binary search (20,000 patients, 1,000 look-ups) ===");
        int n = 20000;
        Patient[] patients = new Patient[n];
        for (int i = 0; i < n; i++) patients[i] = new Patient(1000 + i * 3, "Patient-" + i);

        Random r = new Random(42);
        int[] keys = new int[1000];
        for (int i = 0; i < keys.length; i++)
            keys[i] = (i % 10 == 0) ? 1000 + r.nextInt(n) * 3 + 1 : 1000 + r.nextInt(n) * 3;

        Stats w = new Stats();   // warm-up for the JIT
        for (int k : keys) { SearchService.linearSearch(patients, k, w); SearchService.binarySearch(patients, k, w); }

        Stats ls = new Stats(), bs = new Stats();
        int foundL = 0, foundB = 0;

        long t0 = System.nanoTime();
        for (int k : keys) if (SearchService.linearSearch(patients, k, ls) >= 0) foundL++;
        long linearNs = System.nanoTime() - t0;

        t0 = System.nanoTime();
        for (int k : keys) if (SearchService.binarySearch(patients, k, bs) >= 0) foundB++;
        long binaryNs = System.nanoTime() - t0;

        System.out.printf("Linear search : found=%d  comparisons=%,d  time=%.3f ms%n", foundL, ls.comparisons, linearNs / 1e6);
        System.out.printf("Binary search : found=%d  comparisons=%,d  time=%.3f ms%n", foundB, bs.comparisons, binaryNs / 1e6);
        System.out.printf("Binary search used about %.0fx fewer comparisons.%n", (double) ls.comparisons / bs.comparisons);
        System.out.println("Explanation: linear is O(n) (~n/2 = 10,000 checks on average, 20,000 for a miss);");
        System.out.println("binary is O(log n) (at most ~15 checks for 20,000 records) but needs sorted data.");
    }

    static void task2() {
        System.out.println("\n=== TASK 2: Sort by urgency, then arrival time ===");
        Appointment[] small = {
            new Appointment(1, 101, 3, 490, 1), new Appointment(2, 102, 1, 510, 2),
            new Appointment(3, 103, 2, 480, 3), new Appointment(4, 104, 1, 495, 4),
            new Appointment(5, 105, 3, 470, 5), new Appointment(6, 106, 2, 500, 6)
        };
        System.out.println("Selection-sort TRACE on 6 appointments (U = urgency, t = arrival minute):");
        System.out.println("  Start : " + Arrays.toString(small));
        Stats ts = new Stats();
        SortService.selectionSort(small.clone(), ts, true);
        System.out.println("  Result: comparisons=" + ts.comparisons + ", swaps=" + ts.swaps);

        int n = 5000;
        Random r = new Random(7);
        Appointment[] base = new Appointment[n];
        for (int i = 0; i < n; i++)
            base[i] = new Appointment(i + 1, 1 + r.nextInt(20000), 1 + r.nextInt(5), 480 + r.nextInt(540), r.nextInt(40));

        Appointment[] a1 = base.clone(), a2 = base.clone();
        Stats s1 = new Stats(), s2 = new Stats();

        long t0 = System.nanoTime();
        SortService.selectionSort(a1, s1, false);
        long selNs = System.nanoTime() - t0;

        t0 = System.nanoTime();
        SortService.quickSort(a2, 0, n - 1, s2);
        long qNs = System.nanoTime() - t0;

        System.out.println("\nPerformance on " + n + " random appointments:");
        System.out.printf("Selection sort: swaps=%,d  comparisons=%,d  time=%.3f ms  sorted=%b%n", s1.swaps, s1.comparisons, selNs / 1e6, SortService.isSorted(a1));
        System.out.printf("Quick sort    : swaps=%,d  comparisons=%,d  time=%.3f ms  sorted=%b%n", s2.swaps, s2.comparisons, qNs / 1e6, SortService.isSorted(a2));
        System.out.println("Both give identical order: " + Arrays.equals(a1, a2));
        System.out.println("Explanation: selection sort does at most n-1 swaps (few writes) but always O(n^2) comparisons;");
        System.out.println("quick sort does more swaps but only ~O(n log n) comparisons, so it is far faster.");
    }

    static void task3() {
        System.out.println("\n=== TASK 3: Double-booking detection ===");
        Appointment[] t = {
            new Appointment(1, 500, 2, 480, 10), new Appointment(2, 501, 2, 481, 10),
            new Appointment(3, 500, 1, 482, 11), new Appointment(4, 500, 3, 483, 10)
        };
        System.out.println("Test data -> double bookings found: " + DoubleBookingDetector.findWithHashSet(t)
                + " (expected [A4(U3,t483)])");

        int n = 20000;
        Random r = new Random(99);
        Appointment[] all = new Appointment[n];
        for (int i = 0; i < n; i++)
            all[i] = new Appointment(i + 1, 1 + r.nextInt(15000), 1 + r.nextInt(5), 480 + r.nextInt(540), r.nextInt(40));

        long t0 = System.nanoTime();
        List<Appointment> dupHash = DoubleBookingDetector.findWithHashSet(all);
        long hashNs = System.nanoTime() - t0;

        t0 = System.nanoTime();
        int dupList = DoubleBookingDetector.findWithListScan(all);
        long listNs = System.nanoTime() - t0;

        System.out.printf("HashSet  : %d duplicates in %.3f ms  (O(n))%n", dupHash.size(), hashNs / 1e6);
        System.out.printf("List scan: %d duplicates in %.3f ms  (O(n^2))%n", dupList, listNs / 1e6);
        System.out.println("Explanation: a list scan compares every appointment with all earlier ones: ~n^2/2 = 200 million");
        System.out.println("checks for 20,000 records. A HashSet does one O(1) lookup+insert per record, so only ~20,000 operations.");
    }

    static void task4() {
        System.out.println("\n=== TASK 4: Walk-in token Queue and Recent-patients Stack ===");
        LinkedQueue<String> tokens = new LinkedQueue<>();
        String[] walkIns = {"T-101 Asha", "T-102 Ravi", "T-103 Meena"};
        for (String w : walkIns) { tokens.enqueue(w); System.out.println("Token issued   : " + w); }
        System.out.println("Next to call   : " + tokens.peek());
        System.out.println("Serving        : " + tokens.dequeue());
        System.out.println("Serving        : " + tokens.dequeue());
        System.out.println("Still waiting  : " + tokens.size());
        tokens.dequeue();
        try { tokens.dequeue(); } catch (NoSuchElementException e) { System.out.println("Empty queue check: " + e.getMessage()); }

        ArrayStack<String> recent = new ArrayStack<>();
        String[] viewed = {"P-1001", "P-1002", "P-1003", "P-1004", "P-1005"};
        for (String v : viewed) { recent.push(v); System.out.println("Viewed record : " + v); }
        System.out.println("Most recent    : " + recent.peek());
        System.out.println("Back ->          " + recent.pop());
        System.out.println("Back ->          " + recent.pop());
        System.out.println("History size   : " + recent.size());
    }

    static void task5() {
        System.out.println("\n=== TASK 5: Max appointments in a shift (DP vs Greedy) ===");
        Slot[] shift = {
            new Slot(540, 600), new Slot(570, 660), new Slot(600, 630), new Slot(620, 700),
            new Slot(660, 720), new Slot(690, 780), new Slot(720, 750), new Slot(780, 840)
        };
        System.out.println("Slots (min after midnight): " + Arrays.toString(shift));
        System.out.println("DP     (max count)   : " + ShiftScheduler.dpMax(shift, false));
        System.out.println("Greedy (max count)   : " + ShiftScheduler.greedyMax(shift));

        Slot[] trap = { new Slot(0, 100), new Slot(10, 20), new Slot(30, 40) };
        System.out.println("\nTrap case " + Arrays.toString(trap));
        System.out.println("Max COUNT   -> DP=" + ShiftScheduler.dpMax(trap, false) + ", Greedy=" + ShiftScheduler.greedyMax(trap));
        System.out.println("Max MINUTES -> DP=" + ShiftScheduler.dpMax(trap, true) + "  (greedy would only give 20)");

        Random r = new Random(5);
        int mismatches = 0;
        for (int t = 0; t < 1000; t++) {
            Slot[] x = new Slot[15];
            for (int i = 0; i < x.length; i++) { int st = r.nextInt(600); x[i] = new Slot(st, st + 15 + r.nextInt(120)); }
            if (ShiftScheduler.dpMax(x, false) != ShiftScheduler.greedyMax(x)) mismatches++;
        }
        System.out.println("\n1,000 random shifts: DP vs greedy count mismatches = " + mismatches);
        System.out.println("Explanation: for COUNT, earliest-finish greedy is optimal and O(n log n) (just a sort);");
        System.out.println("DP is also O(n log n) here and is more general: it still works when slots carry weights/durations.");
    }
}
