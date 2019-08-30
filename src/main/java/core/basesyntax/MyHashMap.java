package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75d;
    private static final int BASE_SIZE = 16;
    private static final int INDEX_OFF = -1;
    private int size;
    private int actualSize;
    private Entry[] entries;

    public MyHashMap() {
        this.entries = new Entry[BASE_SIZE];
        actualSize = 0;
        size = BASE_SIZE;
    }

    private int indexOf(K key) {
        for (int i = 0; i < actualSize; i++) {
            if (entries[i].getKey() == null && key == null) {
                return i;
            }
            if (key.equals(entries[i].getKey())) {
                return i;
            }
        }
        return INDEX_OFF;
    }

    private void resize() {
        if (actualSize > size * LOAD_FACTOR) {
            size = (int) (size * 2 * LOAD_FACTOR);
            Entry[] newEntries = new Entry[size];
            System.arraycopy(entries, 0, newEntries, 0, actualSize);
            entries = newEntries;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (indexOf(key) == INDEX_OFF) {
            Entry temp = new Entry(key, value);
            entries[actualSize] = temp;
            actualSize++;
        } else {
            Entry temp = new Entry(key, value);
            entries[indexOf(key)] = temp;
        }
        Entry temp = new Entry(key, value);
        entries[actualSize] = temp;
    }

    @Override
    public V getValue(K key) {

        if (indexOf(key) == INDEX_OFF) {
            return null;
        }
        return (V) entries[indexOf(key)].getValue();
    }

    @Override
    public int getSize() {
        return actualSize;
    }
}
