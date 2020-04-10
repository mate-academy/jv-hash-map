package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFOULT_CAPASITY = 16;
    static final double LOAD_FACTOR = 0.75;
    private Entry<K, V>[] table;
    private int size;

    public <K, V> MyHashMap() {
        table = new Entry[DEFOULT_CAPASITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
        } else {
            int indexInTable = indexForEntry(key.hashCode(), table.length);
            Entry<K, V> presentInEntry = table[indexInTable];
            if (presentInEntry == null) {
                table[indexInTable] = addEntry(key.hashCode(), key, value, null);
                size++;
                resize();
            } else {
                do {
                    if (presentInEntry.key != null && key.hashCode() == presentInEntry.key.hashCode()
                            && (key == presentInEntry.key || key.equals(presentInEntry.key))) {
                        presentInEntry.value = value;
                        return;
                    }
                    if (presentInEntry.next == null) {
                        break;
                    } else {
                        presentInEntry = presentInEntry.next;
                    }
                } while (presentInEntry != null);
                table[indexInTable] = addEntry(key.hashCode(), key, value, table[indexInTable]);
                size++;
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            if (table[0] != null) {
                Entry<K, V> temp = table[0];
                do {
                    if (temp.key == null) {
                        return temp.value;
                    }
                    temp = temp.next;
                } while (temp != null);
                return null;
            } else {
                return null;
            }
        } else {
            int indexInTable = indexForEntry(key.hashCode(), table.length);
            Entry<K, V> temp = table[indexInTable];
            if (temp == null) {
                return null;
            }
            do {
                if (temp.key != null && temp.key.hashCode() == key.hashCode() && key.equals(temp.key)) {
                    return temp.value;
                }
                temp = temp.next;
            } while (temp != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transfer(Entry<K, V>[] newEntry) {
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                entry.copyTo(newEntry);
                while (entry.next != null) {
                    entry.next.copyTo(newEntry);
                    entry.next = entry.next.next;
                }
            }
        }
    }

    private void resize() {
        if (size > table.length * LOAD_FACTOR) {
            Entry<K, V>[] newTable = new Entry[table.length * 2];
            transfer(newTable);
            table = newTable;
        }
    }

    private void putForNullKey(V value) {
        if (table[0] != null) {
            Entry<K, V> temp = table[0];
            do {
                if (temp.key == null) {
                    temp.value = value;
                    return;
                }
                if (temp.next == null) {
                    break;
                }
                temp = temp.next;
            } while (temp != null);
            table[0] = addEntry(0, null, value, table[0]);
            size++;
            resize();

        } else {
            table[0] = addEntry(0, null, value, null);
            size++;
            resize();
        }
    }

    private Entry<K, V> addEntry(int hashKay, K key, V value, Entry<K, V> next) {
        Entry<K, V> entry = new Entry<>();
        entry.key = key;
        entry.value = value;
        entry.next = next;
        return entry;
    }

    public int indexForEntry(int hashCode, int capacity) {
        return Math.abs(hashCode % capacity);
    }

    private class Entry<K, V> {
        Entry<K, V> next;
        K key;
        V value;

        private void copyTo(Entry<K, V>[] newEntryCopy) {
            Entry<K, V> entry = new Entry<>();
            entry.key = key;
            entry.value = value;
            entry.next = newEntryCopy[indexForEntry(key.hashCode(), newEntryCopy.length)];
            newEntryCopy[indexForEntry(key.hashCode(), newEntryCopy.length)] = entry;
        }
    }
}
