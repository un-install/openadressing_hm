import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**In open addressing hash maps collisions resolving by finding a vacant cell(Unlike Chaining which from ceil a List),
 and search must have the same sequence of cell tries.
In addition, it is better if any cell is tried not more than one time
The tries algorithm is Square Trying*/
public class IntLongOpenAddrHashMap {
    //MAX_ARRAY_SIZE is a max integer which is power of 2
    //it depends on -Xmx value. Absolute max is Integer.MAX_VALUE + 1 >>> 1
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE + 1 >>> 3;
    private int initialSize = 128;
    //size of entryArray is always power of 2. This caused by collision resolving algorithm
    private IntLongEntry[] entryArray;
    private int loadFactor = 70;
    private long actualSize;

    public IntLongOpenAddrHashMap() {
        entryArray = new IntLongEntry[initialSize];
    }

    public IntLongOpenAddrHashMap(int initialSizePower, int loadFactor) {
        if (initialSizePower <= 0 || loadFactor > 100 || loadFactor <= 0) {
            throw new IllegalArgumentException();
        }
        //if 2^initialSizePower > MAX_ARRAY_SIZE initialSize sets to MAX_ARRAY_SIZE otherwise it sets to 1st
        this.initialSize = initialSizePower < 28 ? 1 << initialSizePower : MAX_ARRAY_SIZE;
        this.loadFactor = loadFactor;
        entryArray = new IntLongEntry[initialSize];
    }

    public void add(int key, long value) throws ResizeCeilException {
        //resize condition
        if (actualSize * 100 / entryArray.length >= loadFactor) {
            resize();
        }
        //adding entry
        for (int i = 0; ; i++) {
            int index = (key + i * i) % entryArray.length;
            if (entryArray[index] == null || entryArray[index].getKey() == key ||
                    entryArray[index] instanceof DeletedEntry) {
                entryArray[index] = new IntLongEntry(key, value);
                break;
            }
        }
        actualSize++;
    }

    public IntLongEntry get(int key) {
        IntLongEntry entry = entryArray[getIndexOfNullOrEq(key)];
        return entry instanceof DeletedEntry ? null : entry;
    }

    //return number of filled cells in entry array.
    // That might be done by returning entrySet size, but it would require extra time and memory;
    public long size() {
        return actualSize;
    }

    //if loadFactor is reached it creats new array, 2 times bigger, and readd all entrys (excepting deleted)
    private void resize() throws ResizeCeilException {
        if (entryArray.length == MAX_ARRAY_SIZE) {
            loadFactor = 100;
            throw new ResizeCeilException("max size of array riched. cannot resize");
        }
        IntLongEntry[] temp = entryArray;
        entryArray = new IntLongEntry[temp.length << 1];

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null && !(temp[i] instanceof DeletedEntry)) {
                add(temp[i].getKey(), temp[i].getValue());
                actualSize--;
            }
        }
    }
    private int getIndexOfNullOrEq(int key) {
        int index;
        for (int i = 0; ; i++) {
            index = (key + i * i) % entryArray.length;
            if (entryArray[index] == null || entryArray[index].getKey() == key) {
                break;
            }
        }
        return index;
    }

    //For the removing in OA hash maps assigning to null is not allowed because it will break a search.
    //So, it just marks by marker Class. All DeletedEntries are will miss wile resizing
    public void remove(int key) {
        entryArray[getIndexOfNullOrEq(key)] = new DeletedEntry();
        actualSize--;
    }

    public Set<IntLongEntry> getEntrySet() {
        return new HashSet<>(Arrays.asList(entryArray));
    }

    public int getLoadFactor() {
        return loadFactor;
    }

    public int getEntryArraySize(){
        return entryArray.length;
    }
}