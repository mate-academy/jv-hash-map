package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    int size;
    Node<K, V>[] table;
    int capacity = 16;
    double load_factor = 0.75;
    int threshold = (int) (capacity * load_factor);

    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public int getIndex(K key) {
        return key.hashCode() % capacity;
    }

    @Override
    public void put(K key, V value) {
        // Якщо розмір більше порогу, подвоюємо розмір таблиці
        if (size >= threshold) {
            capacity = capacity * 2;
            threshold = (int) (capacity * load_factor);
            Node<K, V>[] newTable = new Node[capacity];

            // Переміщаємо елементи в нову таблицю
            for (int i = 0; i < table.length; i++) {
                Node<K, V> current = table[i];
                while (current != null) {
                    int newHash = current.key.hashCode() % capacity;
                    Node<K, V> next = current.next;
                    current.next = newTable[newHash];
                    newTable[newHash] = current;  // Оновлюємо індекс в новій таблиці
                    current = next;  // Переходимо до наступного елемента в старій таблиці
                }
            }
            table = newTable;  // Оновлюємо таблицю
        }

        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        // Перевіряємо, чи є елемент з таким ключем
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;  // Оновлюємо значення
                return;  // Виходимо, якщо елемент знайдено
            }
            current = current.next;
        }

        // Якщо елемент з таким ключем не знайдений, додаємо новий елемент в ланцюг
        newNode.next = table[index];
        table[index] = newNode;

        size++;  // Збільшуємо розмір хеш-мапи
    }

    @Override
    public V getValue(K key) {
        int hashcode = key.hashCode() % capacity;
        Node<K, V> current = table[hashcode];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;  // Повертаємо значення, якщо знайдено
            }
            current = current.next;
        }
        return null;  // Якщо не знайдено, повертаємо null
    }

    @Override
    public int getSize() {
        return size;  // Повертаємо поточний розмір хеш-мапи
    }
}
