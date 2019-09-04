package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkResize();
        putValue(indexByHash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        return getByIndex(indexByHash(key), key);
    }


    @Override
    public int getSize() {
        return size;
    }

    private int indexByHash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() & (table.length - 1);
    }

    private void checkResize() {
        if (size >= (int) (table.length * LOAD_FACTOR)) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            if (currentNode != null) {
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private void putValue(int index, K key, V value) {
        Node<K, V> basket = table[index];
        if (basket == null) {
            table[index] = new Node(key, value, null);
        } else {
            if (basket.key == key) { // check for key == null
                basket.value = value;
                return;
            }
            Node<K, V> currentNodeInBasket = basket;
            while (currentNodeInBasket.next != null) {
                if (currentNodeInBasket.key != null && currentNodeInBasket.key.equals(key)) {
                    currentNodeInBasket.value = value;
                    return;
                }
                currentNodeInBasket = currentNodeInBasket.next;
            }
            currentNodeInBasket.next = new Node(key, value, null);
        }
        size++;
    }

    private V getByIndex(int indexByHash, K key) {
        Node<K, V> basket = table[indexByHash];
        if (basket == null) {
            return null;
        }
        if (basket.key == key) {
            return basket.value;
        }
        Node<K, V> currentNodeInBasket = basket;
        while (currentNodeInBasket != null) {
            if (currentNodeInBasket.key != null && currentNodeInBasket.key.equals(key)) {
                return currentNodeInBasket.value;
            }
            currentNodeInBasket = currentNodeInBasket.next;
        }
        return null;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
        }
    }
}