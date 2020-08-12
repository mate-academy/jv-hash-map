package core.basesyntax;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] elementData;
    private int size;
    private int currentCapacity = DEFAULT_CAPACITY;

    public MyHashMap() {
        elementData = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size > currentCapacity * LOAD_FACTOR) {
            resizeAndTransfer();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getIndexFor(key.hashCode());
        if (null == elementData[index]) {
            addEntry(key, value, index);
        } else {
            replaceOrAddEntry(key, value, index);
        }
    }

    private void resizeAndTransfer() {
        currentCapacity = currentCapacity * 2;
        LinkedList<Entry<K, V>>[] oldElementData = elementData;
        elementData = new LinkedList[currentCapacity];
        size = 0;
        for (List<Entry<K, V>> list : oldElementData) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : getIndexFor(key.hashCode());
        List<Entry<K, V>> list = elementData[index];
        if (list == null) {
            return null;
        }
        for (Entry<K, V> currentEntry : list) {
            if (key == currentEntry.key || (key != null && key.equals(currentEntry.key))) {
                return currentEntry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        if (null == elementData[0]) {
            addEntry(null, value, 0);
            return;
        }
        replaceOrAddEntry(null, value, 0);
    }

    private void replaceOrAddEntry(K key, V value, int index) {
        boolean replaced = false;
        List<Entry<K, V>> list = elementData[index];
        for (Entry<K, V> currentEntry : list) {
            if (key == currentEntry.key || (key != null && key.equals(currentEntry.key))) {
                currentEntry.value = value;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            list.add(new Entry<K, V>(key, value));
            size++;
        }
    }

    private void addEntry(K key, V value, int index) {
        LinkedList<Entry<K, V>> list = new LinkedList();
        list.add(new Entry<>(key, value));
        elementData[index] = list;
        size++;
    }

    private int getIndexFor(int hashCode) {
        return Math.abs(hashCode) % currentCapacity;
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
