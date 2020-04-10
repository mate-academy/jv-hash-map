package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int arrayCapacity;
    private int elementsNumber;
    private float threshold = arrayCapacity * LOAD_FACTOR;
    private Node<K, V>[] nodesArray;

    public MyHashMap() {
        nodesArray = new Node[DEFAULT_CAPACITY];
        arrayCapacity = nodesArray.length;
        elementsNumber = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> tableNode = nodesArray[index];
        Node<K, V> newNode = new Node<>(key, value, tableNode);
        while (tableNode != null) {
            if (tableNode.key == key || key != null && key.equals(tableNode.key)) {
                tableNode.value = value;
                return;
            }
            tableNode = tableNode.nextNode;
        }
        nodesArray[index] = newNode;
        elementsNumber++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> answer = nodesArray[getIndex(key)];
        if (answer == null) {
            return null;
        }
        while (!(key == answer.key || key != null && key.equals(answer.key))) {
            answer = answer.nextNode;
        }
        return answer.value;
    }

    @Override
    public int getSize() {
        return elementsNumber;
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> nextNode;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.nextNode = next;
        }
    }

    private void resize() {
        if (elementsNumber == threshold) {
            arrayCapacity *= 2;
            Node<K, V>[] newTable = new Node[arrayCapacity];
            for (Node<K, V> node : nodesArray) {
                while (node != null) {
                    int index = getIndex(node.key);
                    Node<K, V> newTableNode = newTable[index];
                    Node<K, V> oldNode = node;
                    oldNode.nextNode = newTableNode;
                    newTable[index] = oldNode;
                    node = node.nextNode;
                }
            }
            nodesArray = newTable;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % arrayCapacity;
    }
}
