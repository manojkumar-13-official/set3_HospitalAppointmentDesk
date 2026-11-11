import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DoubleBookingDetector {

    private static long key(Appointment a) {
        return (long) a.patientId * 1000L + a.slot;   // unique (patient, slot) pair
    }
    public static List<Appointment> findWithHashSet(Appointment[] all) {
        Set<Long> seen = new HashSet<>();
        List<Appointment> dup = new ArrayList<>();
        for (Appointment a : all) if (!seen.add(key(a))) dup.add(a);
        return dup;
    }
    public static int findWithListScan(Appointment[] all) {
        int count = 0;
        for (int i = 0; i < all.length; i++)
            for (int j = 0; j < i; j++)
                if (all[j].patientId == all[i].patientId && all[j].slot == all[i].slot) { count++; break; }
        return count;
    }
}
