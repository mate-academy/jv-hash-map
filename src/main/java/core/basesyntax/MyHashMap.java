package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private Node<K, V>[] table;
    private int capacity = 16;
    private double loadFactor = 0.75;
    private int threshold = (int) (capacity * loadFactor);

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public MyHashMap() {
        table = new Node[capacity]; // Ініціалізація таблиці
    }

    public int getIndex(K key) {
        if (key == null) {
            return 0; // Призначаємо індекс 0 для null ключа
        }
        return key.hashCode() % capacity; // Звичайне обчислення хешу
    }

    @Override
    public void put(K key, V value) {
        // Якщо розмір більше порогу, подвоюємо розмір таблиці
        if (size >= threshold) {
            capacity = capacity * 2;
            threshold = (int) (capacity * loadFactor);
            Node<K, V>[] newTable = new Node[capacity];

            // Переміщаємо елементи в нову таблицю
            for (int i = 0; i < table.length; i++) {
                Node<K, V> current = table[i];
                while (current != null) {
                    int newHash = current.key == null ? 0 : current.key.hashCode() % capacity; // Якщо ключ null,
                    // використовуємо 0
                    Node<K, V> next = current.next;
                    current.next = newTable[newHash];
                    newTable[newHash] = current; // Оновлюємо індекс в новій таблиці
                    current = next; // Переходимо до наступного елемента в старій таблиці
                }
            }
            table = newTable; // Оновлюємо таблицю
        }

        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        // Перевіряємо, чи є елемент з таким ключем
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value; // Оновлюємо значення
                return; // Виходимо, якщо елемент знайдено
            }
            current = current.next;
        }

        // Якщо елемент з таким ключем не знайдений, додаємо новий елемент в ланцюг
        newNode.next = table[index];
        table[index] = newNode;

        size++; // Збільшуємо розмір хеш-мапи
    }

    @Override
    public V getValue(K key) {
        int hashcode = getIndex(key);
        Node<K, V> current = table[hashcode];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value; // Повертаємо значення, якщо знайдено
            }
            current = current.next;
        }
        return null; // Якщо не знайдено, повертаємо null
    }

    @Override
    public int getSize() {
        return size; // Повертаємо поточний розмір хеш-мапи
    }
}
