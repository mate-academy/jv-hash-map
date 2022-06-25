package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] arrayNodes;
    private final int defaultCapacity = 16;
    private final float loadFactor = 0.75f;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.arrayNodes = (Node<K, V>[]) new Node[defaultCapacity];
        this.threshold = (int) (loadFactor * arrayNodes.length);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int backet = getHash(newNode);
        if (backet < 0 || backet > arrayNodes.length) {
            throw new IndexOutOfBoundsException();
        }
        if (arrayNodes[backet] == null) {
            simplePut(newNode);
            return;
        }
        if (getHash(arrayNodes[backet]) == backet) {
            putWithCollision(newNode);
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        V value;
        if (key == null) {
            return getValueByNullKey(key);
        }
        for (Node<K, V> nodeForGetValue : arrayNodes) {
            if (nodeForGetValue != null && key.equals(getKey(nodeForGetValue))) {
                value = nodeForGetValue.value;
                return value;
            }
            if (nodeForGetValue != null && nodeForGetValue.next != null) {
                Node<K, V> nextNode = nodeForGetValue.next;
                while (nextNode != null) {
                    if (getKey(nextNode) == key
                            || (getKey(nextNode) != null && getKey(nextNode).equals(key))) {
                        value = nextNode.value;
                        return value;
                    }
                    nextNode = nextNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "MyHashMap{"
                + "arrayNodes="
                + Arrays.toString(arrayNodes)
                + '}';
    }

    public static class Node<K, V> {
        private Node<K, V> next;
        private int hash;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int hashCode() {
            int primeNumber = 17;
            primeNumber = 31 * primeNumber + ((this.key == null) ? 0 : this.key.hashCode());
            primeNumber = 31 * primeNumber + ((this.value == null) ? 0 : this.value.hashCode());
            return primeNumber;
        }

        @Override
        public boolean equals(Object node) {
            if (node == null) {
                return false;
            }
            if (this == node) {
                return true;
            }
            if (getClass() != node.getClass()) {
                return false;
            }
            Node<K, V> equalNode = (Node<K, V>) node;
            return (this.key == equalNode.key
                    || (this.key != null && this.key.equals(equalNode.key)))
                    && (this.value == equalNode.value
                    || (this.value != null && this.value.equals(equalNode.value)));
        }

        @Override
        public String toString() {
            return "Node{"
                    + "hash="
                    + hash
                    + ", key="
                    + key
                    + ", value="
                    + value
                    + ", next="
                    + next
                    + '}';
        }
    }

    public void setValue(Node<K, V> node,V newValue) {
        node.value = newValue;
    }

    public K getKey(Node<K, V> node) {
        return node.key;
    }

    public V getValueFromNode(Node<K, V> node) {
        return node.value;
    }

    public V[] getAllValues() {
        V[] valuesArray = (V[]) new Object[size];
        int index = 0;
        for (Node<K, V> node : arrayNodes) {
            if (node != null) {
                valuesArray[index] = node.value;
                index++;
            }
            if (node != null && node.next != null) {
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    valuesArray[index] = nextNode.value;
                    index++;
                    nextNode = nextNode.next;
                }
            }
            if (index >= size) {
                break;
            }
        }
        return valuesArray;
    }

    public K[] getAllKeys() {
        K[] keysArray = (K[]) new Object[size];
        int index = 0;
        for (Node<K, V> node : arrayNodes) {
            if (node != null) {
                keysArray[index] = getKey(node);
                index++;
            }
            if (node != null && node.next != null) {
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    keysArray[index] = nextNode.key;
                    index++;
                    nextNode = nextNode.next;
                }
            }
            if (index >= size) {
                break;
            }
        }
        return keysArray;
    }

    public void resize() {
        int oldCapacity = arrayNodes.length;
        size = 0;
        Node<K, V>[] oldArray = arrayNodes;
        arrayNodes = (Node<K, V>[]) new Node[oldCapacity * 2];
        threshold = (int) (arrayNodes.length * loadFactor);
        transfer(oldArray);
    }

    public void transfer(Node<K, V>[] oldArray) {
        Node<K, V> currentNode = null;
        for (Node<K, V> transferedNode: oldArray) {
            if (transferedNode != null) {
                put(getKey(transferedNode), getValueFromNode(transferedNode));
                currentNode = transferedNode.next;
            }
            while (currentNode != null) {
                put(getKey(currentNode), getValueFromNode(currentNode));
                currentNode = currentNode.next;
            }
        }
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    public int getHash(Node<K, V> node) {
        K key = getKey(node);
        return node.hash = ((key == null) ? 0 : (key.hashCode() & (arrayNodes.length - 1)));
    }

    public void simplePut(Node<K, V> node) {
        arrayNodes[getHash(node)] = node;
        size++;
    }

    public void putWithCollision(Node<K, V> node) {
        Node<K, V> currentNode = arrayNodes[getHash(node)];
        while (currentNode != null) {
            if (getKey(currentNode) == getKey(node)
                    || (getKey(currentNode) != null
                    && getKey(currentNode).equals(getKey(node)))) {
                setValue(currentNode, getValueFromNode(node));
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    public V getValueByNullKey(K key) {
        V value = null;
        for (Node<K, V> node = arrayNodes[0]; node != null; node = node.next) {
            if (getKey(node) == null) {
                value = getValueFromNode(node);
            }
        }
        return value;
    }
}
