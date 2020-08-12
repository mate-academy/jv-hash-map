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
    private int currentCapacity;
    private Node<K, V>[] hashMapList;

    public MyHashMap() {
        currentCapacity = DEFAULT_CAPACITY;
        hashMapList = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % currentCapacity;
        }
        Node<K, V> newElement = new Node<>(key, value);
        if (hashMapList[index] != null) {
            Node<K, V> element = hashMapList[index];
            do {
                if ((key == null || element.key == null || element.key.hashCode() == key.hashCode())
                        && ((element.key == key) || (key != null && key.equals(element.key)))) {
                    element.value = value;
                    return;
                }
                if (element.nextItem == null) {
                    break;
                }
                element = element.nextItem;
            } while (true);
            element.nextItem = newElement;
        } else {
            hashMapList[index] = newElement;
        }
        size++;
        checkSize();
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % currentCapacity;
        }
        Node<K, V> element = hashMapList[index];
        do {
            if ((key == null || element.key == null || element.key.hashCode() == key.hashCode())
                    && ((element.key == key) || (key != null && key.equals(element.key)))) {
                return element.value;
            }
            element = element.nextItem;
        } while (element != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addElement(Node<K, V>[] list, Node<K, V> node) {
        int index = 0;
        if (node.key != null) {
            index = Math.abs(node.key.hashCode()) % currentCapacity;
        }
        if (list[index] != null) {
            Node<K, V> element = list[index];
            while (element.nextItem != null) {
                element = element.nextItem;
            }
            element.nextItem = node;
        } else {
            list[index] = node;
        }
    }

    private void checkSize() {
        if (currentCapacity * LOAD_FACTOR < size) {
            currentCapacity = currentCapacity * 2;
            Node<K, V>[] newHashMapList = new Node[currentCapacity];
            for (int i = 0; i < hashMapList.length; i++) {
                if (hashMapList[i] != null) {
                    Node<K, V> movingElement = hashMapList[i];
                    do {
                        if (movingElement.nextItem != null) {
                            Node<K, V> nextElement = movingElement.nextItem;
                            addElement(newHashMapList, movingElement);
                            movingElement.nextItem = null;
                            movingElement = nextElement;
                        } else {
                            addElement(newHashMapList, movingElement);
                            break;
                        }
                    } while (true);
                }
            }
            hashMapList = newHashMapList;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextItem;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
