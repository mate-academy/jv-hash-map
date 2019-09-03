package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private Node<K, V>[] storage;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;

    public MyHashMap() {
        storage = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hashIndex(key);
        Node newNode = new Node(key, value, null);
        if (storage[index] == null) {
            storage[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = storage[index];
            for (int i = 0; i < storage.length; i++) {
                if (key == currentNode.key || newNode.key.equals(currentNode.key)) {
                    currentNode.value = value;
                    break;
                } else {
                    if (currentNode.next == null) {
                        currentNode.next = newNode;
                        size++;
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hashIndex(key);
        Node<K, V> currentNode = storage[index];
        if (currentNode == null) {
            return null;
        }

        for (int i = 0; i < storage.length; i++) {
            if (key == currentNode.key || key.equals(currentNode.key)) {
                return currentNode.value;
            } else {
                currentNode = currentNode.next;
                if (currentNode == null) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % storage.length);
    }

    private void resize() {
        if (size > storage.length * LOAD_FACTOR) {
            Node<K, V>[] oldStorage = storage;
            storage = new Node[storage.length * 2];
            size = 0;
            for (int i = 0; i < oldStorage.length; i++) {
                Node<K, V> oldNode = oldStorage[i];
                while (oldNode != null) {
                    put(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
