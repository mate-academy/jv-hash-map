package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROWTH_RATE = 2;
    private Node<K, V>[] arrayNodes;
    private int size;
    private int actualCapacity;
    private int threshold;

    private static class Node<K, V> {
        private int hashKey;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hashKey, K key, V value) {
            this.hashKey = hashKey;
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.arrayNodes = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        actualCapacity = INITIAL_CAPACITY;
        threshold = (int) (actualCapacity * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        int hashKey = key == null ? 0 : key.hashCode();
        Node<K,V> newNode = new Node<>(hashKey, key, value);
        int index = key == null ? 0 : Math.abs(key.hashCode()) % actualCapacity;
        if (arrayNodes[index] == null) {
            arrayNodes[index] = newNode;
        } else {
            Node<K,V> currentNode = arrayNodes[index];
            if (findNodeByKey(key) != null) {
                currentNode = findNodeByKey(key);
                if (currentNode != null) {
                    currentNode.value = value;
                }
                return;
            } else {
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = newNode;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = findNodeByKey(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        int index;
        actualCapacity = actualCapacity * GROWTH_RATE;
        threshold = (int)(actualCapacity * LOAD_FACTOR);
        Node<K, V>[] newArrayNodes = (Node<K, V>[]) new Node[actualCapacity];
        for (Node<K, V> currentNode : arrayNodes) {
            while (currentNode != null) {
                // Обчислює індекс нового масиву згідно розміру масиву
                index = Math.abs(currentNode.hashKey % actualCapacity);
                // зберегти наступну ноду старого масиву
                Node<K, V> nextNode = currentNode.next;
                // перепривласнити ноді next взявши ноду з нового масиву по індексу
                // з початку буде null, що зробить першу прийняту ноду хвостом
                currentNode.next = newArrayNodes[index];
                // забрати цю ноду в новий масив
                newArrayNodes[index] = currentNode;
                // Перехід на наступну ноду в старому массиві
                currentNode = nextNode;
            }
        }
        arrayNodes = newArrayNodes;
    }

    private Node<K, V> findNodeByKey(K key) {
        int index = key == null ? 0 : Math.abs(key.hashCode()) % actualCapacity;
        Node<K, V> currentNode = arrayNodes[index];
        while (currentNode != null) {
            if (isEqualKeys(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private boolean isEqualKeys(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 != null) {
            return key1.equals(key2);
        }
        return false;
    }

}
