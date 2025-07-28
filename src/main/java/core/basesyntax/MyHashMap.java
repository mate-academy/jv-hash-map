package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {


    private final int DEFAULT_CAPACITY = 16;

    private final double DEFAULT_LOAD_FACTOR = 0.75;

    private int size = 0;
    private double loadFactor;
    private int threshold;

    private Node<K, V>[] table;

    public MyHashMap() {
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        /* todo
            getHash of key
            check equals of keys in LL
            if match, replace, else add at the end
            at the end check on resize in another help function
        */
        int hash = hash(key);
        Node <K,V>[] tab = table;
        Node <K, V> nodeAtPosition;
        int tableLength, pointer;
        if (tab == null || (tableLength = tab.length) == 0) {
            tableLength = (tab = resize()).length;
        }
        if ((nodeAtPosition = tab[pointer = (hash & tableLength - 1)]) == null) {
            tab[pointer] = new Node<>(key, value, hash, null);
        } else {
            K exKey = nodeAtPosition.getKey();
            Node<K,V> existingMapping;
            if (nodeAtPosition.getHash() == hash &&
                    (exKey == key || (key != null && key.equals(exKey)))) {
                existingMapping = nodeAtPosition;
            } else {
              for (int bucket = 0; ; ++bucket) {
                  if ((existingMapping = nodeAtPosition.getNext()) == null) {
                      nodeAtPosition.setNext(new Node<>(key, value, hash, null));
                      break;
                  }
                  exKey = existingMapping.getKey();
                  if (existingMapping.getHash() == hash &&
                          (exKey == key || (key != null && key.equals(exKey)))) {
                      break;
                  }
                  nodeAtPosition = existingMapping;
              }
            }
            if (existingMapping != null) {
                existingMapping.setValue(value);
            }
        }
        if (++size > threshold) {
            resize();
        }

    }

    @Override
    public V getValue(K key) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private Node<K, V>[] resize() {
        int oldCap = (table == null) ? 0 : table.length;
        int oldThr = threshold;
        int newCap, newThr = 0;

        if (oldCap > 0) {
            newCap = oldCap << 1;
            if (oldThr > 0) {
                newThr = oldThr << 1;
            }
        }
        else {
            newCap = DEFAULT_CAPACITY;
            newThr = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        }
        threshold = newThr;
        return transfering(oldCap, newCap);
    }

    private Node<K, V>[] transfering(int oldCap, int newCap) {
        Node<K, V>[] oldTab = table;
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;

        for (int i = 0; i < oldCap; i++) {
            Node<K, V> node;
            if ((node = oldTab[i]) != null) {
                oldTab[i] = null;
                if (node.getNext() == null) {
                    newTab[node.getHash() & (newCap - 1)] = node;
                }
                else {
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K, V> next;
                    do {
                        next = node.getNext();
                        if ((node.getHash() & oldCap) == 0) {
                            if (loTail == null) {
                                loHead = node;
                            } else {
                                loTail.setNext(node);
                            }
                            loTail = node;
                        } else {
                            if (hiTail == null) {
                                hiHead = node;
                            } else {
                                hiTail.setNext(node);
                            }
                            hiTail = node;
                        }
                    } while ((node = node.getNext()) != null);
                    if (loTail != null) {
                        loTail.setNext(null);
                        newTab[i] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.setNext(null);
                        newTab[i + oldCap] = hiHead;
                    }
                }
            }
        }
        return newTab;
    }
}
