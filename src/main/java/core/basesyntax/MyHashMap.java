package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    // Внутрішній клас для представлення пари ключ-значення
    private static class Node<K, V> {
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
        table = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * 0.75) {
            resize();
        }

        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);

        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                current.value = value; // Оновлюємо значення, якщо ключ вже існує
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode; // Вставляємо новий вузол
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key); // Отримуємо індекс для зберігання
        Node<K, V> current = table[index];

        while (current != null) {
            // Перевірка для null-ключа
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                return current.value; // Повертаємо значення, якщо знайдено ключ
            }
            current = current.next; // Перехід до наступного вузла
        }

        return null; // Якщо ключ не знайдено, повертаємо null
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode() % table.length));
    }

    private void resize() {
        int newCapacity = table.length * 2; // Збільшуємо розмір у 2 рази
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = (node.key == null ? 0 : Math.abs(node.key.hashCode() % newCapacity));
                Node<K, V> nextNode = node.next;

                // Переносимо вузол у новий масив
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = nextNode;
            }
        }
        table = newTable; // Замінюємо стару таблицю новою
    }
}
