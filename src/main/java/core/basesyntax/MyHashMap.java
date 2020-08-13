package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int elementCount;
    private Cell<K, V>[] hashMap;

    public MyHashMap() {
        hashMap = new Cell[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (elementCount > LOAD_FACTOR * hashMap.length) {
            resize();
        }
        Cell<K, V> currentCell = getCell(key);
        if (currentCell == null) {
            int index = getCellNumber(key);
            hashMap[index] = new Cell<>(key, value, hashMap[index]);
            elementCount++;
        } else {
            currentCell.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Cell<K, V> currentCell = hashMap[getCellNumber(key)];
        while (currentCell != null) {
            if (Objects.equals(currentCell.key, key)) {
                return currentCell.value;
            }
            currentCell = currentCell.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return elementCount;
    }

    private void resize() {
        elementCount = 0;
        Cell<K, V>[] oldHashMap = hashMap;
        hashMap = new Cell[hashMap.length * 2];
        for (Cell<K, V> cell : oldHashMap) {
            while (cell != null) {
                put(cell.key, cell.value);
                cell = cell.next;
            }
        }
    }

    private int getCellNumber(K key) {
        return key == null ? 0 : (key.hashCode() & hashMap.length - 1);
    }

    private Cell<K, V> getCell(K key) {
        Cell<K, V> currentCell = hashMap[getCellNumber(key)];
        while (currentCell != null) {
            if (Objects.equals(key, currentCell.key)) {
                return currentCell;
            }
            currentCell = currentCell.next;
        }
        return null;
    }

    private static class Cell<K, V> {
        private K key;
        private V value;
        private Cell<K, V> next;

        public Cell(K key, V value, Cell<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
