package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int initialCapacity = 16;
    private static final double loadFactor = 0.75;
    private int elementCount;
    private Cell[] hashMap;

    public MyHashMap() {
        hashMap = new Cell[initialCapacity];
    }

    private void resize() {
        elementCount = 0;
        Cell<K, V>[] oldHashMap = hashMap;
        hashMap = new Cell[hashMap.length * 2];
        for (Cell cell : oldHashMap) {
            while (cell != null) {
                put((K) cell.key, (V) cell.getValue());
                cell = cell.next;
            }
        }
        getSize();
    }

    @Override
    public void put(K key, V value) {
        if (elementCount > loadFactor * hashMap.length) {
            resize();
        }
        Cell<K, V> currentCell = getCell(key);
        if (currentCell == null) {
            hashMap[getCellNumber(key)] = new Cell<>(key, value, hashMap[getCellNumber(key)]);
            elementCount++;
        } else {
            currentCell.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Cell currentCell = hashMap[getCellNumber(key)];
        while (currentCell != null) {
            if (Objects.equals(currentCell.getKey(), key)) {
                return (V) currentCell.getValue();
            }
            currentCell = currentCell.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return elementCount;
    }

    private int getCellNumber(K key) {
        return key == null ? 0 : (Math.abs(key.hashCode()) - 1) % initialCapacity + 1;
    }

    private Cell<K, V> getCell(K key) {
        Cell<K, V> currentCell = hashMap[getCellNumber(key)];
        while (currentCell != null) {
            if (Objects.equals(key, currentCell.getKey())) {
                return currentCell;
            }
            currentCell = currentCell.next;
        }
        return null;
    }

    private class Cell<K, V> {
        private K key;
        private V value;
        private Cell<K, V> next;

        public Cell(K key, V value, Cell<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public V getValue() {
            return value;
        }

        public K getKey() {
            return key;
        }

        public int getSize() {
            return elementCount;
        }
    }
}
