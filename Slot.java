public class Slot {
    public final int start, end;   // minutes after midnight

    public Slot(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int duration() {
        return end - start;
    }

    @Override
    public String toString() {
        return "[" + start + "-" + end + "]";
    }
}
