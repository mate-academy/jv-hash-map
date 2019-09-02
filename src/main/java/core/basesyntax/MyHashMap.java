package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75d;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int INDEX_OFF = -1;
    private int capacity;
    private int size;
    private Entry[] entries;

    public MyHashMap() {
        this.entries = new Entry[DEFAULT_CAPACITY];
        size = 0;
        capacity = DEFAULT_CAPACITY;
    }

    private int indexCalculation(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        if (size > capacity * LOAD_FACTOR) {
            capacity = (int) (capacity * 2 * LOAD_FACTOR);
            Entry[] oldEntries = entries;
            entries = new Entry[capacity];
            size = 0;
            for (int i = 0; i < oldEntries.length; i++) {
                Entry<K, V> temp = oldEntries[i];
                while (temp != null) {
                    put(temp.getKey(), temp.getValue());
                    temp = temp.getNext();
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = indexCalculation(key);
        Entry newEntry = new Entry(key, value, null);
        if (entries[index] == null) {
            entries[index] = newEntry;
            size++;
        } else {
            Entry tempEntry = entries[index];
            while (true) {
                if (key == tempEntry.getKey() || newEntry.getKey().equals(tempEntry.getKey())) {
                    tempEntry.setValue(value);
                    break;
                } else {
                    if (tempEntry.getNext() == null) {
                        tempEntry.setNext(newEntry);
                        size++;
                        break;
                    } else {
                        tempEntry = tempEntry.getNext();
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexCalculation(key);
        Entry tempEntry = entries[index];
        if (tempEntry == null) {
            return null;
        }
        while (true) {
            if (key == tempEntry.getKey() || key.equals(tempEntry.getKey())) {
                return (V) tempEntry.getValue();
            } else {
                tempEntry = tempEntry.getNext();
                if (tempEntry == null) {
                    return null;
                }
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
