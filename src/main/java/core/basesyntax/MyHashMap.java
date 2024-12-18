package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
        this.size = 0;
        this.threshold = (int) (capacity * LOAD_FACTOR);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() & Integer.MAX_VALUE; // Безопасный хэш
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = hash(node.key) % capacity;
                Node<K, V> next = node.next;

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }

        table = newTable;
    }

    @Override
    public void put(K key, V value) {
        // Обработка null-ключа
        if (key == null) {
            if (table[0] == null) {
                table[0] = new Node<>(null, value, null);
                size++;
            } else {
                table[0].value = value;
            }
            return;
        }

        int index = hash(key) % capacity;
        Node<K, V> current = table[index];

        // Если ячейка пуста, добавляем новый узел
        if (current == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            if (size >= threshold) {
                resize();
            }
            return;
        }

        // Проверяем цепочку узлов
        while (true) {
            if (current.key.equals(key)) {
                current.value = value; // Обновляем значение для существующего ключа
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }

        // Добавляем новый узел в конец цепочки
        current.next = new Node<>(key, value, null);
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0] == null ? null : table[0].value;
        }

        int index = hash(key) % capacity;
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
