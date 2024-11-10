package core.basesyntax;


import java.util.LinkedList;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] table;
    private int size;

    private static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        table = new LinkedList[INITIAL_CAPACITY];
        size = 0;
    }

    // Метод для расчета индекса ячейки на основе хэш-кода ключа
    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        // Инициализируем ячейку, если она еще пустая
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        // Проверяем, есть ли уже ключ в цепочке
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value; // Заменяем значение, если ключ уже есть
                return;
            }
        }

        // Если ключ новый, добавляем его в цепочку
        table[index].add(new Entry<>(key, value));
        size++;

        // Увеличиваем размер, если превышен коэффициент заполнения
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        if (bucket != null) {
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null; // Возвращаем null, если ключ не найден

    }

    private void resize() {
        LinkedList<Entry<K, V>>[] oldTable = table;
        table = new LinkedList[oldTable.length * 2];
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }
}
