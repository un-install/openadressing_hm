import java.util.Objects;

public class IntLongEntry {
    private int key;
    private long value;

    public IntLongEntry() {
    }

    public IntLongEntry(int key, long value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntLongEntry)) return false;
        IntLongEntry entry = (IntLongEntry) o;
        return key == entry.key &&
                value == entry.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IntLongEntry{");
        sb.append("key=").append(key);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
