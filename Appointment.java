public class Appointment {
    public final int id;
    public final int patientId;
    public final int urgency;
    public final int arrival;
    public final int slot;

    public Appointment(int id, int patientId, int urgency, int arrival, int slot) {
        this.id = id;
        this.patientId = patientId;
        this.urgency = urgency;
        this.arrival = arrival;
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "A" + id + "(U" + urgency + ",t" + arrival + ")";
    }
}
