package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_CAPACITY = 16;
    static final Double loadFactor = 0.75;
    private int overload;
    private Node<K, V>[] data;
    private int elementsData;

    public MyHashMap() {
        data = new Node[DEFAULT_CAPACITY];
        elementsData = 0;
        overload = data.length * loadFactor.intValue();
    }

    @Override
    public void put(K key, V value) {
        if (elementsData == overload) {
            resize();
        }
        Node<K, V> element = searchIfElementExists(key, hash(key));
        if (element == null) {
            Node<K, V> buffer = new Node<>(key, value, data[hash(key)]);
            data[hash(key)] = buffer;
            elementsData++;
            return;
        }
        element.value = value;
    }

    private Node<K, V> searchIfElementExists(K key, int index) {
        Node<K, V> element = data[index];
        while (element != null) {
            if (key == element.key || key != null && key.equals(element.key)) {
                return element;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = searchIfElementExists(key, hash(key));
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return elementsData;
    }

    private void resize() {
        Node<K, V>[] newData = new Node[data.length * 2];
        overload = newData.length * loadFactor.intValue();
        elementsData = 0;
        Node<K, V>[] buffer = data.clone();
        data = newData;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == null) {
                continue;
            }
            put(buffer[i].key, buffer[i].value);
        }
    }

    private int hash(K entryKey) {
        return entryKey != null
                ? Math.abs(entryKey.hashCode() % data.length)
                : 0;
    }

    private class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;
        private Node<K, V> last;
        private Node<K, V> first;

        public Node(K entryKey, V entryValue, Node<K, V> nextNode) {
            key = entryKey;
            value = entryValue;
            next = nextNode;
        }

        @Override
        public String toString() {
            return key + "";
        }
    }

}
