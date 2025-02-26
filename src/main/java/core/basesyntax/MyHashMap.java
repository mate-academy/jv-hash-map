package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private V prev;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (capacity * LOAD_FACTOR);
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        // Якщо комірка пуста, створюємо новий вузол
        if (table[index] == null) {
            table[index] = new Node<>(index, key, value, null);
        } else {
            Node<K, V> current = table[index];
            while (true) {
                if (Objects.equals(current.key, key)) {
                    // Оновлення значення, якщо ключ вже є
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    // Додаємо новий вузол у кінець списку (колізія)
                    current.next = new Node<>(index, key, value, null);
                    break;
                }
                current = current.next;
            }
        }
        size++;

        // Перевірка, чи потрібно збільшувати масив
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; // Якщо ключа немає
    }

    @Override
    public void removeValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (prev == null) {
                    // Видаляємо перший елемент у комірці
                    table[index] = current.next;
                } else {
                    // Видаляємо елемент у середині або в кінці списку
                    prev.next = current.next;
                }
                size--;
                return; // Видалення успішне
            }
            prev = current;
            current = current.next;
        }
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = hash(node.key);
                Node<K, V> next = node.next;

                // Додаємо в новий масив, зберігаючи зв'язок
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = next;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }
}

